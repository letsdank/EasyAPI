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

import java.util.List;

import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ChunkCache;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NavigationAbstract;
import net.minecraft.server.v1_8_R3.PathEntity;
import net.minecraft.server.v1_8_R3.PathPoint;
import net.minecraft.server.v1_8_R3.Pathfinder;
import net.minecraft.server.v1_8_R3.Vec3D;
import net.minecraft.server.v1_8_R3.World;

/**
 * 
 */
public class PlayerNavigation extends NavigationAbstract {
	private final AttributeInstance a;
    protected EntityBot b;
    protected World c;
    protected PathEntity d;
    protected double e;
    private int f;
    private boolean fb;
    private int g;
    private Vec3D h = new Vec3D(0.0D, 0.0D, 0.0D);
    private float i = 1.0F;
    private final PlayerPathfinder j;
    private final PlayerPathfinderNormal s;
    
    public PlayerNavigation(EntityBot bot, World world) {
        super(getDummyInsentient(bot, world), world);
        b = bot;
        c = world;
        a = bot.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        a.setValue(24);

        s = new PlayerPathfinderNormal();
        s.a(true);
        j = new PlayerPathfinder(s);
        // b.C().a(this);
    }
    
	protected Pathfinder a() {
		return null;
	}
	
	public PathEntity a(BlockPosition pos) {
		if (!b()) {
			return null;
		}
		
		float f1 = i();
		c.methodProfiler.a("pathfind");
		BlockPosition localPos = new BlockPosition(b);
		int k = (int) (f1 + 8.0F);
		
		ChunkCache cache = new ChunkCache(c, localPos.a(-k, -k, -k), localPos.a(k, k, k), 0);
		PathEntity entity = j.a(cache, b, pos, f1);
		c.methodProfiler.b();
		return entity;
	}
	
	public void a(boolean bool) {
		s.c(bool);
	}
	
	public void a(double d) {
		e = d;
	}
	
	public boolean a(double d0, double d1, double d2, double d3) {
		PathEntity entity = a(MathHelper.floor(d0), (int) d1, MathHelper.floor(d2));
		return a(entity, d3);
	}
	
	public PathEntity a(Entity entity) {
		if (!b()) {
			return null;
		}
		float f1 = i();
		c.methodProfiler.a("pathfind");
		BlockPosition pos = new BlockPosition(b).up();
		int k = (int) (f1 + 16.0F);
		
		ChunkCache cache = new ChunkCache(c, pos.a(-k, -k, -k), pos.a(k, k, k), 0);
		PathEntity pathEntity = j.a(cache, b, entity, f1);
		c.methodProfiler.b();
		return pathEntity;
	}
	
	public boolean a(Entity entity, double d) {
		PathEntity pathEntity = a(entity);
		if (pathEntity != null) {
			return a(pathEntity, d);
		}
		
		return false;
	}
	
	public void a(float f) {
		i = f;
	}
	
