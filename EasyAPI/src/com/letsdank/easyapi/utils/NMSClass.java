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
package com.letsdank.easyapi.utils;

import org.bukkit.Bukkit;

import com.letsdank.easyapi.main.PluginLogger;

/**
 * 
 */
public class NMSClass {
	
	private static final String PACKAGE_NMS = "net.minecraft.server.";
	private static final String PACKAGE_CB = "org.bukkit.craftbukkit.";
	
	public static Class<?> getNMSClass(String name) {
		// thanks Skionz
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		return getClass(PACKAGE_NMS + version + name);
		
	}
	
	public static Class<?> getCBClass(String name, String parent) {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		if (parent != null)
			return getClass(PACKAGE_CB + version + name + parent);
		else
			return getClass(PACKAGE_CB + version + name);
	}
	
	private static Class<?> getClass(String name) {
		try {
			Class<?> clazz = Class.forName(name);
			return clazz;
		} catch (ClassNotFoundException e) {
			PluginLogger.error("Could not find class %s. Is it actually minecraft server?", name);
			return null;
		}
	}
}
