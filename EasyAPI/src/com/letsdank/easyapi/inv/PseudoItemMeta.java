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

import java.util.List;

import org.bukkit.inventory.ItemStack;

/**
 * 
 */
public class PseudoItemMeta {

	private String name;
	private List<String> lore;
	
	public PseudoItemMeta(ItemStack stack) {
		if (stack.getItemMeta() != null) {
			name = stack.getItemMeta().getDisplayName();
			lore = stack.getItemMeta().getLore();
		}
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the lore
	 */
	public List<String> getLore() {
		return lore;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param lore the lore to set
	 */
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		PseudoItemMeta obj2 = (PseudoItemMeta) obj;
		
		if (this.name != null && this.lore != null &&
				obj2.name != null && obj2.lore != null) {
			return this.name.equals(obj2.name) &&
					this.lore.equals(obj2.lore);
		}
		
		return true;
	}
}
