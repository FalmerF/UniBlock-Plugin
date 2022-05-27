package net.ifmcore;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import net.ifmcore.NBTEditor.NBTCompound;
import net.ifmcore.data.ConfigsManager;

public class ItemUtils {
	public static ItemStack emptyInventorySlotItem;
	public static boolean checkIfPickaxe(Material mat) {
		if(mat == Material.WOODEN_PICKAXE || mat == Material.STONE_PICKAXE || mat == Material.IRON_PICKAXE
				|| mat == Material.DIAMOND_PICKAXE || mat == Material.NETHERITE_PICKAXE || mat == Material.GOLDEN_PICKAXE)
			return true;
		return false;
	}
	
	public static boolean checkIfAxe(Material mat) {
		if(mat == Material.WOODEN_AXE || mat == Material.STONE_AXE || mat == Material.IRON_AXE
				|| mat == Material.DIAMOND_AXE || mat == Material.NETHERITE_AXE || mat == Material.GOLDEN_AXE)
			return true;
		return false;
	}
	
	public static boolean checkIfShovel(Material mat) {
		if(mat == Material.WOODEN_SHOVEL || mat == Material.STONE_SHOVEL || mat == Material.IRON_SHOVEL
				|| mat == Material.DIAMOND_SHOVEL || mat == Material.NETHERITE_SHOVEL || mat == Material.GOLDEN_SHOVEL)
			return true;
		return false;
	}
	
	public static boolean checkIfHoe(Material mat) {
		if(mat == Material.WOODEN_HOE || mat == Material.STONE_HOE || mat == Material.IRON_HOE
				|| mat == Material.DIAMOND_HOE || mat == Material.NETHERITE_HOE || mat == Material.GOLDEN_HOE)
			return true;
		return false;
	}
	
	public static boolean checkIfSword(Material mat) {
		if(mat == Material.WOODEN_SWORD || mat == Material.STONE_SWORD || mat == Material.IRON_SWORD
				|| mat == Material.DIAMOND_SWORD || mat == Material.NETHERITE_SWORD || mat == Material.GOLDEN_SWORD)
			return true;
		return false;
	}
	
	public static boolean checkIfFood(Material mat) {
		if(mat == Material.WHEAT || mat == Material.POTATOES || mat == Material.CARROTS
				|| mat == Material.MELON || mat == Material.PUMPKIN || mat == Material.COCOA || mat == Material.BEETROOTS
				|| mat == Material.NETHER_WART)
			return true;
		return false;
	}
	
	public static boolean checkIfHelmet(Material mat) {
		if(mat == Material.LEATHER_HELMET || mat == Material.IRON_HELMET || mat == Material.CHAINMAIL_HELMET
				|| mat == Material.GOLDEN_HELMET || mat == Material.DIAMOND_HELMET || mat == Material.NETHERITE_HELMET
				|| mat == Material.TURTLE_HELMET)
			return true;
		return false;
	}
	
	public static boolean checkIfChestplate(Material mat) {
		if(mat == Material.LEATHER_CHESTPLATE || mat == Material.IRON_CHESTPLATE || mat == Material.CHAINMAIL_CHESTPLATE
				|| mat == Material.GOLDEN_CHESTPLATE || mat == Material.DIAMOND_CHESTPLATE || mat == Material.NETHERITE_CHESTPLATE)
			return true;
		return false;
	}
	
	public static boolean checkIfLeggings(Material mat) {
		if(mat == Material.LEATHER_LEGGINGS || mat == Material.IRON_LEGGINGS || mat == Material.CHAINMAIL_LEGGINGS
				|| mat == Material.GOLDEN_LEGGINGS || mat == Material.DIAMOND_LEGGINGS || mat == Material.NETHERITE_LEGGINGS)
			return true;
		return false;
	}
	
	public static boolean checkIfBoots(Material mat) {
		if(mat == Material.LEATHER_BOOTS || mat == Material.IRON_BOOTS || mat == Material.CHAINMAIL_BOOTS
				|| mat == Material.GOLDEN_BOOTS || mat == Material.DIAMOND_BOOTS || mat == Material.NETHERITE_BOOTS)
			return true;
		return false;
	}
	
	public static boolean checkIfArmor(Material mat) {
		if(checkIfHelmet(mat) || checkIfChestplate(mat) || checkIfLeggings(mat) || checkIfBoots(mat)
				|| mat == Material.SHIELD)
			return true;
		return false;
	}
	
