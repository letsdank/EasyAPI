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
package com.letsdank.easyapi.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.letsdank.easyapi.craft.CustomCraft;
import com.letsdank.easyapi.craft.CustomCraftListSerializer;
import com.letsdank.easyapi.inv.ClickableInventory;
import com.letsdank.easyapi.inv.ClickableInventorySerializer;
import com.letsdank.easyapi.inv.ItemStackSerializer;
import com.letsdank.easyapi.inv.SettingsInventory;
import com.letsdank.easyapi.utils.PacketManager;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R1.PlayerConnection;

/**
 * 
 */
public class Main extends JavaPlugin {
	private static JavaPlugin instance;
	private static List<ItemStack> stacks;
	private static List<CustomCraft> customCrafts;
	private static List<ClickableInventory> clickableInventories;
	
	private static Map<String, EntityPlayer> npcs;
	
	private static final String[] commands = new String[] {
			"infologging"
	};
	
	public static void broadcastJSONMessage(String msg) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) online).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(msg)));
		}
	}
	
	@Override
	public void onEnable() {
		new PluginLogger(getLogger());
		PluginLogger.success("Initializated success!");
		
		stacks = new ArrayList<ItemStack>();
		customCrafts = new ArrayList<CustomCraft>();
		clickableInventories = new ArrayList<ClickableInventory>();
		npcs = new HashMap<String, EntityPlayer>();
		
		instance = this;
		
		//
		// Plugin Initialization
		//
		
		File itemPath = new File(getDataFolder(), "itemstacks");
		if (!itemPath.exists()) itemPath.mkdirs();
		
		for (File file : itemPath.listFiles()) {
			stacks.add(new ItemStackSerializer().serialize(file, ""));
		}
		
		File invPath = new File(getDataFolder(), "inventories");
		if (!invPath.exists()) invPath.mkdirs();
		
		for (File file : invPath.listFiles()) {
			clickableInventories.add(new ClickableInventorySerializer().serialize(file, ""));
		}
		
		File craftPath = new File(getDataFolder(), "customrecipes");
		if (!craftPath.exists()) craftPath.mkdirs();
		
		for (File file : craftPath.listFiles()) {
			customCrafts.addAll(new CustomCraftListSerializer().serialize(file, ""));
		}
		
		//
		// Custom Craft Engine Initialization
		//
		
		for (CustomCraft craft : customCrafts) {
			Bukkit.addRecipe(craft.toRecipe());
		}
		
		//
		// We cannot see a npc while we will not send packet to see those npcs.
		//
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onJoin(PlayerJoinEvent e) {
				for (Map.Entry<String, EntityPlayer> entry : npcs.entrySet()) {
					showNpc(entry.getValue(), e.getPlayer());
				}
			}
		}, this);
	}
	
	@Override
	public void onDisable() {
		clearNpc();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("easyapi")) {
			if (args.length <= 0) {
				if (sender instanceof Player) {
					((Player) sender).openInventory(new SettingsInventory().getInv());
				} else {
					sender.sendMessage("This feature is available only for players");
					return true;
				}
			} else {
				String arg = args[0];
				
				if (arg.equalsIgnoreCase("help")) {
					
					//
					// show usage
					//
					
					sender.sendMessage("List of commands available for managing plugin:");
					for (String cmd : commands) {
						sender.sendMessage(" - " + cmd);
					}
				} else if (arg.equalsIgnoreCase("infologging")) {
					sender.sendMessage("Setting infoLogging to" + !PluginLogger.infoLogging);
					PluginLogger.infoLogging = !PluginLogger.infoLogging;
				}
			}
			
			return true;
		} else if (command.getName().equalsIgnoreCase("inv")) {
			if (args.length <= 0) {
				// should print usage
				return false;
			}
			
			String id = args[0];
			if (id.equalsIgnoreCase("list")) {
				
				//
				// Shows list of available clickable inventories.
				//
				
				sender.sendMessage("Available Inventories:");
				
				for (ClickableInventory entry : clickableInventories) {
					sender.sendMessage(" - " + entry.getId());
				}
				
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command is available only for players!");
				return true;
			}
			
			ClickableInventory inv = null;
			
			for (ClickableInventory entry : clickableInventories) {
				if (entry.getId().equalsIgnoreCase(id)) {
					inv = entry;
				}
			}
			
			if (inv == null) {
				sender.sendMessage("Could not find inventory with id " + id);
				return true;
			}
			
			((Player) sender).openInventory(inv.getInventory());
			return true;
		} else if (command.getName().equalsIgnoreCase("craft")) {
			if (args.length <= 0) {
				return false;
			}
			
			String id = args[0];
			
			if (id.equalsIgnoreCase("list")) {
				sender.sendMessage("Available Custom Crafts:");
				
				for (CustomCraft craft : customCrafts) {
					sender.sendMessage(" - " + craft.getId());
				}
				
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command is available only for players!");
				return true;
			}
			
			CustomCraft craft = null;
			
			for (CustomCraft entry : customCrafts) {
				if (entry.getId().equalsIgnoreCase(id)) {
					craft = entry;
				}
			}
			
			if (craft == null) {
				sender.sendMessage("Could not find Custom Craft with id " + id);
				return true;
			}
			
			Inventory inv = Bukkit.createInventory(null, InventoryType.WORKBENCH);
			
			for (int i = 0; i < craft.getMatrix().length; i++) {
				inv.setItem(i + 1, craft.getMatrix()[i]);
			}
			((Player) sender).openInventory(inv);
			inv.setItem(0, craft.getResult());
			
			//
			// Create a special listener to not duping items :)
			//
			
			Bukkit.getPluginManager().registerEvents(new Listener() {
				@EventHandler
				public void onClick(InventoryClickEvent e) {
					if (e.getClickedInventory() != null) {
						if (e.getClickedInventory().equals(inv)) {
							e.setCancelled(true);
						}
					}
				}
				
				@EventHandler
				public void onClose(InventoryCloseEvent e) {
					if (e.getInventory().equals(inv)) {
						//
						// Unload this listener
						//
						
						HandlerList.unregisterAll(this);
					}
				}
			}, Main.instance);
		} else if (command.getName().equalsIgnoreCase("spawnnpc")) {
			if (args.length <= 1) return false; // print usage
			
			String id = args[0];
			String skin = args[1];
			
			if (!(sender instanceof Player)) {
				sender.sendMessage("This feature is available only for players");
				return true;
			}
			
			for (Map.Entry<String, EntityPlayer> entry : npcs.entrySet()) {
				if (id.equalsIgnoreCase(entry.getKey())) {
					sender.sendMessage("This npc with ID " + id + " are exists");
					return true;
				}
			}
			
			Player p = (Player) sender;
			Location loc = p.getLocation();
			EntityPlayer player = PacketManager.spawnPlayer(skin, loc);

			npcs.put(id, player);
			showNpc(player);
			return true;
		} else if (command.getName().equalsIgnoreCase("destroynpc")) {
			if (args.length <= 0) return false;
			
			String id = args[0];
			EntityPlayer removeReturned = null;
			
			for (Map.Entry<String, EntityPlayer> entry : npcs.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(id)) {
					removeReturned = npcs.remove(entry.getKey());
				}
			}
			
			if (removeReturned == null) {
				sender.sendMessage("The NPC with ID " + id + " is not exist");
				return true;
			}
			
			hideNpc(removeReturned);
			
			return true;
		} else if (command.getName().equalsIgnoreCase("clearnpc")) {
			clearNpc();
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return the stacks
	 */
	public static List<ItemStack> getStacks() {
		return stacks;
	}
	
	/**
	 * @return the customCrafts
	 */
	public static List<CustomCraft> getCustomCrafts() {
		return customCrafts;
	}
	
	/**
	 * @return the instance
	 */
	public static JavaPlugin getInstance() {
		return instance;
	}
	
	public static void addNpc(String id, EntityPlayer npc) {
		npcs.put(id, npc);
	}
	
	public static EntityPlayer removeNpc(String id) {
		return npcs.remove(id);
	}
	
	private void showNpc(EntityPlayer player) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			showNpc(player, p);
		}
	}
	
	private void showNpc(EntityPlayer player, Player to) {
		PlayerConnection connection = ((CraftPlayer) to).getHandle().playerConnection;
		
		connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, player));
		connection.sendPacket(new PacketPlayOutNamedEntitySpawn(player));
		connection.sendPacket(new PacketPlayOutAnimation(player, 1));
	}
	
	private void hideNpc(EntityPlayer player) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutEntityDestroy(player.getId()));
		}
	}
	
	private void clearNpc() {
		for (Map.Entry<String, EntityPlayer> entry : npcs.entrySet()) {
			EntityPlayer player = entry.getValue();
			hideNpc(player);
		}
		
		npcs.clear();
	}
}
