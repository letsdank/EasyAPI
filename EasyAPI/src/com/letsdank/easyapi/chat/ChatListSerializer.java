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
package com.letsdank.easyapi.chat;

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
public class ChatListSerializer {
	
	public List<TextComponent> serialize(File file, String startPos) {
		return serialize(file, startPos, false);
	}
	
	List<TextComponent> serialize(File file, String startPos, boolean extra) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = startPos != null && startPos != "" ?
				config.getConfigurationSection(startPos) : config;
		
		if (!section.isConfigurationSection("chatlist")) {
			PluginLogger.error(
					"Error while parsing file %s: cannot find chatlist", 
					file.getName());
			return null;
		}
		
		section = section.getConfigurationSection("chatlist");
		
		List<TextComponent> result = new ArrayList<TextComponent>();
		
		for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
			if (!section.isConfigurationSection(entry.getKey())) {
				PluginLogger.error(
						"Error while parsing file %s: this list should contain only sections",
						file.getName());
				return null;
			}
			
			ConfigurationSection entrySection = section.getConfigurationSection(entry.getKey());
			
			result.add(new ChatSerializer().serialize(file, entrySection.getCurrentPath(), extra));
		}
		
		PluginLogger.success("Successful serialized chat list from file %s", file.getName());
		PluginLogger.success("Start position: %s", startPos);
		
		return result;
	}
}
