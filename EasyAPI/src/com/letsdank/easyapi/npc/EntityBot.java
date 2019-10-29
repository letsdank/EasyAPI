/*
 * Copyright 2019 LetsDank.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.letsdank.easyapi.npc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.letsdank.easyapi.main.Main;
import com.letsdank.easyapi.main.PluginLogger;
import com.letsdank.easyapi.utils.NMSClass;
import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NavigationAbstract;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

/**
 * 
 */
public class EntityBot extends EntityPlayer {
	
	EntityInsentient insentient;
	
	private PlayerControllerMove move;
	private PlayerControllerLook look;
	private PlayerControllerJump jump;
	private PlayerNavigation navigation;
	
	private int jumpTicks = 0;
    private int updateCounter = 0;
    private final Location packetLocationCache = new Location(null, 0, 0, 0);
	
	public EntityBot(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile,
			PlayerInteractManager playerinteractmanager) {
		super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
		
		try {
			NetworkManager manager = new NullNetworkManager(EnumProtocolDirection.CLIENTBOUND);
			playerConnection = new NullNetServerHandler(server, manager, this);
			manager.a(playerConnection);
		} catch (Exception e) {
			PluginLogger.error("An internal error has occured while creating a NPC.");
			e.printStackTrace();
		}
		
		AttributeInstance range = getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
		if (range == null) {
			range = getAttributeMap().b(GenericAttributes.FOLLOW_RANGE);
		}
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16);
		
		this.noDamageTicks = 1;
		this.S = 0;
			
		insentient = new EntityInsentient(world) {};
		
		move = new PlayerControllerMove(this);
		look = new PlayerControllerLook(this);
		jump = new PlayerControllerJump(this);
		
