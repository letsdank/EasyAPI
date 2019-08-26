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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.letsdank.easyapi.main.PluginLogger;

/**
 * 
 */
public class CustomCraftListSerializer {
	
	public List<CustomCraft> serialize(File file, String startPos) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = startPos != null && startPos != "" ?
				config.getConfigurationSection(startPos) : config;
		
		if (!section.isConfigurationSection("craftlist")) {
			if (!section.isConfigurationSection("craft")) {
				error(file, "This file does not have a list of custom crafts");
				return null;
			}
			
			List<CustomCraft> crafts = new ArrayList<CustomCraft>();
			crafts.add(new CustomCraftSerializer().serialize(file, startPos));
			return crafts;
		}
		
		section = section.getConfigurationSection("craftlist");
		
		List<CustomCraft> result = new ArrayList<CustomCraft>();
		for(Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
			if (!section.isConfigurationSection(entry.getKey())) {
				error(file, "This list must contain a custom crafts");
				return null;
			}
			
			result.add(new CustomCraftSerializer().serialize(file,
					section.getConfigurationSection(entry.getKey()).getCurrentPath()));
		}
		
		PluginLogger.success("Successful serialized custom craft list from file %s", file.getName());
		PluginLogger.success("Start position: %s", startPos);
		
		return result;
	}

	/**
	 * @param file
	 * @param reason
	 */
	private void error(File file, String reason) {
		PluginLogger.error("Error while parsing file %s: %s",
				file.getName(), reason);
	}
}
