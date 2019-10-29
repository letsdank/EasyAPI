package com.letsdank.easyapi.npc;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockCobbleWall;
import net.minecraft.server.v1_8_R3.BlockDoor;
import net.minecraft.server.v1_8_R3.BlockFence;
import net.minecraft.server.v1_8_R3.BlockFenceGate;
import net.minecraft.server.v1_8_R3.BlockMinecartTrackAbstract;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.IBlockAccess;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PathPoint;

public class PlayerPathfinderNormal extends PlayerPathfinderAbstract {
	private boolean f;
    private boolean g;
    private boolean h;
    private boolean i;
    private boolean j;
	
    public void a() {
    	super.a();
    	h = j;
    }
    
    public void a(boolean bool) {
    	f = bool;
    }
    
    public PathPoint a(Entity entity) {
    	int k;
    	if (i && entity.V()) {
    		k = (int) entity.getBoundingBox().b;
    		BlockPosition.MutableBlockPosition position = new BlockPosition.MutableBlockPosition(MathHelper.floor(entity.locX), k, MathHelper.floor(entity.locZ));
    		Block localBlock = a.getType(position).getBlock();
    		while ((localBlock == Blocks.FLOWING_WATER) || (localBlock == Blocks.WATER)) {
    			k++;
    			position.c(MathHelper.floor(entity.locX), k, MathHelper.floor(entity.locZ));
    			localBlock = a.getType(position).getBlock();
    		}
    		h = false;
    	} else {
    		k = MathHelper.floor(entity.getBoundingBox().b + 0.5D);
    	}
    	return a(MathHelper.floor(entity.getBoundingBox().a), k, MathHelper.floor(entity.getBoundingBox().c));
    }
    
    public PathPoint a(Entity entity, double x, double y, double z) {
    	return a(MathHelper.floor(x - entity.width / 2.0F), MathHelper.floor(y), MathHelper.floor(z - entity.width / 2.0F));
    }
    
    private int a(Entity entity, int x, int y, int z) {
    	return a(a, entity, x, y, z, c, d, e, h, g, f);
    }
    
    private PathPoint a(Entity entity, int x, int y, int z, int speed) {
    	PathPoint point = null;
    	int k = a(entity, x, y, z);
    	if (k == 2) {
    		return a(x, y, z);
    	}
    	if (k == 1) {
    		point = a(x, y, z);
    	}
    	if ((point == null) && (speed > 0) && (k != -3) && (k != -4) && (a(entity, x, y + speed, z) == 1)) {
    		point = a(x, y + speed, z);
    		y += speed;
    	}
    	if (point != null) {
    		int m = 0;
    		int n = 0;
    		while (y > 0) {
    			n = a(entity, x, y - 1, z);
    			if (h && n == -1) {
    				return null;
    			}
    			if (n == 1) {
    				if (m++ >= entity.aE()) {
    					return null;
    				}
    				y--;
    				if (y > 0) {
    					point = a(x, y, z);
    				} else {
    					return null;
    				}
    			} else {
    				break;
    			}
    		}
    		if (n == -2) {
    			return null;
    		}
    	}
    	
    	return point;
    }
    
    public void a(IBlockAccess block, Entity entity) {
    	super.a(block, entity);
    	j = h;
    }
    
    public int a(PathPoint[] points, Entity entity, PathPoint point1,
    		PathPoint point2, float f) {
    	int k = 0;
    	
    	int m = 0;
    	if (a(entity, point1.a, point2.b + 1, point2.c) == 1) {
    		m = 1;
    	}
    	
    	PathPoint localPoint1 = a(entity, point1.a, point1.b, point1.c + 1, m);
    	PathPoint localPoint2 = a(entity, point1.a - 1, point1.b, point1.c, m);
    	PathPoint localPoint3 = a(entity, point1.a + 1, point1.b, point1.c, m);
    	PathPoint localPoint4 = a(entity, point1.a, point1.b, point1.c - 1, m);
    	
    	if ((localPoint1 != null) && (!localPoint1.i) && (localPoint1.a(point2) < f)) {
    		points[k++] = localPoint1;
    	}
    	
    	if ((localPoint2 != null) && (!localPoint2.i) && (localPoint2.a(point2) < f)) {
    		points[k++] = localPoint2;
    	}
    	
    	if ((localPoint3 != null) && (!localPoint3.i) && (localPoint3.a(point2) < f)) {
    		points[k++] = localPoint3;
    	}
    	
    	if ((localPoint4 != null) && (!localPoint4.i) && (localPoint4.a(point2) < f)) {
    		points[k++] = localPoint4;
    	}
    	
    	return k;
    }
    
    public boolean b() {
        return f;
    }

    public void b(boolean bool) {
        g = bool;
    }

    public void c(boolean bool) {
        h = bool;
    }

    public boolean d() {
        return i;
    }

    public void d(boolean bool) {
        i = bool;
    }

    public boolean e() {
        return h;
    }
    
    public static int a(IBlockAccess block, Entity entity, int x0, int y0, int z0, int x1, int y1, int z1, boolean onlyX, boolean onlyY, boolean onlyZ) {
    	int k = 0;
    	BlockPosition position = new BlockPosition(entity);
    	
    	BlockPosition.MutableBlockPosition mutablePosition = new BlockPosition.MutableBlockPosition();
    	for (int x = x0; x < x0 + x1; x++) {
    		for (int y = y0; y < y0 + y1; x++) {
    			for (int z = z0; z < z0 + z1; z++) {
    				mutablePosition.c(x, y, z);
    				Block b = block.getType(mutablePosition).getBlock();
    				if (b.getMaterial() != Material.AIR) {
    					if ((b == Blocks.TRAPDOOR) || (b == Blocks.IRON_TRAPDOOR)) {
    						k = 1;
    					} else if ((b == Blocks.FLOWING_WATER) || (b == Blocks.WATER)) {
    						if (onlyX) {
    							return -1;
    						}
    						k = 1;
    					} else if ((!onlyZ) && ((b instanceof BlockDoor)) && (b.getMaterial() == Material.WOOD)) {
    						return 0;
    					}
    					if ((entity.world.getType(mutablePosition).getBlock() instanceof BlockMinecartTrackAbstract)) {
    						if ((!(entity.world.getType(position).getBlock() instanceof BlockMinecartTrackAbstract)) && (!(entity.world.getType(position.down()).getBlock() instanceof BlockMinecartTrackAbstract))) {
    							return -3;
    						}
    					} else if (!b.b(block, mutablePosition)) {
    						if ((!onlyY) || (!(b instanceof BlockDoor)) || (b.getMaterial() != Material.WOOD)) {
    							if (((b instanceof BlockFence)) || (b instanceof BlockFenceGate) || ((b instanceof BlockCobbleWall))) {
    								return -3;
    							}
    						}
    						if ((b == Blocks.TRAPDOOR) || (b == Blocks.IRON_TRAPDOOR)) {
    							return -4;
    						}
    						Material material = b.getMaterial();
    						if (material == Material.LAVA) {
    							if (!entity.ab()) {
    								return -2;
    							}
    						} else {
    							return 0;
    						}
    					}
    				}
    			}
    		}
    	}
    	return k != 0 ? 2 : 1;
    }
}