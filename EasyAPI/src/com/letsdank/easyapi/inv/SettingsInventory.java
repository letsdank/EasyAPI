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
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.letsdank.easyapi.main.Main;
import com.letsdank.easyapi.main.PluginLogger;

/**
 * 
 */
public class SettingsInventory {
	private List<NumerableItemStack> buttons;
	private Inventory inv;
	
	public SettingsInventory() {
		buttons = new ArrayList<NumerableItemStack>();
		inv = Bukkit.createInventory(null, 18);
		
		setButtons();
		reload();
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onClick(InventoryClickEvent e) {
				if (e.getInventory() == null) return;
				if (e.getCurrentItem() == null) return;
				
				if (!e.getCurrentItem().hasItemMeta()) return;
				if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;
				
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Info logging")) {
					PluginLogger.infoLogging = !PluginLogger.infoLogging;
					setButtons();
					reload();
					e.setCancelled(true);
					return;
				}
			}
		}, Main.getInstance());
	}
	
	private void reload() {
		for (NumerableItemStack stack : buttons) {
			inv.setItem(stack.index, stack.base);
		}
	}

	private void setButtons() {
		boolean infoLogging = PluginLogger.infoLogging;
		
		buttons.add(new NumerableItemStack(0, new ItemStackBuilder(
				infoLogging ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK)
				.withName((infoLogging ? "&a" : "&c") + "Info logging")
				.withLore("&7You can inspect the loading of all templates on the console",
						"&7Click to " + (infoLogging ? "disable" : "enable")).build()
				));
	}
	
	/**
	 * @return the inv
	 */
	public Inventory getInv() {
		return inv;
	}
}
