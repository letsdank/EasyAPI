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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.letsdank.easyapi.main.Main;

/**
 * 
 */
public class ClickableInventory {

	private String id;
	private List<NumerableItemStack> backupInv;
	private Inventory inv;
	private List<ClickableItemStack> buttons;
	
	public ClickableInventory(Inventory inv, String id) {
		this.id = id;
		this.inv = inv;
		backupInv = new ArrayList<NumerableItemStack>();
		for (int k = 0; k < inv.getSize(); k++) {
			if (inv.getItem(k) == null) continue;
			backupInv.add(new NumerableItemStack(k, inv.getItem(k).clone()));
		}
		
		buttons = new ArrayList<ClickableItemStack>();
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onClick(InventoryClickEvent e) {
				if (e.getClickedInventory() == null) return;
				
				if (e.getClickedInventory().equals(inv)) {
					for (ClickableItemStack item : buttons) {
						if (item.getBase().equals(e.getCurrentItem())) {
							for (Runnable run : item.getAction()) {
								run.run();
							}
							e.setCancelled(true);
						}
					}
				}
			}
			
			@EventHandler
			public void onClose(InventoryCloseEvent e) {
				if (e.getInventory().equals(inv)) {
					setNewInventory();
				}
			}
		}, Main.getInstance());
	}
	
	private void setNewInventory() {
		for (NumerableItemStack stack : backupInv) {
			ItemStack item = inv.getItem(stack.index);
			
			item.setAmount(stack.amount);
			item.setType(stack.material);
			ItemMeta meta = item.getItemMeta();
			
			meta.setDisplayName(stack.name);
			meta.setLore(stack.lore);
			
			item.setItemMeta(meta);
		}
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
		updateInv(index, button.getBase().clone());
	}
	
	public void removeButton(ClickableItemStack button) {
		inv.remove(button.getBase());
		buttons.remove(button);
		removeButton(button.getBase());
	}
	
	/**
	 * @param base
	 */
	private void removeButton(ItemStack base) {
		for (NumerableItemStack inv : backupInv) {
			if (stackEquals(base, inv.material, inv.amount, inv.name, inv.lore)) {
				backupInv.remove(inv);
			}
		}
	}

	private boolean stackEquals(ItemStack base, Material material, int amount, String name, List<String> lore) {
		if (base.getAmount() == amount &&
			base.getType().equals(material) &&
			base.getItemMeta().getDisplayName().equals(name) &&
			base.getItemMeta().getLore().equals(lore)) {
			return true;
		}
		
		return false;
	}

	/**
	 * @return the name
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return The base inventory.
	 */
	public Inventory getInventory() {
		return inv;
	}
	
	private void updateInv(int index, ItemStack button) {
		backupInv.add(new NumerableItemStack(index, button));
	}
}
