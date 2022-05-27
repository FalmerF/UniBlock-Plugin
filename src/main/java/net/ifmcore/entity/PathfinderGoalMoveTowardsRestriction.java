package net.ifmcore.entity;

import java.util.EnumSet;

import net.ifmcore.Uni;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3D;

public class PathfinderGoalMoveTowardsRestriction extends PathfinderGoal {
   private final EntityCreature a;
   private double b;
   private double c;
   private double d;
   private final double e;

   public PathfinderGoalMoveTowardsRestriction(EntityCreature var0, double var1) {
      this.a = var0;
      this.e = var1;
      this.a(EnumSet.of(Type.a));
   }

   public boolean a() {
	   Vec3D var0 = this.h();
		if (var0 == null) {
		   return false;
		} else {
		   this.b = var0.b;
		   this.c = var0.c;
		   this.d = var0.d;
		   return true;
		}
   }
   
   protected Vec3D h() {
	   Vec3D var0 =  Vec3D.c(this.a.fl());
	   Vec3D var2 = HoverRandomPos.a(this.a, 8, 7, var0.b, var0.d, 1.5707964F, 3, 1);
	   return var2 != null ? var2 : AirAndWaterRandomPos.a(this.a, 8, 4, -2, var0.b, var0.d, 1.5707963705062866D);
   }

   public boolean b() {
      return !this.a.D().l();
   }

   public void c() {
      this.a.D().a(this.b, this.c, this.d, this.e);
   }
}