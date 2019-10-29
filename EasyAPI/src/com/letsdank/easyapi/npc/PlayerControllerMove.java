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

import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MathHelper;

/**
 * 
 */
public class PlayerControllerMove extends ControllerMove {
	protected EntityLiving a;
    protected double b;
    protected double c;
    protected double d;
    protected double e;
    protected boolean f;
    
	public PlayerControllerMove(EntityBot bot) {
		super(bot.insentient);
		a = bot;
		b = bot.locX;
		c = bot.locZ;
	}
	
	public boolean a() {
		return f;
	}
	
	public void a(double d0, double d1, double d2, double d3) {
		b = d0;
		c = d1;
		d = d2;
		e = d3;
		f = true;
	}
	
	protected float a(float f0, float f1, float f2) {
		float f3 = MathHelper.g(f1 - 1);
		
		if (f3 > f2)
			f3 = f2;
		
		if (f3 < -f2)
			f3 = -f2;
		
		float f4 = f0 + f3;
		
		if (f4 < 0.0F)
			f4 += 360.0F;
		else if (f4 > 360.0F)
			f4 -= 360.0F;
		
		return f4;
	}
	
	public double b() {
		return e;
	}
	
	public void c() {
		a.ba = 0F;
		if (f) {
			f = false;
			int i = MathHelper.floor(a.getBoundingBox().b + 0.5D);
			double d0 = b - a.locX;
			double d1 = d - a.locZ;
			double d2 = c - i;
			double d3 = d0 * d0 + d2 * d2 + d1 * d1;
			if (d3 < 2.500000277905201E-007D)
				return;
			
			float f = (float) Math.toDegrees(Math.atan2(d1, d0)) - 90.0F;
			a.yaw = a(a.yaw, f, 90.0F);
			setHeadYaw(a, a.yaw);
			
			AttributeInstance speed = a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
			speed.setValue(0.1D * e);
			float movement = (float) (e * speed.getValue()) * 10;
			a.k(movement);
			a.ba = movement;
			if ((d2 > 0.0D) && (d0 * d0 + d1 * d1 < 1.0D)) {
				((EntityBot) a).getControllerJump().a();
			}
		}
	}
	
    public double d() {
        return this.b;
    }

    public double e() {
        return this.c;
    }
    public double f() {
        return this.d;
    }

	private void setHeadYaw(EntityLiving entity, float yaw) {
		yaw = clampYaw(yaw);
		entity.aJ = yaw;
		
		entity.aK = yaw;
	}
	
	private float clampYaw(float yaw) {
        while (yaw < -180.0F) {
            yaw += 360.0F;
        }

        while (yaw >= 180.0F) {
            yaw -= 360.0F;
        }
        return yaw;
    }
}
