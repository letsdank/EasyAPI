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
package com.letsdank.easyapi.chat;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.md_5.bungee.api.ChatColor;

/**
 * 
 */
public class TextComponent {
	
	private List<TextComponent> extra;
	
	private String text;
	private boolean bold;
	private boolean italic;
	private boolean underlined;
	private boolean strikethrough;
	private boolean obfuscated;
	private ChatColor color;
	
	//
	// Click events
	//
	
	private String insertion;
	private ChatClickEvent clickEvent;
	private ChatHoverEvent hoverEvent;
	
	public TextComponent(String text) {
		this.text = text;
		bold = false;
		italic = false;
		underlined = false;
		strikethrough = false;
		obfuscated = false;
		color = ChatColor.WHITE;
		
		extra = new ArrayList<TextComponent>();
	}
	
	/**
	 * @param clickEvent the clickEvent to set
	 */
	public void setClickEvent(ChatClickEvent clickEvent) {
		this.clickEvent = clickEvent;
	}
	
	public void setClickEvent(ChatClickEventType type, String str) {
		this.clickEvent = new ChatClickEvent(type, str);
	}
	
	 /**
	 * @param hoverEvent the hoverEvent to set
	 */
	public void setHoverEvent(ChatHoverEvent hoverEvent) {
		this.hoverEvent = hoverEvent;
	}
	
	public void setHoverEvent(ChatHoverEventType type, String str) {
		this.hoverEvent = new ChatHoverEvent(type, str);
	}
	
	/**
	 * @param color the color to set
	 */
	public void setColor(ChatColor color) {
		this.color = color;
	}
	
	/**
	 * @param bold the bold to set
	 */
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	/**
	 * @param extra the extra to set
	 */
	public void addExtra(TextComponent extra) {
		this.extra.add(extra);
	}
	
	/**
	 * @param insertion the insertion to set
	 */
	public void setInsertion(String insertion) {
		this.insertion = insertion;
	}
	
	/**
	 * @param italic the italic to set
	 */
	public void setItalic(boolean italic) {
		this.italic = italic;
	}
	
	/**
	 * @param obfuscated the obfuscated to set
	 */
	public void setObfuscated(boolean obfuscated) {
		this.obfuscated = obfuscated;
	}
	
	/**
	 * @param strikethrough the strikethrough to set
	 */
	public void setStrikethrough(boolean strikethrough) {
		this.strikethrough = strikethrough;
	}
	
	/**
	 * @param underlined the underlined to set
	 */
	public void setUnderlined(boolean underlined) {
		this.underlined = underlined;
	}
	
	public String toJSONmessage() {
		return toJSON(false).toJSONString();
	}
	
	public JSONObject toJSON() {
		return toJSON(false);
	}
	
	private JSONObject toJSON(boolean isParent) {
		JSONObject obj = new JSONObject();
		
		obj.put("text", text);
		obj.put("bold", bold);
		obj.put("italic", italic);
		obj.put("underlined", underlined);
		obj.put("strikethrough", strikethrough);
		obj.put("obfuscated", obfuscated);
		if (color != null)
			obj.put("color", color.getName());
		if (insertion != null)
			obj.put("insertion", insertion);
		
		if (clickEvent != null) {
			JSONObject clickObj = new JSONObject();
			clickObj.put("action", clickEvent.getType().name().toLowerCase());
			clickObj.put("value", clickEvent.getContent());
			
			obj.put("clickEvent", clickObj);
		}
		
		if (hoverEvent != null) {
			JSONObject hoverObj = new JSONObject();
			hoverObj.put("action", hoverEvent.getType().name().toLowerCase());
			hoverObj.put("value", hoverEvent.getContent());
			
			obj.put("hoverEvent", hoverObj);
		}
		
		if (!extra.isEmpty()) {
			JSONArray arr = new JSONArray();
			
			if (!isParent) {
				for (TextComponent component : extra) {
					arr.add(component.toJSON(true));
				}
			}
		}
		
		return obj;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("TextComponent[text=");
		builder.append(text);
		builder.append(", bold=\"");
		builder.append(bold);
		builder.append("\", italic=");
		builder.append(italic);
		builder.append(", underlined=");
		builder.append(underlined);
		builder.append(", strikethrough=");
		builder.append(strikethrough);
		builder.append(", obfuscated=");
		builder.append(obfuscated);
		builder.append(", color=");
		builder.append(color.getName());
		builder.append(", insertion=");
		builder.append(insertion);
		if (clickEvent != null) {
			builder.append(",\n clickEvent=");
			builder.append(clickEvent.toString());
		}
		
		if (hoverEvent != null) {
			builder.append(",\n hoverEvent=");
			builder.append(hoverEvent.toString());
		}
		
		if (extra != null) {
			builder.append(",\n extra=");
			builder.append(extra.toString());
		}
		builder.append("]");
		
		return builder.toString();
	}
}
