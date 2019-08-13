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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

/**
 * 
 */
public class ItemStackBuilder {
	
	private Material material;
	
	private int amount;
	private String name;
	private String localizedName;
	private List<String> lore;
	
	private Map<Enchantment, Integer> enchantments;
	
	public ItemStackBuilder(Material material) {
		this.material = material;
		
		enchantments = new HashMap<Enchantment, Integer>();
	}
	
	public ItemStackBuilder withAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public ItemStackBuilder withLocalizedName(String localizedName) {
		this.localizedName = localizedName;
		return this;
	}
	
	public ItemStackBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public ItemStackBuilder withLore(String... lore) {
		this.lore = Lists.newArrayList(lore);
		return this;
	}
	
	public ItemStackBuilder withLore(List<String> lore) {
		this.lore = lore;
		return this;
	}
	
	public ItemStackBuilder withEnchantment(Enchantment ench, int level) {
		enchantments.put(ench, level);
		return this;
	}
	
	public ItemStack build() {
		ItemStack stack = new ItemStack(material);
		
		if (amount != 0) stack.setAmount(amount);
		if (!enchantments.isEmpty()) stack.addEnchantments(enchantments);
		
		ItemMeta meta = stack.getItemMeta();
		
		if (name != null) meta.setDisplayName(name);
		if (localizedName != null) meta.setLocalizedName(localizedName);
		if (lore != null) meta.setLore(lore);
		
		stack.setItemMeta(meta);
		return stack;
	}
}
