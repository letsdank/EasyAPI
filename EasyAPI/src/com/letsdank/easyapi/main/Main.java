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
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.letsdank.easyapi.craft.CustomCraft;
import com.letsdank.easyapi.craft.CustomCraftListSerializer;
import com.letsdank.easyapi.inv.ClickableInventory;
import com.letsdank.easyapi.inv.ClickableInventorySerializer;
import com.letsdank.easyapi.inv.ItemStackSerializer;
import com.letsdank.easyapi.inv.PseudoItemStack;

/**
 * 
 */
public class Main extends JavaPlugin {
	
	private static JavaPlugin instance;
	private static List<ItemStack> stacks;
	private static List<CustomCraft> customCrafts;
	private static List<ClickableInventory> clickableInventories;
	
	@Override
	public void onEnable() {
		stacks = new ArrayList<ItemStack>();
		customCrafts = new ArrayList<CustomCraft>();
		clickableInventories = new ArrayList<ClickableInventory>();
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
		
		startListeningCraft();
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("easyapi")) {
			sender.sendMessage("Give me cookies");
			
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
				System.out.println("Could not find inventory with id " + id);
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
	
	/**
	 * 
	 */
	private void startListeningCraft() {
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onCraft(InventoryClickEvent e) {
				if (e.getClickedInventory() == null) return;
				
				if (e.getClickedInventory().getType() == InventoryType.WORKBENCH) {
					if (!(e.getClickedInventory() instanceof CraftingInventory)) {
						return;
					}
					CraftingInventory inv = (CraftingInventory) e.getClickedInventory();
					
					if (e.getCurrentItem() == null) return;
					
					for (CustomCraft craft : customCrafts) {
						boolean correct = true;
						ItemStack[] matrix1 = craft.getMatrix();
						ItemStack[] matrix2 = inv.getMatrix();
						
						for (int i = 0; i < matrix1.length; i++) {
							if (matrix1[i] == null || matrix2[i] == null) {
								correct = false;
								break;
							}
							
							if (!new PseudoItemStack(matrix1[i]).equals(new PseudoItemStack(matrix2[i]))) {
								System.out.println("FALSEEEEEeqwrfergpjo");
								correct = false;
							}
						}
						
						System.out.println(toSttring(matrix1));
						System.out.println(toSttring(matrix2));
						
						if (correct) {
							e.getWhoClicked().sendMessage("Result?");
							System.out.println("Result?");
							inv.setResult(craft.getResult());
							Bukkit.getScheduler().scheduleSyncDelayedTask(Main.this, new Runnable() {
								
								@SuppressWarnings("deprecation")
								@Override
								public void run() {
									for (HumanEntity ent : inv.getViewers()) {
										((Player) ent).updateInventory();
									}
								}
							}, 1L);
						}
					}
				}
			}
		}, this);
	}

	private String toSttring(ItemStack[] matrix) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		
		builder.append("ItemStack[");
		for (ItemStack stack : matrix) {
			boolean isNull = false;
			if (stack == null) {
				builder.append("null");
				isNull = true;
			}
			
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			
			if (isNull) continue;
			
			builder.append(stack.toString());
		}
		
		builder.append("]");
		
		return builder.toString();
	}
}
