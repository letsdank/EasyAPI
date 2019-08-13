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

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.letsdank.easyapi.main.Main;

/**
 * 
 */
public class ClickableInventory {
	private Inventory inv;
	private List<ClickableItemStack> buttons;
	
	public ClickableInventory(Inventory inv) {
		this.inv = inv;
		buttons = new ArrayList<ClickableItemStack>();
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onClick(InventoryClickEvent e) {
				if (e.getClickedInventory().equals(inv)) {
					for (ClickableItemStack item : buttons) {
						if (item.getBase().equals(e.getCurrentItem())) {
							item.getAction().run();
						}
					}
				}
			}
		}, Main.getInstance());
	}
	
	/**
	 * Adds button to the inventory.
	 * 
	 * @param button ItemStack. Will represent like button.
	 * @param index Index to place button.
	 */
	public void addButton(ClickableItemStack button, int index) {
		inv.setItem(index, button.getBase());
		buttons.add(button);
	}
	
	public void removeButton(ClickableItemStack button) {
		inv.remove(button.getBase());
	}
}
