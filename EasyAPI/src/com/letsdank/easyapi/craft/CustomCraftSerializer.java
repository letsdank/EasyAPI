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
package com.letsdank.easyapi.craft;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.letsdank.easyapi.inv.ItemStackSerializer;

/**
 * 
 */
public class CustomCraftSerializer {
	
	public CustomCraft serialize(File file, String startPos) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = startPos != null && startPos != "" ?
				config.getConfigurationSection(startPos) : config;
		
		if (!section.isConfigurationSection("craft")) {
			error(file, "This file does not have a custom craft");
			return null;
		}
		
		section = section.getConfigurationSection("craft");
		
		String id = section.getString("id");
		
		if (id == null) {
			if (section.getParent() != null) {
				id = section.getParent().getName();
				if (id == null) {
					error(file, "Could not find id for Custom Craft");
				}
			} else {
				id = "craft";
			}
		}
		CustomCraftType craftType = CustomCraftType.valueOf(section.getString("type").toUpperCase());
		List<String> craftPattern = section.getStringList("pattern");
		Map<String, ItemStack> craftKeys = new HashMap<String, ItemStack>();
		
		ConfigurationSection keySection = section.getConfigurationSection("keys");
		for (Map.Entry<String, Object> entry : keySection.getValues(false).entrySet()) {
			if (!keySection.isConfigurationSection(entry.getKey())) {
				error(file, "This list should contain an item stacks");
				return null;
			}
			
			ConfigurationSection entrySection = keySection.getConfigurationSection(entry.getKey());
			ItemStack entryStack = new ItemStackSerializer().serialize(file, entrySection.getCurrentPath());
			
			craftKeys.put(entrySection.getName(), entryStack);
		}
		
		ItemStack craftResult = new ItemStackSerializer().serialize(file,
				section.getConfigurationSection("result").getCurrentPath());
		
		return new CustomCraft(id, craftType, craftPattern, craftKeys, craftResult);
	}

	/**
	 * @param file
	 * @param reason
	 */
	private void error(File file, String reason) {
		System.out.println("Error while parsing file " + file.getPath());
		System.out.println("Reason: " + reason);
	}
}
