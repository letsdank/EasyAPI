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

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.MathHelper;

/**
 * 
 */
public class PlayerControllerLook {
	private final EntityBot a;
    private float b;
    private float c;
    private boolean d;
    private double e;
    private double f;
    private double g;
    
	public PlayerControllerLook(EntityBot bot) {
		a = bot;
	}
	
	public void a() {
		if (!a.getNavigation().m())
			return;
		a.pitch = 0.0F;
		a.aI = a.aK;
		if (d) {
			d = false;
			
			double d1 = e - a.locX;
			double d2 = f - (a.locY + a.getHeadHeight());
			double d3 = g - a.locZ;
			double d4 = MathHelper.sqrt(d1 * d1 + d3 * d3);
			
			float f1 = (float) (MathHelper.b(d3, d1) * 57.2957763671875D) - 90.0F;
			float f2 = (float) -(MathHelper.b(d2, d4) * 57.2957763671875D);
			a.pitch = a(a.pitch, f2, c);
			a.aK = a(a.aK, f1, b);
			a.yaw = a.aK;
			while (a.aK >= 180.0F) {
				a.aK -= 360F;
			}
			while (a.aK < -180.0F) {
				a.aK += 360F;
			}
		} else {
			// a.aK = a(a.aK, a.aI, 10.0F);
		}
		
		float f3 = MathHelper.g(a.aK - a.aI);
		if (!a.getNavigation().m()) {
			if (f3 < -75.0F) {
				a.aK = (a.aI - 75.0F);
			}
			if (f3 > 75.0F) {
				a.aK = (a.aI + 75.0F);
			}
		}
	}
	
	public void a(double d0, double d1, double d2, float f0, float f1) {
        e = d0;
        f = d1;
        g = d2;
        b = f0;
        c = f1;
        d = true;
    }
	
	public void a(Entity entity, float f0, float f1) {
        e = entity.locX;
        if ((entity instanceof EntityLiving))
            f = (entity.locY + entity.getHeadHeight());
        else {
            f = ((entity.getBoundingBox().b + entity.getBoundingBox().e) / 2.0D);
        }

        g = entity.locZ;
        b = f0;
        c = f1;
        d = true;
    }
	
	private float a(float f0, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f0);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f0 + f3;
    }
	
	public boolean b() {
        return this.d;
    }

    public double e() {
        return this.e;
    }

    public double f() {
        return this.f;
    }

    public double g() {
        return this.g;
    }
}