	private boolean a(int x0, int y0, int z0, int x1, int y1, int z1, Vec3D vec, double d1, double d2) {
		int i = x0 - x1 / 2;
		int j = z0 - z1 / 2;
		if (!b(i, y0, j, x1, y1, z1, vec, d1, d2)) {
			return false;
		}
		
		for (int k = i; k < i + x1; k++) {
			for (int m = j; m < j + z1; m++) {
				double d3 = k + 0.5D - vec.a;
				double d4 = m + 0.5D - vec.c;
				if (d3 * d1 + d4 * d2 >= 0.0D) {
					Block block = c.getType(new BlockPosition(k, y0 - 1, m)).getBlock();
					Material material = block.getMaterial();
					
					if (material == Material.AIR) {
						return false;
					}
					
					if ((material == Material.WATER) && (!b.V())) {
						return false;
					}
					
					if (material == Material.LAVA) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean a(PathEntity entity, double d) {
		if (entity == null) {
			this.d = null;
			return false;
		}
		
		if (!entity.a(this.d)) {
			this.d = entity;
		}
		d();
		if (this.d.d() == 0) {
			return false;
		}
		e = d;
		Vec3D vec = c();
		g = f;
		h = vec;
		return true;
	}
	
	protected void a(Vec3D vec) {
		if (f - g > 100) {
			if (vec.distanceSquared(h) < 2.25D) {
				n();
			}
			g = f;
			h = vec;
		}
	}
	
	protected boolean a(Vec3D vec1, Vec3D vec2, int x, int y, int z) {
		int i = MathHelper.floor(vec1.a);
		int j = MathHelper.floor(vec1.c);
		
		double d1 = vec2.a - vec1.a;
		double d2 = vec2.c - vec1.c;
		double d3 = d1 * d1 + d2 * d2;
		if (d3 < 1.0E-8D) {
			return false;
		}
		
		double d4 = 1.0D / Math.sqrt(d3);
		d1 *= d4;
		d2 *= d4;
		
		x += 2;
		z += 2;
		if (!a(i, (int) vec1.b, j, x, y, z, vec1, d1, d2)) {
			return false;
		}
		
		x -= 2;
		z -= 2;
		
		double d5 = 1.0D / Math.abs(d1);
		double d6 = 1.0D / Math.abs(d2);
		
		double d7 = i * 1 - vec1.a;
		double d8 = j * 1 - vec1.c;
		
		if (d1 >= 0.0D) {
			d7 += 1.0D;
		}
		
		if (d2 >= 0.0D) {
			d8 += 1.0D;
		}
		
		d7 /= d1;
		d8 /= d2;
		
		int k = d1 < 0.0D ? -1 : 1;
		int m = d2 < 0.0D ? -1 : 1;
		int n = MathHelper.floor(vec2.a);
		int i1 = MathHelper.floor(vec2.c);
		int i2 = n - i;
		int i3 = i1 - j;
		while ((i2 * k > 0) || (i3 * m > 0)) {
			if (d7 < d8) {
				d7 += d5;
				i += k;
				i2 = n - i;
			} else {
				d8 += d6;
				j += m;
				i3 = i1 - j;
			}
			if (!a(i, (int) vec1.b, j, x, y, z, vec1, d1, d2)) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean b() {
		return (b.onGround) || ((h()) && (o())) || ((b.au()));
	}
	
	public void b(boolean bool) {
		s.b(bool);
	}
	
	private boolean b(int x0, int y0, int z0, int x1, int y1, int z1, Vec3D vec, double d1, double d2) {
		for (BlockPosition pos : BlockPosition.a(new BlockPosition(x0, y0, z0), new BlockPosition(x0 + x1 - 1, y0 + y1 - 1, z0 + z1 - 1))) {
			double d3 = pos.getX() + 0.5D - vec.a;
			double d4 = pos.getZ() + 0.5D - vec.c;
			if (d3 * d1 + d4 * d2 >= 0.0D) {
				Block block = c.getType(pos).getBlock();
				if (!block.b(c, pos)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	protected Vec3D c() {
		return new Vec3D(b.locX, p(), b.locZ);
	}
	
	public void c(boolean bool) {
		s.a(bool);
	}
	
	protected void d() {
		super.d();
		if (fb) {
			if (c.i(new BlockPosition(MathHelper.floor(b.locX), (int) (b.getBoundingBox().b + 0.5D), MathHelper.floor(b.locZ)))) {
				return;
			}
			for (int i = 0; i < d.d(); i++) {
				PathPoint point = d.a(i);
				if (c.i(new BlockPosition(point.a, point.b, point.c))) {
					d.b(i - 1);
					return;
				}
			}
		}
	}
	
	public void d(boolean bool) {
        s.d(bool);
    }

    public boolean e() {
        return s.e();
    }

    public void e(boolean bool) {
    	fb = bool;
    }

    public boolean g() {
        return s.b();
    }

    public boolean h() {
        return s.d();
    }
    
    public float i() {
        return (float) a.getValue();
    }

    public PathEntity j() {
        return d;
    }
    
    public void k() {
    	f += 1;
    	if (m()) {
    		return;
    	}
    	if (b()) {
    		l();
    	} else if ((d != null) && (d.e() < d.d())) {
    		Vec3D vec1 = c();
    		Vec3D vec2 = d.a(b, d.e());
    		if ((vec1.b > vec2.b) && (!b.onGround) && (MathHelper.floor(vec1.a) == MathHelper.floor(vec2.a)) && (MathHelper.floor(vec1.c) == MathHelper.floor(vec2.c))) {
    			d.c(d.e() + 1);
    		}
    	}
    	if (m()) {
    		return;
    	}
    	Vec3D vec = d.a(b);
    	if (vec == null) {
    		return;
    	}
    	Object obj = new AxisAlignedBB(vec.a, vec.b, vec.c, vec.a, vec.b, vec.c).grow(0.5D, 0.5D, 0.5D);
    	List<AxisAlignedBB> aabbs = c.getCubes(b, ((AxisAlignedBB) obj).a(0.0D, -1.0D, 0.0D));
    	double d1 = -1.0D;
    	obj = ((AxisAlignedBB) obj).c(0.0D, 1.0D, 0.0D);
    	for (AxisAlignedBB bb : aabbs) {
    		d1 = bb.b((AxisAlignedBB) obj, d1);
    	}
    	b.getControllerMove().a(vec.a, vec.b + d1, vec.c, e);
    }
    
    protected void l() {
    	Vec3D vec = c();

        int k = d.d();
        for (int m = d.e(); m < d.d(); m++) {
            if (d.a(m).b != (int) vec.b) {
                k = m;
                break;
            }
        }
        float f1 = b.width * b.width * i;
        for (int n = d.e(); n < k; n++) {
            Vec3D vec2 =d.a(b, n);
            if (vec.distanceSquared(vec2) < f1) {
                d.c(n + 1);
            }
        }
        int n = MathHelper.f(b.width);
        int i1 = (int) b.length + 1;
        int i2 = n;
        for (int i3 = k - 1; i3 >= d.e(); i3--) {
            if (a(vec, d.a(b, i3), n, i1, i2)) {
                d.c(i3);
                break;
            }
        }
        a(vec);
    }
    
    public boolean m() {
    	return (d == null) || (d.b());
    }
    
    public void n() {
    	d = null;
    }
    
    protected boolean o() {
    	return (b.V()) || (b.ab());
    }
    
    private int p() {
    	if ((!b.V()) || (!h())) {
            return (int) (b.getBoundingBox().b + 0.5D);
        }
        int i = (int) b.getBoundingBox().b;
        Block localBlock = c
                .getType(new BlockPosition(MathHelper.floor(b.locX), i, MathHelper.floor(b.locZ))).getBlock();
        int j = 0;
        while ((localBlock == Blocks.FLOWING_WATER) || (localBlock == Blocks.WATER)) {
            i++;
            localBlock = c
                    .getType(new BlockPosition(MathHelper.floor(b.locX), i, MathHelper.floor(b.locZ)))
                    .getBlock();
            j++;
            if (j > 16) {
                return (int) b.getBoundingBox().b;
            }
        }
        return i;
    }
    
    public void setRange(float pathfindingRange) {
        this.a.setValue(pathfindingRange);
    }

    private static EntityInsentient getDummyInsentient(EntityBot from, World world) {
        return new EntityInsentient(world) {
        };
    }
}
