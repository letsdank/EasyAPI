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

/**
 * 
 */
public class ChatHoverEvent {
	private ChatHoverEventType type;
	private String content;
	
	public ChatHoverEvent(ChatHoverEventType type, String str) {
		this.content = str;
		this.type = type;
	}
	
	/**
	 * @return the type
	 */
	public ChatHoverEventType getType() {
		return type;
	}
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChatHoverEvent[action=");
		builder.append(type.name());
		builder.append(", value=");
		builder.append(content);
		builder.append("]");
		
		return builder.toString();
	}
}
