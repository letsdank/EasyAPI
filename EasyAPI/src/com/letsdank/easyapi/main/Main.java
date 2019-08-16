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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.letsdank.easyapi.craft.CustomCraft;
import com.letsdank.easyapi.craft.CustomCraftListSerializer;
import com.letsdank.easyapi.inv.ClickableInventory;
import com.letsdank.easyapi.inv.ClickableInventorySerializer;
import com.letsdank.easyapi.inv.ItemStackSerializer;

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
}
