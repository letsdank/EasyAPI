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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.letsdank.easyapi.main.PluginLogger;

/**
 * 
 */
public class MojangAPI {
	
	public static UUID getUUIDFromName(String playerName) {
		String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
		String json = readWebFile(url);
		if (json == null || json == "") {
			throw new IllegalArgumentException("Cannot find player with name " + playerName);
		}
		UUID result = null;
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(json);
			result = toUUID((String) obj.get("id"));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		PluginLogger.success("Got the UUID from player %s", playerName);
		
		return result;
	}

	private static UUID toUUID(String string) {
		return UUID.fromString(string.replaceAll(
				"([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", 
				"$1-$2-$3-$4-$5"));
	}

	private static String readWebFile(String url) {
		try {
			URL urll = new URL(url);
			String result = IOUtils.toString(urll.openStream(), StandardCharsets.UTF_8);
			return result;
		} catch (IOException e) {
			PluginLogger.error("Cannot find file %s. \n "
					+ "If it's web file, check your internet connection", url);
			throw new RuntimeException(e);
		}
	}
	
	public static JSONObject getSession(String playerName) {
		String playerUUID = getUUIDFromName(playerName).toString().replace("-", "");
		String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + playerUUID + "?unsigned=false";
		String json = readWebFile(url);
		
		try {
			return (JSONObject) new JSONParser().parse(json);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} finally {
			PluginLogger.success("Got the session from player %s", playerName);
		}
	}
}