	public static boolean checkIfTool(Material mat) {
		if(checkIfPickaxe(mat) || checkIfAxe(mat) || checkIfShovel(mat) || checkIfSword(mat) || checkIfHoe(mat)
				|| mat == Material.FISHING_ROD || mat == Material.SHEARS || mat == Material.COMPASS
				|| mat == Material.CLOCK || mat == Material.BOW || mat == Material.TRIDENT || mat == Material.CROSSBOW)
			return true;
		return false;
	}
	
	public static boolean checkIfImportantly(Material mat) {
		if(checkIfTool(mat) || checkIfArmor(mat))
			return true;
		return false;
	}
	
	public static int itemsCount(PlayerInventory inventory, ItemStack item) {
		int count = 0;
		for(ItemStack is : inventory.getContents())
			if(is != null && is.isSimilar(item))
				count += is.getAmount();
		return count;
	}
	
	public static boolean removeItem(PlayerInventory inventory, ItemStack item) {
		ItemStack itemCopyToEquals = item.clone();
		itemCopyToEquals.setAmount(1);
		for(int i = 0; i < inventory.getSize(); i++) {
			ItemStack is = inventory.getItem(i);
			if(is == null)
				continue;
			ItemStack isCopyToEquals = is.clone();
			isCopyToEquals.setAmount(1);
			if(isCopyToEquals.equals(itemCopyToEquals) && is.getAmount() >= item.getAmount()) {
				is.setAmount(is.getAmount()-item.getAmount());
				return true;
			}
		}
		return false;
	}
	
	public static boolean removeMultiplyItem(PlayerInventory inventory, ItemStack item) {
		int amount = item.getAmount();
		for(int i = 0; i < inventory.getSize(); i++) {
			ItemStack is = inventory.getItem(i);
			if(is == null || is.getType() == Material.AIR)
				continue;
			if(is.isSimilar(item)) {
				if(is.getAmount() >= amount) {
					is.setAmount(is.getAmount()-amount);
					return true;
				}
				else {
					amount -= is.getAmount();
					is.setAmount(0);
				}
			}
		}
		return false;
	}
	
	public static void dropItemsFromMap(Player player, HashMap<Integer, ItemStack> items) {
		for(Entry<Integer, ItemStack> set : items.entrySet()) {
			if(set.getValue().getAmount() == 0) continue;
			player.getWorld().dropItemNaturally(player.getLocation(), set.getValue());
		}
	}
	
	public static void givePlayerItem(Player player, ItemStack item) {
		dropItemsFromMap(player, player.getInventory().addItem(item));
	}
	
	public static boolean checkItemInFilter(String filter, ItemStack item) {
		NBTCompound nbt = NBTEditor.getNBTCompound(filter);
		int size = NBTEditor.getInt(nbt, "size");
		for(int i = 0; i < size; i++) {
			Material mat = Material.valueOf(NBTEditor.getString(nbt, String.valueOf(i), "Material"));
			String nbtJson = NBTEditor.getString(nbt, String.valueOf(i), "NBT");
			if(mat == item.getType()) {
				if(nbtJson != null && !nbtJson.equals("")) {
					ItemStack filterItem = NBTEditor.getItemFromTag(NBTEditor.getNBTCompound(nbtJson));
					if(filterItem.isSimilar(item))
						return true;
				}
				else
					return true;
			}
		}
		return false;
	}
	
	public static String getItemName(ItemStack item) {
		if(item.getItemMeta().hasDisplayName())
			return item.getItemMeta().getDisplayName();
		if(item.getType().isBlock())
			return ConfigsManager.localization.getOrDefault("block.minecraft."+item.getType().toString().toLowerCase(), "");
		if(item.getType().isItem())
			return ConfigsManager.localization.getOrDefault("item.minecraft."+item.getType().toString().toLowerCase(), "");
		return "";
	}
	
	public static ItemStack setItemTags(ItemStack item) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public static ItemStack setItemLore(ItemStack item, List<String> lore) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public static ItemStack setItemName(ItemStack item, String name) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	static {
		emptyInventorySlotItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta itemMeta = emptyInventorySlotItem.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(" ");
		emptyInventorySlotItem.setItemMeta(itemMeta);
	}
}
