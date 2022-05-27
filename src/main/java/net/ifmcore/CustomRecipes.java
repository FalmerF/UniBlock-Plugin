package net.ifmcore;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.ifmcore.inventory.RecipeListInventory;

import org.bukkit.inventory.ShapedRecipe;

public class CustomRecipes {
	public static ShapelessRecipe customBottleRecipe;
	public static void MakeRecipes() {
		mobBottleRecipe();
		expBottleRecipe();
		customExpBottleRecipe();
		voidEyeRecipe();
		voidProtectionRecipe();
		strangeHeartRecipe();
		quartzRecipe();
		hopperFilterRecipe();
		filterWorkbenchRecipe();
		voidSkullBeaterRecipe();
		voidVampireRecipe();
		decoyOfDeathRecipe();
	}
	
	static void mobBottleRecipe() {
		ItemStack item = CustomItems.getCustomItem("mobBottle", 1);
		
		ShapedRecipe itemRecipe = new ShapedRecipe(NamespacedKey.randomKey(), item);
		
		itemRecipe.shape(" & ","#B*"," & ");
		
		itemRecipe.setIngredient('#', Material.AMETHYST_SHARD);
		itemRecipe.setIngredient('*', Material.ENDER_PEARL);
		itemRecipe.setIngredient('B', Material.GLASS_BOTTLE);
		itemRecipe.setIngredient('&', Material.DIAMOND);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void expBottleRecipe() {
		ShapelessRecipe itemRecipe = new ShapelessRecipe(NamespacedKey.randomKey(), new ItemStack(Material.EXPERIENCE_BOTTLE));
		 
		itemRecipe.addIngredient(Material.GOLD_NUGGET);
		itemRecipe.addIngredient(Material.BLAZE_POWDER);
		itemRecipe.addIngredient(Material.GLASS_BOTTLE);
		itemRecipe.addIngredient(Material.GLISTERING_MELON_SLICE);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void customExpBottleRecipe() {
		ItemStack item = CustomItems.getCustomItem("customExpBottle2", 1);
		ShapelessRecipe itemRecipe = new ShapelessRecipe(NamespacedKey.randomKey(), item);
		 
		itemRecipe.addIngredient(Material.DIAMOND);
		itemRecipe.addIngredient(Material.GLASS_BOTTLE);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		customBottleRecipe = itemRecipe;
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void voidEyeRecipe() {
		ItemStack item = CustomItems.getCustomItem("voidEye", 8);
		ShapedRecipe itemRecipe = new ShapedRecipe(NamespacedKey.randomKey(), item);
		
		itemRecipe.shape("*#*","#B#","*#*");
		
		itemRecipe.setIngredient('#', Material.ENDER_EYE);
		itemRecipe.setIngredient('*', Material.AMETHYST_SHARD);
		itemRecipe.setIngredient('B', Material.NETHER_STAR);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void voidProtectionRecipe() {
		ShapelessRecipe itemRecipe = new ShapelessRecipe(NamespacedKey.randomKey(), CustomEnchants.makeEnchantBook("VoidProtection", 1));
		 
		itemRecipe.addIngredient(new RecipeChoice.ExactChoice(CustomItems.getCustomItem("voidCrystal1", 1)));
		itemRecipe.addIngredient(Material.ENCHANTED_BOOK);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void strangeHeartRecipe() {
		ShapedRecipe itemRecipe = new ShapedRecipe(NamespacedKey.randomKey(), CustomItems.getCustomItem("strangeHeart", 1));
		 
		itemRecipe.shape("*C*","#B#","*#*");
		
		itemRecipe.setIngredient('C', new RecipeChoice.ExactChoice(CustomItems.getCustomItem("voidCrystal2", 1)));
		itemRecipe.setIngredient('B', Material.HEART_OF_THE_SEA);
		itemRecipe.setIngredient('*', Material.ENDER_PEARL);
		itemRecipe.setIngredient('#', Material.CHORUS_FRUIT);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void quartzRecipe() {
		ShapelessRecipe itemRecipe = new ShapelessRecipe(NamespacedKey.randomKey(), new ItemStack(Material.QUARTZ, 4));
		 
		itemRecipe.addIngredient(Material.QUARTZ_BLOCK);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void hopperFilterRecipe() {
		ShapedRecipe itemRecipe = new ShapedRecipe(NamespacedKey.randomKey(), CustomItems.getCustomItem("hopperFilter", 1));
		 
		itemRecipe.shape(" *&","*#*","&* ");
		
		itemRecipe.setIngredient('*', Material.REDSTONE);
		itemRecipe.setIngredient('#', Material.PAPER);
		itemRecipe.setIngredient('&', Material.DIAMOND);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void filterWorkbenchRecipe() {
		ShapedRecipe itemRecipe = new ShapedRecipe(NamespacedKey.randomKey(), CustomItems.getCustomItem("filterWorkbench", 1));
		 
		itemRecipe.shape("&*&","*#*","&*&");
		
		itemRecipe.setIngredient('*', Material.PAPER);
		itemRecipe.setIngredient('#', Material.CRAFTING_TABLE);
		itemRecipe.setIngredient('&', Material.DIAMOND);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void voidSkullBeaterRecipe() {
		ShapelessRecipe itemRecipe = new ShapelessRecipe(NamespacedKey.randomKey(), CustomEnchants.makeEnchantBook("SkullBeater", 1));
		 
		itemRecipe.addIngredient(new RecipeChoice.ExactChoice(CustomItems.getCustomItem("eternalCoal", 1)));
		itemRecipe.addIngredient(Material.ENCHANTED_BOOK);
		itemRecipe.addIngredient(new RecipeChoice.ExactChoice(CustomItems.getCustomItem("defectiveChorus", 1)));
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void voidVampireRecipe() {
		ShapelessRecipe itemRecipe = new ShapelessRecipe(NamespacedKey.randomKey(), CustomEnchants.makeEnchantBook("Vampire", 1));
		 
		ItemStack potion = new ItemStack(Material.POTION);
		PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
		potionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
		potion.setItemMeta(potionMeta);
		
		itemRecipe.addIngredient(new RecipeChoice.ExactChoice(CustomItems.getCustomItem("voidCrystal3", 1)));
		itemRecipe.addIngredient(Material.ENCHANTED_BOOK);
		itemRecipe.addIngredient(new RecipeChoice.ExactChoice(potion));
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
	
	static void decoyOfDeathRecipe() {
		ShapedRecipe itemRecipe = new ShapedRecipe(NamespacedKey.randomKey(), CustomItems.getCustomItem("decoyOfDeath", 1));
		 
		itemRecipe.shape("***","*#*","***");
		
		itemRecipe.setIngredient('*', new RecipeChoice.ExactChoice(CustomItems.getCustomItem("enchPrismarineShard", 1)));
		itemRecipe.setIngredient('#', Material.GOLDEN_CARROT);
		 
		Uni.instance.getServer().addRecipe(itemRecipe);
		
		RecipeListInventory.addRecipe(itemRecipe);
	}
}
