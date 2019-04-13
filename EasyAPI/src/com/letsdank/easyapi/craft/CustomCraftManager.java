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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

/**
 * 
 */
public class CustomCraftManager {
					 /* id */
	public static Map<String, CraftRecipe> customCrafts = new HashMap<String, CraftRecipe>();
	
	public static CraftRecipe getCraftRecipe(String id) {
		for (Map.Entry<String, CraftRecipe> entry : customCrafts.entrySet()) {
			if (id.equalsIgnoreCase(entry.getKey())) {
				return entry.getValue();
			}
		}
		
		return null;
	}
	
	public static void addCraftRecipe(String newId, CraftRecipe recipe) {
		
		//
		// Проверка. Если рецепта с таким же идентификатором не существует, то пропускаем.
		//
		
		for (Map.Entry<String, CraftRecipe> entry : customCrafts.entrySet()) {
			if (newId.equalsIgnoreCase(entry.getKey())) {
				System.err.println("ERROR: Recipe " + newId + " is already exists!");
				return;
			}
		}
		
		customCrafts.put(newId, recipe);
	}
	
	public class CraftRecipe {
		private String firstRow;
		private String secondRow;
		private String thirdRow;
		
		private List<ItemStack> stacks;
		private ItemStack result;
		
		/**
		 * Creates a new recipe for custom craft.
		 * <p>
		 * Example:
		 * {@code "AAA"}, {@code"BBB"}, {@code"AAA"}, {@code ItemStack.DIAMOND_BLOCK},
		 * {@code ItemStack.DIAMOND}, {@code ItemStack.DIRT}
		 * <p>
		 * creates a new Dirt recipe (old recipe are not replacing) with:
		 * 
		 * <p> AAA BBB AAA -> Dirt (where A is Diamond Block, B is Diamond)
		 * 
		 * @param firstRow The first row.
		 * @param secondRow The second row.
		 * @param thirdRow The third row.
		 * @param stacks Stacks for creating a recipe. Last {@code ItemStack} is result of this
		 * 		  		 recipe.
		 */
		public CraftRecipe(String firstRow, String secondRow, String thirdRow, ItemStack... stacks) {
			List<ItemStack> l = Lists.newArrayList(stacks);
			result = l.remove(l.size() - 1);
			
			this.stacks = l;
			this.firstRow = firstRow;
			this.secondRow = secondRow;
			this.thirdRow = thirdRow;
		}
		
		/**
		 * @return the firstRow
		 */
		public String getFirstRow() {
			return firstRow;
		}
		
		/**
		 * @return the secondRow
		 */
		public String getSecondRow() {
			return secondRow;
		}
		
		/**
		 * @return the thirdRow
		 */
		public String getThirdRow() {
			return thirdRow;
		}
		
		public String[] getRows() {
			return new String[] {firstRow, secondRow, thirdRow};
		}
		
		/**
		 * @return the stacks
		 */
		public List<ItemStack> getStacks() {
			return stacks;
		}
		
		/**
		 * @return the result
		 */
		public ItemStack getResult() {
			return result;
		}
	}
}
