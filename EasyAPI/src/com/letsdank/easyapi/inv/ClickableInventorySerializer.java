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
package com.letsdank.easyapi.inv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * 
 */
public class ClickableInventorySerializer {
	
	public ClickableInventory serialize(File file, String startPos) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = startPos != null && startPos != "" ?
				config.getConfigurationSection(startPos) : config;
		
		if (!config.isConfigurationSection("clickable")) {
			System.out.println("Its not Clickable Inventory, return null");
			return null;
		}
		
		int size = 27; // default inventory size
		
		section = section.getConfigurationSection("clickable");
		List<ClickableItemStack> stacks = new ArrayList<ClickableItemStack>();
		Inventory inv = Bukkit.createInventory(null, size);
		
		for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
			if (!section.isConfigurationSection(entry.getKey())) {
				if (entry.getKey().equalsIgnoreCase("size")) {
					if (!(entry.getValue() instanceof Integer)) {
						System.out.println("Size should be Integer type!");
						return null;
					}
					size = (int) entry.getValue();
				} else {
					System.out.println("Clickable Inventory would contain only item stacks");
					return null;
				}
			}
			
			ConfigurationSection entrySection = section.getConfigurationSection(entry.getKey());
			if (!entrySection.isConfigurationSection("action")) {
				System.out.println("It's not Clickable Item Stack, return null");
				return null;
			}
			
			String id = entrySection.getString("id");
			if (id == null) {
				id = entrySection.getName();
			}
			
			Material material = Material.valueOf(entrySection.getString("type").toUpperCase());
			if (material.equals(Material.AIR)) {
				System.out.println("This button mustn't be empty!");
				return null;
			}
			
			int amount = entrySection.getInt("amount");
			String name = entrySection.getString("name");
			// String localizedName = entrySection.getString("localizedName");
			List<String> lore = entrySection.getStringList("lore");
			
			ConfigurationSection actionSection = entrySection.getConfigurationSection("action");
			
			ItemStack button = new ItemStackBuilder(material)
					.withAmount(amount)
					.withName(name)
					.withLore(lore)
					.build();
			
			stacks.add(new ClickableItemStack(id, button, new ActionSerializer(inv, button).serialize(file, actionSection.getParent().getCurrentPath())));
					
		}
		
		String fileName = file.getName();
		String name = fileName.substring(0, fileName.lastIndexOf('.'));
		
		ClickableInventory result = new ClickableInventory(inv, name);
		
		for (ClickableItemStack stack : stacks) {
			result.addButton(stack, 0);
		}
		
		return result;
	}
}
