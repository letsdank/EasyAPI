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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.letsdank.easyapi.craft.CustomCraft;
import com.letsdank.easyapi.craft.CustomCraftListSerializer;

/**
 * 
 */
public class Main extends JavaPlugin {
	
	public List<CustomCraft> customCrafts;
	
	@Override
	public void onEnable() {
		customCrafts = new ArrayList<CustomCraft>();
		
		//
		// Plugin Initialization
		//
		
		File craftPath = new File(getDataFolder(), "recipes");
		if (!craftPath.exists()) craftPath.mkdirs();
		
		for (File file : craftPath.listFiles()) {
			customCrafts.addAll(new CustomCraftListSerializer().serialize(file, null));
		}
	}
}
