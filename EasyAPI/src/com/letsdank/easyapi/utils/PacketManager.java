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
package com.letsdank.easyapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.PlayerInteractManager;
import net.minecraft.server.v1_8_R1.WorldServer;

/**
 * 
 */
public class PacketManager {
	
	public static EntityPlayer spawnPlayer(String playerSkin, Location loc) {
		
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
		
		EntityPlayer npc = new EntityPlayer(server, nmsWorld, getGameProfileWithTexture(playerSkin), new PlayerInteractManager(nmsWorld));
		
		npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		return npc;
	}
	
	private static GameProfile getGameProfileWithTexture(String playerSkin) {
		GameProfile profile = new GameProfile(MojangAPI.getUUIDFromName(playerSkin), playerSkin);
		
		//
		// Parsing JSON file
		//
		
		JSONObject obj = MojangAPI.getSession(playerSkin);
		JSONArray arr = (JSONArray) obj.get("properties");
		String texture = (String) ((JSONObject)arr.get(0)).get("value");
		String signature = (String) ((JSONObject)arr.get(0)).get("signature");
		
		profile.getProperties().put("textures", new Property("textures", texture, signature));
		
		return profile;
	}
}
