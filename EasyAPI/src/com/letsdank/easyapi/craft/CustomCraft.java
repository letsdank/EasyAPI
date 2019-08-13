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

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

/**
 * 
 */
public class CustomCraft {
	
	//
	// Recipe structure took from Minecraft Forge JSON example.
	//
	
	
	private CustomCraftType type;
	private List<String> pattern;
	private Map<String, ItemStack> keys;
	private ItemStack result;
	
	public CustomCraft(CustomCraftType type, List<String> pattern, Map<String, ItemStack> keys, ItemStack result) {
		this.type = type;
		this.pattern = pattern;
		this.keys = keys;
		this.result = result;
	}
	
	/**
	 * @return the keys
	 */
	public Map<String, ItemStack> getKeys() {
		return keys;
	}
	
	/**
	 * @return the pattern
	 */
	public List<String> getPattern() {
		return pattern;
	}
	
	/**
	 * @return the result
	 */
	public ItemStack getResult() {
		return result;
	}
	
	/**
	 * @return the type
	 */
	public CustomCraftType getType() {
		return type;
	}
}
