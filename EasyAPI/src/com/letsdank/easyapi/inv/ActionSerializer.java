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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.letsdank.easyapi.chat.ChatSerializer;
import com.letsdank.easyapi.main.Main;
import com.letsdank.easyapi.main.PluginLogger;

/**
 * 
 */
public class ActionSerializer {
	
	private Inventory inv;
	private ItemStack button;
	
	public ActionSerializer(Inventory inv, ItemStack button) {
		this.inv = inv;
		this.button = button;
	}
	
	public List<Runnable> serialize(File file, String startPos) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection actionSection = startPos != null && startPos != "" ?
				config.getConfigurationSection(startPos) : config;
		
		if (!actionSection.isConfigurationSection("action")) {
			PluginLogger.error(
					"Error while parsing file %s: cannot find action section", 
					file.getName());
			return null;
		}
		
		actionSection = actionSection.getConfigurationSection("action");
		List<Runnable> ret = new ArrayList<Runnable>();
		
		for (Map.Entry<String, Object> entry : actionSection.getValues(false).entrySet()) {
			if (!actionSection.isConfigurationSection(entry.getKey())) {
				continue;
			}
			
			ConfigurationSection section = actionSection.getConfigurationSection(entry.getKey());
			
			switch (entry.getKey().toLowerCase()) {
				case "stack":
					ItemStack newStack = new ItemStackSerializer().serialize(file, actionSection.getCurrentPath());
					
					ret.add(new Runnable() {
						@Override
						public void run() {
							
							//
							// Getting exact ItemStack in the inventory
							//
							
							int pos = 0;
							
							for (int k = 0; k < inv.getSize(); k++) {
								if (inv.getItem(k) == null) continue;
								if (inv.getItem(k).equals(button)) {
									pos = k;
								}
							}
							
							inv.setItem(pos, newStack);
						}
					}); break;
				case "chat":
					String rawMsg = new ChatSerializer().serialize(file, section.getParent().getCurrentPath()).toJSONmessage();
					ret.add(new Runnable() {
						@Override
						public void run() {
							Main.broadcastJSONMessage(rawMsg);
						}
					});
					break;
			}
		}
		
		PluginLogger.success("Successful serialized action from file %s", file.getName());
		PluginLogger.success("Start position: %s", startPos);
		
		return ret;
	}
}
