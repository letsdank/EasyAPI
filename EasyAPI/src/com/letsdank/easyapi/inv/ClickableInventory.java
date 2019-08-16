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
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.letsdank.easyapi.main.Main;

/**
 * 
 */
public class ClickableInventory {
	private String id;
	private Inventory inv;
	private List<ClickableItemStack> buttons;
	
	public ClickableInventory(Inventory inv, String id) {
		this.id = id;
		this.inv = inv;
		buttons = new ArrayList<ClickableItemStack>();
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onClick(InventoryClickEvent e) {
				if (e.getClickedInventory() == null) return;
				
				if (e.getClickedInventory().equals(inv)) {
					for (ClickableItemStack item : buttons) {
						if (stacksEquals(e.getCurrentItem(), item.getBase())) {
							for (Runnable run : item.getAction()) {
								run.run();
							}
							updateInventory((Player)e.getWhoClicked(), e.getCurrentItem());
							e.setCancelled(true);
						} else {
							e.getWhoClicked().sendMessage(":(");
						}
					}
				}
			}
		}, Main.getInstance());
	}
	
	/**
	 * 
	 */
	private void updateInventory(Player p, ItemStack button) {
		for (ClickableItemStack stack : buttons) {
			if (stack.getBase().equals(button)) {
				stack.setBase(button.clone());
				System.out.println(button.toString());
			}
		}
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (HumanEntity he : inv.getViewers()) {
					((Player)he).updateInventory();
				}
			}
		}.runTaskLater(Main.getInstance(), 1L);
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
		buttons.remove(button);
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
	
	private boolean stacksEquals(ItemStack o1, ItemStack o2) {
		if (o1.getType() != o2.getType()) {
			System.out.println("type not equal");
			return false;
		}
		if (o1.getAmount() != o2.getAmount()) {
			System.out.println("amount not equal");
			return false;
		}
		return metaEquals(o1.getItemMeta(), o2.getItemMeta());
	}

	private boolean metaEquals(ItemMeta o1, ItemMeta o2) {
		if (o1.getDisplayName() != o2.getDisplayName()) {
			System.out.println("name not equal");
			return false;
		}
		if (o1.getLore() != o2.getLore()) {
			System.out.println("lore not equal");
			return false;
		}
		// if (o1.getLocalizedName() != o2.getLocalizedName()) {
		//	return false;
		// }
		
		return true;
	}
}
