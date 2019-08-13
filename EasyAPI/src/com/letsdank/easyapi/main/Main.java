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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.letsdank.easyapi.craft.CustomCraft;
import com.letsdank.easyapi.craft.CustomCraftListSerializer;
import com.letsdank.easyapi.inv.ItemStackSerializer;

/**
 * 
 */
public class Main extends JavaPlugin {
	
	private static JavaPlugin instance;
	private static List<ItemStack> stacks;
	private static List<CustomCraft> customCrafts;
	
	@Override
	public void onEnable() {
		customCrafts = new ArrayList<CustomCraft>();
		instance = this;
		
		//
		// Plugin Initialization
		//
		
		File itemPath = new File(getDataFolder(), "stacks");
		if (!itemPath.exists()) itemPath.mkdirs();
		
		for (File file : itemPath.listFiles()) {
			stacks.add(new ItemStackSerializer().serialize(file, ""));
		}
		
		File craftPath = new File(getDataFolder(), "recipes");
		if (!craftPath.exists()) craftPath.mkdirs();
		
		for (File file : craftPath.listFiles()) {
			customCrafts.addAll(new CustomCraftListSerializer().serialize(file, null));
		}
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("easyapi")) {
			sender.sendMessage("Give me cookies");
			
			return true;
		}
		
		return true;
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
