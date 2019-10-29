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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.letsdank.easyapi.main.PluginLogger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

/**
 * 
 */
public class PacketInjector {
	
	private static List<Player> players = new ArrayList<Player>();
	
	public static void injectPlayer(Player p) {
		
		if (players.contains(p)) {
			PluginLogger.error("Error: Player %s is already injecting", p.getName());
			return;
		}
		players.add(p);
		
		ChannelDuplexHandler handler = new ChannelDuplexHandler() {
			
			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
				System.out.println("[PACKET] READ: " + packetToString(msg));
				super.channelRead(ctx, msg);
			}
		};
		NetworkManager networkManager = ((CraftPlayer) p).getHandle().playerConnection.networkManager;
		Channel packet = (Channel) NMSClass.getDeclaredField(networkManager, "i");
		ChannelPipeline pipe = packet.pipeline();
		
		if (pipe == null) {
			PluginLogger.error("Could not inject player %s", p.getName());
			return;
		}
		pipe.addBefore("packet_handler", p.getName(), handler);
	}
	
	public static void stopInjecting(Player p) {
		NetworkManager networkManager = ((CraftPlayer) p).getHandle().playerConnection.networkManager;
		Channel channel = (Channel) NMSClass.getDeclaredField(networkManager, "i");
		
		channel.eventLoop().submit(() -> {
			channel.pipeline().remove(p.getName());
			return null;
		});
		
		players.remove(p);
	}
	
	private static String packetToString(Object obj) {
		StringBuilder builder = new StringBuilder();
		String className = obj.getClass().getSimpleName();
		builder.append(className);
		
		switch (className) {
			case "PacketPlayInPosition":
			case "PacketPlayInFlying":
				PacketPlayInFlying pos = (PacketPlayInFlying) obj;
				builder.append("[x = ");
				builder.append(pos.a());
				builder.append(", y = ");
				builder.append(pos.b());
				builder.append(", z = ");
				builder.append(pos.c());
				builder.append(", yaw = ");
				builder.append(pos.d());
				builder.append(", pitch = ");
				builder.append(pos.e());
				builder.append(", f = "); // ?
				builder.append(pos.f());
				builder.append(", hasPos = ");
				builder.append(pos.g());
				builder.append(", hasLook = ");
				builder.append(pos.h());
				break;
			default:
				break;
		}
		
		return builder.toString();
	}
}
