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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * 
 */
public class ItemListSerializer {
	
	public List<ItemStack> serialize(File file, String startPos) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (startPos != null) {
			startPos = "";
		}
		ConfigurationSection section = startPos != null && startPos != "" ?
				config.getConfigurationSection(startPos) : config;
		
		if (!section.isConfigurationSection("itemlist")) {
			error(file, "This file does not have item list.");
			return null;
		}
		
		section = section.getConfigurationSection("itemlist");
		
		//
		// Serializing..
		//
		
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		for (Map.Entry<String, Object> entry : section.getValues(true).entrySet()) {
			
			//
			// The section will contain something
			//
			
			if (!section.isConfigurationSection(entry.getKey())) {
				error(file, "List must have only stacks");
				return null;
			}
			
			ConfigurationSection entrySection = section.getConfigurationSection(entry.getKey());
			list.add(new ItemStackSerializer().serialize(file, entrySection.getCurrentPath()));
		}
		
		if (list.size() < 0) {
			System.out.println("ItemStack list is empty, returned empty list");
		}
		
		return list;
	}

	/**
	 * Types error in the console.
	 * 
	 * @param file The file that occur error
	 * @param reason The reason to occur this error
	 */
	private void error(File file, String reason) {
		System.out.println("Error while parsing file " + file.getPath());
		System.out.println("Reason: " + reason);
	}

}
