package net.ifmcore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CustomEnchants implements Listener {
	public static HashMap<String, String> enchantsName = new HashMap<String, String>();
	enum ItemType {
		Pickaxe,
		Axe,
		Shovel,
		Armor,
		Helmet,
		Chestplate,
		Leggings,
		Boots,
		Sword,
		Elytra,
		FishingRod;
	}
	
	public static int getEnchantMaxLevel(ItemStack item) {
		return NBTEditor.contains(item, "customEnchantMaxLevel") ? NBTEditor.getInt(item, "customEnchantMaxLevel") : 0;
	}
	
	public static int getEnchantLevel(ItemStack item) {
		return NBTEditor.contains(item, "customEnchantLevel") ? NBTEditor.getInt(item, "customEnchantLevel") : 1;
	}
	
	public static ItemType[] getEnchantTypes(ItemStack item) {
		String types = NBTEditor.contains(item, "customEnchantTypes") ? NBTEditor.getString(item, "customEnchantTypes") : "";
		List<ItemType> typesList = new ArrayList<ItemType>();
		for(String t : types.split(","))
			typesList.add(ItemType.valueOf(t));
		return typesList.toArray(new ItemType[] {});
	}
	
	public static String getEnchantLocalizedName(String unlocalizedName, int level) {
		String name = enchantsName.getOrDefault(unlocalizedName, "?");
		String localizedName = ChatColor.GRAY+name+" "+getLocalizedEnchantLevel(level);
		return localizedName;
	}
	
	public static boolean isCustomEnchant(ItemStack item) {
		return NBTEditor.contains(item, "customItemName") ? NBTEditor.getString(item, "customItemName").startsWith("enchant") : false;
	}
	
	public static ItemStack makeEnchantBook(String name, int level) {
		ItemStack item = CustomItems.items.getOrDefault("enchant"+name, null);
		return makeEnchantBook(item, level);
	}
	
	public static ItemStack makeEnchantBook(ItemStack item, int level) {
		if(item != null) {
			int maxLevel = getEnchantMaxLevel(item);
			level = Math.min(level, maxLevel);
			item = NBTEditor.set(item, level, "customEnchantLevel");
			
			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = itemMeta.getLore();
			String unlocalizedName = CustomItems.getCustomItemName(item).replaceAll("enchant", "");
			lore.add(0, getEnchantLocalizedName(unlocalizedName, level));
			itemMeta.setLore(lore);
//			String displayName = itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : "";
//			displayName += " "+getLocalizedEnchantLevel(level);
//			itemMeta.setDisplayName(displayName);
			item.setItemMeta(itemMeta);
		}
		return item;
	}
	
	public static ItemStack setEnchantTags(ItemStack item, int maxLevel, ItemType[] types) {
		StringBuilder supportedTypes = new StringBuilder();
		for(ItemType t : types)
			supportedTypes.append(t.name()).append(",");
		item = NBTEditor.set(item, maxLevel, "customEnchantMaxLevel");
		item = NBTEditor.set(item, supportedTypes.substring(0, supportedTypes.length()-1), "customEnchantTypes");
		return item;
	}
	
	public static Map<String, Integer> getItemEnchants(ItemStack item) {
		Map<String, Integer> enchants = new TreeMap<String, Integer>();
		String enchantTag = NBTEditor.contains(item, "customEnchants") ? NBTEditor.getString(item, "customEnchants") : "";
		for(String ench : enchantTag.split(",")) {
			if(!ench.equals("")) {
				String[] enchData = ench.split(":");
				enchants.put(enchData[0], Integer.parseInt(enchData[1]));
			}
		}

		return enchants;
	}
	
	public static ItemStack setItemEnchants(ItemStack item, Map<String, Integer> enchants) {
		StringBuilder enchantTag = new StringBuilder();
		for(Entry<String, Integer> ench : enchants.entrySet())
			enchantTag.append(ench.getKey()).append(":").append(ench.getValue()).append(",");
		item = NBTEditor.set(item, enchantTag.substring(0, enchantTag.length()-1), "customEnchants");
		
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<String>();
		List<String> newLore = new ArrayList<String>();
		for(String line : lore)
			if(!line.startsWith(String.valueOf('\u256D')))
				newLore.add(line);
		for(Entry<String, Integer> ench : enchants.entrySet()) {
			newLore.add("\u256D"+getEnchantLocalizedName(ench.getKey(), ench.getValue()));
		}
		itemMeta.setLore(newLore);
		item.setItemMeta(itemMeta);
		
		return item;
	}
	
	public static boolean canEnchantItem(ItemStack item, ItemStack enchant) {
		String enchantName = CustomItems.getCustomItemName(enchant).replace("enchant", "");
		int enchantLevel = getEnchantLevel(enchant);
		if(enchantName.equals("LuckPlus") && item.getEnchantmentLevel(Enchantment.LUCK) >= enchantLevel) {
			return false;
		}
		else {
			Map<String, Integer> enchants = getItemEnchants(item);
			int currentEnchantLevel = enchants.getOrDefault(enchantName, 0);
			if(currentEnchantLevel >= enchantLevel) {
				return false;
			}
		}
		
		ItemType[] types = getEnchantTypes(enchant);
		for(ItemType t : types) {
			switch(t) {
				case Sword:
					if(ItemUtils.checkIfSword(item.getType())) return true;
					break;
				case Pickaxe:
					if(ItemUtils.checkIfPickaxe(item.getType())) return true;
					break;
				case Shovel:
					if(ItemUtils.checkIfShovel(item.getType())) return true;
					break;
				case Axe:
					if(ItemUtils.checkIfAxe(item.getType())) return true;
					break;
				case Armor:
					if(ItemUtils.checkIfArmor(item.getType())) return true;
					break;
				case Helmet:
					if(ItemUtils.checkIfHelmet(item.getType())) return true;
					break;
				case Chestplate:
					if(ItemUtils.checkIfChestplate(item.getType())) return true;
					break;
				case Leggings:
					if(ItemUtils.checkIfLeggings(item.getType())) return true;
					break;
				case Boots:
					if(ItemUtils.checkIfBoots(item.getType())) return true;
					break;
				case Elytra:
					if(item.getType() == Material.ELYTRA) return true;
					break;
				case FishingRod:
					if(item.getType() == Material.FISHING_ROD) return true;
					break;
			}
		}
		return false;
	}
	
	public static ItemStack enchantItem(ItemStack item, ItemStack enchant) {
		String enchantName = CustomItems.getCustomItemName(enchant).replace("enchant", "");
		int enchantLevel = getEnchantLevel(enchant);
		return enchantItem(item, enchantName, enchantLevel);
	}
	
	public static ItemStack enchantItem(ItemStack item, String enchantName, int enchantLevel) {
		if(enchantName.equals("LuckPlus")) {
			item.addUnsafeEnchantment(Enchantment.LUCK, enchantLevel);
		}
		else {
			Map<String, Integer> enchants = getItemEnchants(item);
			int currentEnchantLevel = enchants.getOrDefault(enchantName, 0);
			if(currentEnchantLevel < enchantLevel) {
				enchants.put(enchantName, enchantLevel);
				item = setItemEnchants(item, enchants);
			}
		}
		return item;
	}
	
	@EventHandler
    public void inventoryClick(InventoryClickEvent event)
	{
		try {
			ItemStack clickedItem = event.getCurrentItem();
			ItemStack cursorItem = event.getCursor();
			if(clickedItem != null && cursorItem != null && isCustomEnchant(cursorItem)) {
				if(canEnchantItem(clickedItem, cursorItem)) {
					clickedItem = enchantItem(clickedItem, cursorItem);
					event.getView().setCursor(null);
					event.setCurrentItem(clickedItem);
					event.setCancelled(true);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
    public void inventoryClick(InventoryCreativeEvent event)
	{
		try {
			ItemStack clickedItem = event.getCurrentItem();
			ItemStack cursorItem = event.getCursor();
			if(clickedItem != null && cursorItem != null && isCustomEnchant(cursorItem)) {
				if(canEnchantItem(clickedItem, cursorItem)) {
					event.setCancelled(true);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getLocalizedEnchantLevel(int level) {
		switch(level) {
			case 1: return "";
			case 2: return "II";
			case 3: return "III";
			case 4: return "IV";
			case 5: return "V";
			case 6: return "VI";
			case 7: return "VII";
			case 8: return "VIII";
			case 9: return "IX";
			case 10: return "X";
			case 11: return "XI";
			case 12: return "XII";
			case 13: return "XIII";
			case 14: return "XIV";
			case 15: return "XV";
			case 16: return "XVI";
			case 17: return "XVII";
			case 18: return "XVIII";
			case 19: return "XIX";
			case 20: return "XX";
		}
		return "";
	}
	
	static {
		enchantsName.put("Lumberjack", "Лесоруб");
		enchantsName.put("Airbag", "Подушка безопасности");
		enchantsName.put("Tunnel", "Туннель");
		enchantsName.put("Accuracy", "Аккуратность");
		enchantsName.put("MoreExp", "Опытный");
		enchantsName.put("VoidProtection", "Пустотная защита");
		enchantsName.put("SkullBeater", "Выбиватель черепов");
		enchantsName.put("Vampire", "Вампиризм");
		enchantsName.put("FireHook", "Огненный поплавок");
		enchantsName.put("MovingHook", "Крюк");
		enchantsName.put("LuckPlus", "Везучий рыбак");
	}
}
