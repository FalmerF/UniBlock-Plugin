package net.ifmcore.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeListInventory implements Listener {
	static ItemStack nextItem;
	static ItemStack previousItem;
	static List<Inventory> recipesInventory = new ArrayList<Inventory>();
	public static RecipeListInventory instance;
	
	public RecipeListInventory() {
		instance = this;
	}
	
	public static void addRecipe(ShapelessRecipe recipe) {
		Inventory inventory = Bukkit.createInventory(null, 45, recipe.getResult().getItemMeta().getDisplayName());
		List<ItemStack> items = recipe.getIngredientList();
		
		for(int i = 0; i < 36; i++) {
			inventory.setItem(i, TradeInventory.emptyInventorySlotItem);
		}
		
		for(int i = 0; i < 9; i++) {
			int slot = 2+i;
			if(i >= 6)
				slot = 20+i-6;
			else if(i >= 3)
				slot = 11+i-3;
			ItemStack item = null;
			if(i < items.size())
				item = items.get(i);
			inventory.setItem(slot, item);
		}
		inventory.setItem(15, recipe.getResult());
		inventory.setItem(36, previousItem);
		inventory.setItem(44, nextItem);
		
		recipesInventory.add(inventory);
	}
	
	public static void addRecipe(ShapedRecipe recipe) {
		Inventory inventory = Bukkit.createInventory(null, 45, recipe.getResult().getItemMeta().getDisplayName());
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		for(String s : recipe.getShape()) {
			for(char c : s.toCharArray()) {
				items.add(recipe.getIngredientMap().get(c));
			}
		}
		
		for(int i = 0; i < 36; i++) {
			inventory.setItem(i, TradeInventory.emptyInventorySlotItem);
		}
		
		for(int i = 0; i < 9; i++) {
			int slot = 2+i;
			if(i >= 6)
				slot = 20+i-6;
			else if(i >= 3)
				slot = 11+i-3;
			ItemStack item = null;
			if(i < items.size())
				item = items.get(i);
			inventory.setItem(slot, item);
		}
		inventory.setItem(15, recipe.getResult());
		inventory.setItem(36, previousItem);
		inventory.setItem(44, nextItem);
		
		recipesInventory.add(inventory);
	}
	
	public static void openRecipe(Player player, int index) {
		index = Math.max(Math.min(index, recipesInventory.size()-1), 0);
		player.openInventory(recipesInventory.get(index));
	}
	
	@EventHandler
    public void inventoryClick(InventoryClickEvent event)
	{
		Inventory inventory = event.getView().getTopInventory();
		if(recipesInventory.contains(inventory)) {
			event.setCancelled(true);
			Inventory clickedInventory = event.getClickedInventory();
			if(clickedInventory == inventory) {
				if(event.getSlot() == 36) {
					openRecipe((Player) event.getWhoClicked(), recipesInventory.indexOf(inventory)-1);
				}
				else if(event.getSlot() == 44) {
					openRecipe((Player) event.getWhoClicked(), recipesInventory.indexOf(inventory)+1);
				}
			}
		}
	}
	
	static {
		nextItem = new ItemStack(Material.ARROW, 1);
		ItemMeta itemMeta = nextItem.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(ChatColor.YELLOW+"Вперед");
		nextItem.setItemMeta(itemMeta);
		
		previousItem = new ItemStack(Material.ARROW, 1);
		itemMeta = previousItem.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(ChatColor.YELLOW+"Назад");
		previousItem.setItemMeta(itemMeta);
	}
}
