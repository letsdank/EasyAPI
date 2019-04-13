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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

public class Main extends JavaPlugin {
	
	public static final String VERSION = "0.1";
	
	private static final List<String> EAPI_COMMANDS = fromArray(new String[] {
			"options",
			"control"
	});
	
	private static final List<String> EAPI_OPTIONS = fromArray(new String[] {
			"version",
			"onlyop"
	});
	
	private static List<String> fromArray(String[] arr) {
		List<String> list = Lists.newArrayList(arr);
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		return list;
	}
	
	public static JavaPlugin instance;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		System.out.println("Initialized EasyAPI version " + VERSION);
	}
	
	public List<String> onTabCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		if (cmdName.equalsIgnoreCase("eapi")) {
			
			//
			// Кстати, забыл напомнить.
			// Здесь комментарии будут на русском, так что если найдутся умники,
			// которые смогут это перевести, то дерзайте!
			//
			
			switch (args.length) {
				case 1:
					return EAPI_COMMANDS;
				case 2:
					if (args[1].equalsIgnoreCase("options")) {
						return EAPI_OPTIONS;
					}
			}
		}

		return Collections.emptyList();
	}
}
