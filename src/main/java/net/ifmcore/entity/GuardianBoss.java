package net.ifmcore.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

import com.comphenix.protocol.reflect.FieldUtils;

import net.ifmcore.Uni;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalPanic;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.navigation.NavigationFlying;
import net.minecraft.world.entity.ai.attributes.AttributeDefaults;
import net.minecraft.world.entity.ai.attributes.AttributeMapBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.AttributeProvider.Builder;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.monster.EntityGuardian;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.syncher.DataWatcherObject;

public class GuardianBoss extends EntityGuardian {
	public GuardianBoss(Location loc) {
		super(EntityTypes.K, ((CraftWorld)loc.getWorld()).getHandle());
		
		this.a(loc.getX(), loc.getY(), loc.getZ());
		this.e(true);
		this.u();
		this.bQ.a();
		
		this.d = new PathfinderGoalRandomStroll(this, 1.0D, 80);
		try {
			Class guardianAttackClass = Class.forName("net.minecraft.world.entity.monster.EntityGuardian$PathfinderGoalGuardianAttack");
			Constructor c = guardianAttackClass.getDeclaredConstructors()[0];
			c.setAccessible(true);
			this.bQ.a(4, (PathfinderGoal) c.newInstance(this));
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.bQ.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1));
//		this.bQ.a(7, this.d);
		this.bQ.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.bQ.a(8, new PathfinderGoalLookAtPlayer(this, EntityGuardian.class, 12.0F, 0.01F));
		this.bQ.a(9, new PathfinderGoalRandomLookaround(this));
		this.bQ.a(9, new PathfinderGoalPanic(this, 3));
		
		this.bP = new NavigationFlying(this, ((CraftWorld)loc.getWorld()).getHandle());
		
		AttributeModifiable maxHealth = this.a(GenericAttributes.a);
		maxHealth.a(500);
		
		AttributeModifiable damage = this.a(GenericAttributes.f);
		damage.a(60);
		
		AttributeModifiable speed = this.a(GenericAttributes.d);
		speed.a(1f);
		
		AttributeModifiable followRange = this.a(GenericAttributes.b);
		followRange.a(32);
		
		this.Y.<Float>b(EntityLiving.bK, 500f);
		
		this.a((IChatBaseComponent)ChatSerializer.a("{\"text\": \"Стражник\"}"));
		this.n(true);
		this.a("isCustomEntity");
	}
	
	public void h(Vec3D var0) {
		this.a(0.1F, var0);
        this.a(EnumMoveType.a, this.da());
        this.g(this.da().a(0.9D));
	}
	
	public static Builder fz() {
	      return EntityMonster.fE().a(GenericAttributes.f, 6.0D).a(GenericAttributes.d, 0.5D).a(GenericAttributes.b, 16.0D).a(GenericAttributes.a, 30.0D).a(GenericAttributes.h, 1.0D);
  	}
	
	static {
		
	}
}
