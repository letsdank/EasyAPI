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
package com.letsdank.easyapi.npc;

/**
 * 
 */
public enum EnumPlayerAnimation {
	ARM_SWING(1),
	HURT(2),
	EAT_FOOD(3),
	ARM_SWING_OFFHAND(4),
	CRIT(5),
	MAGIC_CRIT(6),
	
	SIT,
    SLEEP,
    SNEAK,
    START_USE_MAINHAND_ITEM,
    STOP_SITTING,
    STOP_SLEEPING,
    STOP_SNEAKING,
    STOP_USE_ITEM;
	
	private int code;
	
	EnumPlayerAnimation() {
		this(0);
	}
	
	EnumPlayerAnimation(int code) {
		this.code = code;
	}
	
	public boolean hasCode() {
		return code != 0;
	}
	
	/**
	 * @return the code
	 */
	public int getCode() {
		return code - 1;
	}
}
