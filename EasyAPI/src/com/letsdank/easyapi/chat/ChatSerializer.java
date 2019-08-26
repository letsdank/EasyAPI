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
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

/**
 * 
 */
public class ChatSerializer {
	
	public TextComponent serialize(File file, String startPos) {
		return serialize(file, startPos, false);
	}
	
	TextComponent serialize(File file, String startPos, boolean parent) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = startPos != null && startPos != "" ?
				config.getConfigurationSection(startPos) : config;

		if (!section.isConfigurationSection("chat")) {
			System.out.println("Cannot find \"chat\" to serialize");
			return null;
		}
		
		section = section.getConfigurationSection("chat");
		
		String text = section.getString("text");
		if (text == null) {
			System.out.println("Text Component should contain text value!");
			return null;
		}
		
		TextComponent result = new TextComponent(text);
		result.setBold(section.getBoolean("bold"));
		result.setItalic(section.getBoolean("italic"));
		result.setUnderlined(section.getBoolean("underlined"));
		result.setStrikethrough(section.getBoolean("strikethrough"));
		result.setObfuscated(section.getBoolean("obfuscated"));
		result.setColor(ChatColor.valueOf(section.getString("color", "white").toUpperCase()));
		result.setInsertion(section.getString("insertion"));
		
		if (section.isConfigurationSection("clickEvent")) {
			ConfigurationSection clickSection = section.getConfigurationSection("clickEvent");
			ChatClickEventType clickType = ChatClickEventType.valueOf(clickSection.getString("action").toUpperCase());
			String clickValue = clickSection.getString("value");
			
			result.setClickEvent(clickType, clickValue);
		}
		
		if (section.isConfigurationSection("hoverEvent")) {
			ConfigurationSection hoverSection = section.getConfigurationSection("hoverEvent");
			ChatHoverEventType hoverType = ChatHoverEventType.valueOf(hoverSection.getString("action").toUpperCase());
			String hoverValue = hoverSection.getString("value");
			
			result.setHoverEvent(hoverType, hoverValue);
		}
		
		//
		// extra feature:
		//
		
		if (section.isConfigurationSection("extra")) {
			
			//
			// At first, we need to check, if itsn't parent.
			//
			
			if (parent) {
				System.out.println("Error: extra parent should not be in the extra");
				System.out.println("Use: chatlist");
				return null;
			}
			
			List<TextComponent> extra = new ChatListSerializer().serialize(file, section.getConfigurationSection("extra").getCurrentPath(), true);
			
			for (TextComponent entry : extra) {
				result.addExtra(entry);
			}
		}
		
		section = section.getParent();
		
		return result;
	}
}
