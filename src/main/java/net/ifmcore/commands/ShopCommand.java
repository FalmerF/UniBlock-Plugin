package net.ifmcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import net.ifmcore.CustomEnchants;

public class ShopCommand implements CommandExecutor {
	public static Merchant merchant;
	public static List<MerchantRecipe> merchantRecipes;
	public static List<MerchantRecipe> merchantRecipesLvl_1;
	public static List<MerchantRecipe> merchantRecipesLvl_2;
	public static List<MerchantRecipe> merchantRecipesLvl_3;
	public static List<MerchantRecipe> merchantRecipesLvl_4;
	public static List<MerchantRecipe> merchantRecipesLvl_5;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
	        try {
	        	if(((Player)sender).getGameMode() != GameMode.CREATIVE) return true;
	        	Player player = (Player) sender;
	        	player.openMerchant(merchant, true);
    		}
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
	    }
		return true;
	}
	
	static {
		merchant = Bukkit.createMerchant("Магазин супер книг!");
		merchantRecipes = new ArrayList<MerchantRecipe>();
		
		MerchantRecipe itemRecipe = new MerchantRecipe(CustomEnchants.makeEnchantBook("Airbag", 1), 0, 1, true, 3, 0);
		itemRecipe.addIngredient(new ItemStack(Material.EMERALD, 46));
		itemRecipe.addIngredient(new ItemStack(Material.CHORUS_FRUIT, 38));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(new ItemStack(Material.EMERALD), 0, 16, true, 1, 0);
		itemRecipe.addIngredient(new ItemStack(Material.CHORUS_FRUIT, 64));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(CustomEnchants.makeEnchantBook("Lumberjack", 1), 0, 1, true, 3, 0);
		itemRecipe.addIngredient(new ItemStack(Material.EMERALD, 64));
		itemRecipe.addIngredient(new ItemStack(Material.WARPED_STEM, 48));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(new ItemStack(Material.EMERALD), 0, 16, true, 1, 0);
		itemRecipe.addIngredient(new ItemStack(Material.CRIMSON_STEM, 64));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(CustomEnchants.makeEnchantBook("Tunnel", 1), 0, 1, true, 3, 0);
		itemRecipe.addIngredient(new ItemStack(Material.NETHERITE_INGOT, 10));
		itemRecipe.addIngredient(new ItemStack(Material.AMETHYST_SHARD, 64));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(new ItemStack(Material.EMERALD), 0, 16, true, 1, 0);
		itemRecipe.addIngredient(new ItemStack(Material.AMETHYST_SHARD, 32));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(CustomEnchants.makeEnchantBook("Accuracy", 1), 0, 1, true, 3, 0);
		itemRecipe.addIngredient(new ItemStack(Material.DEEPSLATE_EMERALD_ORE, 16));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(new ItemStack(Material.EMERALD), 0, 16, true, 1, 0);
		itemRecipe.addIngredient(new ItemStack(Material.GRASS_BLOCK, 32));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(CustomEnchants.makeEnchantBook("MoreExp", 1), 0, 1, true, 3, 0);
		itemRecipe.addIngredient(new ItemStack(Material.EMERALD, 5));
		itemRecipe.addIngredient(new ItemStack(Material.EXPERIENCE_BOTTLE, 6));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(new ItemStack(Material.EMERALD), 0, 16, true, 1, 0);
		itemRecipe.addIngredient(new ItemStack(Material.ROTTEN_FLESH, 8));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(CustomEnchants.makeEnchantBook("MoreExp", 2), 0, 1, true, 3, 0);
		itemRecipe.addIngredient(new ItemStack(Material.DIAMOND, 10));
		itemRecipe.addIngredient(new ItemStack(Material.EXPERIENCE_BOTTLE, 16));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(new ItemStack(Material.EMERALD), 0, 16, true, 1, 0);
		itemRecipe.addIngredient(new ItemStack(Material.GUNPOWDER, 8));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(CustomEnchants.makeEnchantBook("MoreExp", 3), 0, 1, true, 3, 0);
		itemRecipe.addIngredient(new ItemStack(Material.NETHERITE_INGOT, 5));
		itemRecipe.addIngredient(new ItemStack(Material.EXPERIENCE_BOTTLE, 32));
		merchantRecipes.add(itemRecipe);
		
		itemRecipe = new MerchantRecipe(new ItemStack(Material.EMERALD), 0, 16, true, 1, 0);
		itemRecipe.addIngredient(new ItemStack(Material.SPIDER_EYE, 2));
		merchantRecipes.add(itemRecipe);
		
		merchant.setRecipes(merchantRecipes);
		
		merchantRecipesLvl_1 = new ArrayList<MerchantRecipe>();
		for(int i = 0; i < 2; i++) merchantRecipesLvl_1.add(merchantRecipes.get(i));
		
		merchantRecipesLvl_2 = new ArrayList<MerchantRecipe>();
		for(int i = 0; i < 4; i++) merchantRecipesLvl_2.add(merchantRecipes.get(i));
		
		merchantRecipesLvl_3 = new ArrayList<MerchantRecipe>();
		for(int i = 0; i < 6; i++) merchantRecipesLvl_3.add(merchantRecipes.get(i));
		
		merchantRecipesLvl_4 = new ArrayList<MerchantRecipe>();
		for(int i = 0; i < 8; i++) merchantRecipesLvl_4.add(merchantRecipes.get(i));
		
		merchantRecipesLvl_5 = new ArrayList<MerchantRecipe>();
		for(int i = 0; i < 14; i++) merchantRecipesLvl_5.add(merchantRecipes.get(i));
	}
}
