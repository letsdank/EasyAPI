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

import com.letsdank.easyapi.main.PluginLogger;

/**
 * 
 */
public class ClickableInventorySerializer {
	
	public ClickableInventory serialize(File file, String startPos) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = startPos != null && startPos != "" ?
				config.getConfigurationSection(startPos) : config;
		
		if (!config.isConfigurationSection("clickable")) {
			PluginLogger.error(
					"Error while parsing file %s: cannot find clickable section", 
					file.getName());
			return null;
		}
		
		int size = section.getInt("size", 27);
		
		section = section.getConfigurationSection("clickable");
		List<ClickableItemStack> stacks = new ArrayList<ClickableItemStack>();
		Inventory inv = Bukkit.createInventory(null, size);
		
		for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
			if (!section.isConfigurationSection(entry.getKey())) {
				if (entry.getKey().equalsIgnoreCase("size")) continue;
				PluginLogger.error(
						"Error while parsing file %s: clickable Inventory would contain only item stacks",
						file.getName());
				return null;
			}
			
			ConfigurationSection entrySection = section.getConfigurationSection(entry.getKey());
			if (!entrySection.isConfigurationSection("action")) {
				PluginLogger.error("Error while parsing file %s: It's not Clickable Item Stack",
						file.getName());
				return null;
			}
			
			String id = entrySection.getString("id");
			if (id == null) {
				id = entrySection.getName();
			}
			
			Material material = Material.valueOf(entrySection.getString("type").toUpperCase());
			if (material.equals(Material.AIR)) {
				PluginLogger.error("Error while parsing file %s: This button mustn't be empty!",
						file.getName());
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
		
		PluginLogger.success("Successful serialized clickable inventory from file %s", file.getName());
		PluginLogger.success("Start position: %s", startPos);
		
		return result;
	}
}
