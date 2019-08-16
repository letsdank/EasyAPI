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
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * 
 */
public class ItemStackSerializer {

	public ItemStack serialize(File file, String startPos) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = startPos != null && startPos != "" ?
				config.getConfigurationSection(startPos) : config;
		
		if (!section.isConfigurationSection("stack")) {
			error(file, "This file does not have an item stack.");
			return null;
		}
		
		section = section.getConfigurationSection("stack");
		
		if (section.contains("type")) {
			Material material = Material.valueOf(section.getString("type").toUpperCase());
			int amount = section.getInt("amount", 1);
			String name = section.getString("name");
			List<String> lore = section.getStringList("lore");
			String localizedName = section.getString("localizedName");
			
			//
			// Enchantments will be soon..
			//
			
			return new ItemStackBuilder(material)
					.withAmount(amount)
					.withName(name)
					.withLocalizedName(localizedName)
					.withLore(lore).build();
		}
		
		error(file, "Item stack must typed");
		return null;
	}

	/**
	 * @param file
	 * @param string
	 */
	private void error(File file, String reason) {
		System.out.println("Error while parsing file " + file.getPath());
		System.out.println("Reason: " + reason);
	}

}