		navigation = new PlayerNavigation(this, world);
		S = 1;
	}
	
	private void moveOnCurrentHeading() {
        if (aY) {
            if (onGround && jumpTicks == 0) {
                bF();
                jumpTicks = 10;
            }
        } else {
            jumpTicks = 0;
        }
        aZ *= 0.98F;
        ba *= 0.98F;
        bb *= 0.9F;
        g(aZ, ba); // movement method
        setHeadYaw(yaw);
        if (jumpTicks > 0) {
            jumpTicks--;
        }
    }
	
	private void setHeadYaw(float yaw) {
		yaw = clampYaw(yaw);
		aJ = yaw;
		aK = yaw;
	}
	
	private float clampYaw(float yaw) {
        while (yaw < -180.0F) {
            yaw += 360.0F;
        }

        while (yaw >= 180.0F) {
            yaw -= 360.0F;
        }
        return yaw;
    }
	
	public void t_() {
		super.t_();
		
		if (updateCounter + 1 > 30) {
			updateEffects = true;
		}
		noclip = isSpectator();
		livingEntityBaseTick();

		boolean navigating = !navigation.m();
		updatePackets(navigating);
		
		if (!navigating && getBukkitEntity() != null) {
			g(0, 0);
		}
		
		if (Math.abs(motX) < EPSILON && Math.abs(motY) < EPSILON && Math.abs(motZ) < EPSILON) {
			motX = motY = motZ = 0;
		}
		
		if (navigating) {
			updateNavigationWorld(bukkitEntity, bukkitEntity.getWorld());
			moveOnCurrentHeading();
		}
		
		if (noDamageTicks > 0) {
			--noDamageTicks;
		}
		
		trySwim();
	}
	
	public void trySwim() {
		Random rnd = new Random();
		
		if (rnd.nextFloat() < 0.8F && (W() || ab())) {
			motY += 0.04F;
		}
	}

	private void updatePackets(boolean navigating) {
        if (updateCounter++ <= 30)
            return;
        updateCounter = 0;
        Location current = getBukkitEntity().getLocation(packetLocationCache);
        Packet<?>[] packets = new Packet[navigating ? this.inventory.armor.length : this.inventory.armor.length + 1];
        if (!navigating) {
            packets[this.inventory.armor.length] = new PacketPlayOutEntityHeadRotation(this,
            		(byte)(yaw * 256.0F / 360.0F));
        }
        for (int i = 0; i < this.inventory.armor.length; i++) {
            packets[i] = new PacketPlayOutEntityEquipment(getId(), i, getEquipment(i));
        }
        sendPacketsNearby(getBukkitEntity(), current, packets);
    }
	
	public static void sendPacketsNearby(Player from, Location location, Packet<?>... packets) {
        sendPacketsNearby(from, location, Arrays.asList(packets), 64);
    }
	
	public static void sendPacketsNearby(Player from, Location location, Collection<Packet<?>> packets, double radius) {
        radius *= radius;
        final org.bukkit.World world = location.getWorld();
        for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
            if (ply == null || world != ply.getWorld() || (from != null && !ply.canSee(from))) {
                continue;
            }
            if (location.distanceSquared(ply.getLocation(PACKET_CACHE_LOCATION)) > radius) {
                continue;
            }
            for (Packet<?> packet : packets) {
                ((CraftPlayer) ply).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
	
	public void livingEntityBaseTick() {
        if (!this.world.isClientSide) {
            b(0, this.fireTicks > 0);
        }
        this.ay = this.az;
        this.aE = this.aF;
        if (this.hurtTicks > 0) {
            this.hurtTicks -= 1;
        }
        bi();
        this.aU = this.aT;
        this.aJ = this.aI;
        this.aL = this.aK;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }

	/**
	 * @param a
	 * @return
	 */
	public PlayerControllerMove getControllerMove() {
		return move;
	}
	
	/**
	 * @return the jump
	 */
	public PlayerControllerJump getControllerJump() {
		return jump;
	}
	
	/**
	 * @return the look
	 */
	public PlayerControllerLook getControllerLook() {
		return look;
	}
	
	/**
	 * @return the navigation
	 */
	public PlayerNavigation getNavigation() {
		return navigation;
	}
	
	public void updateNavigationWorld(org.bukkit.entity.Entity entity, org.bukkit.World world) {
        if (NAVIGATION_WORLD_FIELD == null)
            return;
        Entity en = ((CraftPlayer) entity).getHandle();
        if (en == null || !(en instanceof EntityInsentient))
            return;
        EntityInsentient handle = (EntityInsentient) en;
        WorldServer worldHandle = ((CraftWorld) world).getHandle();
        try {
            NAVIGATION_WORLD_FIELD.set(handle.getNavigation(), worldHandle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        // knock back velocity is cancelled and sent to client for handling when
        // the entity is a player. there is no client so make this happen
        // manually.
        boolean damaged = super.damageEntity(damagesource, f);
        if (damaged && velocityChanged) {
            velocityChanged = false;
            Bukkit.getScheduler().runTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    EntityBot.this.velocityChanged = true;
                }
            });
        }
        return damaged;
    }
	
	public void setMoveDestination(double x, double y, double z, double speed) {
        move.a(x, y, z, speed);
    }
	
	public void setShouldJump() {
        jump.a();
    }
	
	public void setTargetLook(EntityLiving target) {
		setTargetLook(target, 10, 40);
	}
	
	public void setTargetLook(EntityLiving target, float yawOffset, float renderOffset) {
        look.a(target, yawOffset, renderOffset);
    }

    public void setTargetLook(Location target) {
        look.a(target.getX(), target.getY(), target.getZ(), 10, 40);
    }
    
    public void attack(Entity entity) {
    	if (!canReachEntity(entity) || dead || getHealth() == 0.0F)
    		return;
    	super.attack(entity);
    }
    
	private boolean canReachEntity(Entity entity) {
		BoundingBox handleBB = new BoundingBox(getBoundingBox()), targetBB = new BoundingBox(entity.getBoundingBox());
        return (handleBB.maxY > targetBB.minY && handleBB.minY < targetBB.maxY)
                && getBukkitEntity().getLocation(HANDLE_LOCATION).distanceSquared(
                		entity.getBukkitEntity().getLocation(TARGET_LOCATION)) 
                <= getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue()
                && hasLineOfSight(entity);
	}

	public void playAnimation(EnumPlayerAnimation type) {
    	if (type.hasCode()) {
    		if (type == EnumPlayerAnimation.ARM_SWING) {
    			attack(((CraftPlayer)Bukkit.getPlayer("LetsDank")).getHandle());
    		}
	    	PacketPlayOutAnimation packet = new PacketPlayOutAnimation(this, type.getCode());
	    	sendPacketsNearby(getBukkitEntity(), getBukkitEntity().getLocation(), packet);
	    	return;
    	}
    	
    	switch (type) {
    		case SIT:
    			break; // not supported yet
    		case SNEAK:
    			getBukkitEntity().setSneaking(true);
    			break;
    		case STOP_SNEAKING:
    			getBukkitEntity().setSneaking(false);
    			break;
    		default:
    			break;
    			
    	}
    	
    	sendPacketNearby(new PacketPlayOutEntityMetadata(getId(), getDataWatcher(), true));
    }

	/**
	 * @param packetPlayOutEntityMetadata
	 */
	public void sendPacketNearby(Packet<?> packet) {
		sendPacketsNearby(getBukkitEntity(), getBukkitEntity().getLocation(), packet);
	}

	private static final float EPSILON = 0.005F;
	private static Field NAVIGATION_WORLD_FIELD = NMSClass.getField(NavigationAbstract.class, "c");
	public static final Location PACKET_CACHE_LOCATION = new Location(null, 0, 0, 0);
    // private static final Location LOADED_LOCATION = new Location(null, 0, 0, 0);
    private static final Location HANDLE_LOCATION = new Location(null, 0, 0, 0);
    private static final Location TARGET_LOCATION = new Location(null, 0, 0, 0);
}
