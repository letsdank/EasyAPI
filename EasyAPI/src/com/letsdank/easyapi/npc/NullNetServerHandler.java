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

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class NullNetServerHandler extends PlayerConnection
{
	public NullNetServerHandler(MinecraftServer minecraftserver, NetworkManager inetworkmanager, EntityPlayer entityplayer)
	{
		super(minecraftserver, inetworkmanager, entityplayer);
	}

	@Override
    public void a(PacketPlayInWindowClick packet) {
    }

    @Override
    public void a(PacketPlayInTransaction packet) {
    }

    @Override
    public void a(PacketPlayInFlying packet) {
    }

    @Override
    public void a(PacketPlayInUpdateSign packet) {
    }

    @Override
    public void a(PacketPlayInBlockDig packet) {
    }

    @Override
    public void a(PacketPlayInBlockPlace packet) {
    }

	@Override
	public void disconnect(String s)
	{
	}

	@Override
	public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot)
	{
	}

	@Override
	public void a(PacketPlayInChat packetplayinchat)
	{
	}

    @SuppressWarnings("rawtypes")
	@Override
    public void sendPacket(Packet packet) {
    }
}