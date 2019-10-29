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

import com.letsdank.easyapi.utils.NMSClass;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.IBlockAccess;
import net.minecraft.server.v1_8_R3.Path;
import net.minecraft.server.v1_8_R3.PathEntity;
import net.minecraft.server.v1_8_R3.PathPoint;

/**
 * 
 */
public class PlayerPathfinder {
	private final Path a = new Path();
	private final PathPoint[] b = new PathPoint[32];
	private final PlayerPathfinderNormal c;
	
	public PlayerPathfinder(PlayerPathfinderNormal path) {
		c = path;
	}
	
	private PathEntity a(Entity entity, PathPoint point1, PathPoint point2, float f) {
		float newF = 0.0F;
		try {
			E.set(point1, 0.0F);
			newF = point1.b(point2);
			F.set(point1, newF);
			G.set(point1, newF);
		} catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
		
		a.a();
		a.a(point1);
		
		Object obj = point1;
		while (!a.e()) {
			PathPoint point3 = a.c();
			if (point1.equals(point2)) {
				return a(point1, point2);
			}
			if (point1.b(point2) < ((PathPoint) obj).b(point2)) {
				obj = point1;
			}
			point1.i = true;
			
			int i = c.a(b, entity, point1, point2, f);
			for (int j = 0; j < i; j++) {
				PathPoint point4 = b[j];
				try {
					float e = E.getFloat(obj);
					float ff = e + point3.b(point4);
					if ((ff < f * 2.0F) && ((!point4.a()) || (ff < e))) {
						H.set(point4, point3);
						E.set(point4, ff);
						newF = point4.b(point2);
						F.set(point4, newF);
						if (point4.a()) {
							a.a(point4, ff + newF);
						} else {
							G.set(point4, ff + newF);
							a.a(point4);
						}
					}
				} catch (IllegalArgumentException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
			}
		}
		if (obj == point1) {
			return null;
		}
		
		return a(point1, (PathPoint) obj);
	}
	
	public PathEntity a(IBlockAccess block, Entity entity, BlockPosition position, float f) {
		return a(block, entity, position.getX() + 0.5F, position.getY() + 0.5F, position.getZ() + 0.5F, f);
	}
	
	private PathEntity a(IBlockAccess block, Entity entity, double x, double y, double z, float f) {
		a.a();
		c.a(block, entity);
		PathPoint point1 = c.a(entity);
		PathPoint point2 = c.a(entity, x, y, z);
		
		PathEntity pathEntity = a(entity, point1, point2, f);
		
		c.a();
		return pathEntity;
	}
	
	public PathEntity a(IBlockAccess block, Entity entity1, Entity entity2, float f) {
        return a(block, entity1, entity2.locX, entity2.getBoundingBox().b, entity2.locZ, f);
    }
	
	private PathEntity a(PathPoint point1, PathPoint point2) {
		int i = 1;
		PathPoint point3 = point2;
		try {
			while (H.get(point3) != null) {
				i++;
				point3 = (PathPoint) H.get(point3);
			}
		} catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
		
		PathPoint[] points = new PathPoint[i];
		point3 = point2;
		points[--i] = point3;
		try {
			while (H.get(point3) != null) {
				point3 = (PathPoint)  H.get(point3);
				points[--i] = point3;
			}
		} catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
		
		return new PathEntity(points);
	}
	
	private static Field E = NMSClass.getField(PathPoint.class, "e");
    private static Field F = NMSClass.getField(PathPoint.class, "f");
    private static Field G = NMSClass.getField(PathPoint.class, "g");
    private static Field H = NMSClass.getField(PathPoint.class, "h");
}
