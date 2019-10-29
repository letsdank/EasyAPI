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

import java.lang.reflect.Field;
import java.net.SocketAddress;

import com.letsdank.easyapi.utils.NMSClass;

import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.NetworkManager;

/**
 * 
 */
public class NullNetworkManager extends NetworkManager {

	/**
	 * @param enumprotocoldirection
	 */
	public NullNetworkManager(EnumProtocolDirection enumprotocoldirection) {
		super(enumprotocoldirection);
		
		if (NETWORK_ADDRESS == null)
            return;
        try {
            this.channel = new NullChannel(null);
            NETWORK_ADDRESS.set(this, new SocketAddress() {
                private static final long serialVersionUID = 8207338859896320185L;
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
	}
	
	public boolean g() {
		return true;
	}
	
	public static Field NETWORK_ADDRESS = NMSClass.getField(NetworkManager.class, "l");
}
