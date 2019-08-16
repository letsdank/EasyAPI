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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * 
 */
public class CustomCraft {
	
	//
	// Recipe structure took from Minecraft Forge JSON example.
	//
	
	private String id;
	private CustomCraftType type;
	private List<String> pattern;
	private Map<String, ItemStack> keys;
	private ItemStack result;
	
	public CustomCraft(String id, CustomCraftType type, List<String> pattern, Map<String, ItemStack> keys, ItemStack result) {
		this.type = type;
		this.pattern = pattern;
		this.keys = keys;
		this.result = result;
		this.id = id;
		
		checkForAir();
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
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		
		builder.append("CustomCraft[Type = ");
		builder.append(type.name());
		builder.append(", Pattern: [");
		for (String p : pattern) {
			if (first) first = false;
			else builder.append(", ");
			builder.append(p);
		}
		builder.append("], Keys: [");
		first = true;
		for (Map.Entry<String, ItemStack> entry : keys.entrySet()) {
			if (first) first = false;
			else builder.append("], ");
			builder.append("[");
			builder.append(entry.getKey());
			builder.append(", ");
			builder.append(entry.getValue().toString());
		}
		
		builder.append(", Result: ");
		builder.append(result.toString());
		builder.append("]");
		
		return builder.toString();
	}

	/**
	 * @return
	 */
	public ItemStack[] getMatrix() {
		ItemStack[] result = new ItemStack[9];
		int index = 0;
		for (Map.Entry<String, ItemStack> entry : keys.entrySet()) {
			for (int i = 0; i < pattern.size(); i++) {
				String p = pattern.get(i);
				for (int j = 0; j < p.toCharArray().length; j++) {
					if (p.substring(j, j + 1).equals(entry.getKey())) {
						result[index] = entry.getValue();
					}
					index++;
				}
			}
			
			index = 0;
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	private void checkForAir() {
		for (Map.Entry<String, ItemStack> stack : keys.entrySet()) {
			if (stack.getValue().getType() == Material.AIR) {
				stack.getValue().setAmount(0);
			}
		}
	}
}
