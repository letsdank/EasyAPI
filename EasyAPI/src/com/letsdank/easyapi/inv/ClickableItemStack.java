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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

/**
 * 
 */
public class ClickableItemStack {
	private String id;
	private List<Runnable> action;
	private ItemStack base;
	
	public ClickableItemStack(String id, ItemStack base, Runnable action) {
		this.id = id;
		this.base = base;
		this.action = new ArrayList<Runnable>();
		this.action.add(action);
	}
	
	public ClickableItemStack(String id, ItemStack base, Runnable... action) {
		this(id, base, Lists.newArrayList(action));
	}
	
	public ClickableItemStack(String id, ItemStack base, List<Runnable> action) {
		this.id = id;
		this.base = base;
		this.action = action;
	}
	
	/**
	 * @return the action
	 */
	public List<Runnable> getAction() {
		if (action == null) System.out.println("idk y action is null ;(");
		return action;
	}
	
	/**
	 * @return the base
	 */
	public ItemStack getBase() {
		return base;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	void setBase(ItemStack stack) {
		base = stack;
	}
}
