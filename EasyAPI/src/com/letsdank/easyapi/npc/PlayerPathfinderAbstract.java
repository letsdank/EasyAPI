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
import net.minecraft.server.v1_8_R3.IBlockAccess;
import net.minecraft.server.v1_8_R3.IntHashMap;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PathPoint;
import net.minecraft.server.v1_8_R3.PathfinderAbstract;

/**
 * 
 */
public abstract class PlayerPathfinderAbstract extends PathfinderAbstract {
	protected IBlockAccess a;
    protected IntHashMap<PathPoint> b = new IntHashMap<PathPoint>();
    protected int c;
    protected int d;
    protected int e;
    
    @Override
    public void a() {
    }

    @Override
    public abstract PathPoint a(Entity paramEntity);

    @Override
    public abstract PathPoint a(Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3);

    @Override
    public void a(IBlockAccess paramIBlockAccess, Entity paramEntity) {
        a = paramIBlockAccess;
        b.c();

        c = MathHelper.d(paramEntity.width + 1.0F);
        d = MathHelper.d(paramEntity.length + 1.0F);
        e = MathHelper.d(paramEntity.width + 1.0F);
    }

    @Override
    protected PathPoint a(int x, int y, int z) {
        int i = PathPoint.a(x, y, z);
        PathPoint localPathPoint = b.get(i);
        if (localPathPoint == null) {
            localPathPoint = new PathPoint(x, y, z);
            b.a(i, localPathPoint);
        }
        return localPathPoint;
    }

    @Override
    public abstract int a(PathPoint[] paramArrayOfPathPoint, Entity paramEntity, PathPoint paramPathPoint1,
            PathPoint paramPathPoint2, float paramFloat);
}
