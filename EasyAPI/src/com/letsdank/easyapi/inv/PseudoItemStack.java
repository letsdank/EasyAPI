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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * 
 */
public class PseudoItemStack {
	
	private int amount;
	private Material material;
	private PseudoItemMeta meta;
	
	public PseudoItemStack(ItemStack stack) {
		amount = stack.getAmount();
		material = stack.getType();
		meta = new PseudoItemMeta(stack);
	}
	
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * @return the material
	 */
	public Material getType() {
		return material;
	}
	
	/**
	 * @param material the material to set
	 */
	public void setType(Material material) {
		this.material = material;
	}
	
	/**
	 * @return the meta
	 */
	public PseudoItemMeta getItemMeta() {
		return meta;
	}
	
	/**
	 * @param meta the meta to set
	 */
	public void setItemMeta(PseudoItemMeta meta) {
		this.meta = meta;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		PseudoItemStack obj2 = (PseudoItemStack) obj;
		
		if (this.amount != obj2.amount ||
			!this.material.equals(obj2.material) ||
			!this.meta.equals(obj2.meta)) {
			return false;
		}
		
		return true;
	}
}
