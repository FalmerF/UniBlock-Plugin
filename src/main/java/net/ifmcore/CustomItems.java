package net.ifmcore;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import net.ifmcore.CustomEnchants.ItemType;

public class CustomItems {
	public static Map<String, ItemStack> items = new TreeMap<String, ItemStack>();
	public static List<EntityType> mobBottleBlackList = Arrays.asList(new EntityType[] {EntityType.PAINTING, EntityType.ENDER_CRYSTAL, EntityType.ITEM_FRAME, EntityType.ARMOR_STAND, EntityType.MINECART, EntityType.MINECART_CHEST, EntityType.MINECART_COMMAND, EntityType.MINECART_FURNACE, EntityType.MINECART_HOPPER, EntityType.MINECART_MOB_SPAWNER, EntityType.MINECART_TNT, EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.PLAYER, EntityType.BOAT});
	public static List<EntityType> spawnerWhiteList = Arrays.asList(new EntityType[] {EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CAVE_SPIDER});
	static String enchantDescription = ChatColor.YELLOW+"���������� ����� �� �������, ����� ���������� ���.";
	
	public static boolean isCustomItem(ItemStack item) {
		return NBTEditor.contains(item, "isCustomItem") ? NBTEditor.getBoolean(item, "isCustomItem") : false;
	}
	
	public static String getCustomItemName(ItemStack item) {
		return NBTEditor.contains(item, "isCustomItem") ? NBTEditor.getString(item, "customItemName") : "";
	}
	
	public static ItemStack makeCustomItem(String name, String displayName, Material material, String[] lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(Arrays.asList(lore));
		itemMeta.setDisplayName(displayName);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(itemMeta);
		item = NBTEditor.set(item, true, "isCustomItem");
		item = NBTEditor.set(item, name, "customItemName");
		return item;
	}
	
	public static ItemStack getCustomItem(String name, int amount) {
		ItemStack item = items.getOrDefault(name, null);
		if(item != null) {
			item = item.clone();
			item.setAmount(amount);
		}
		return item;
	}
	
	static {
		ItemStack item = makeCustomItem("mobBottle", ChatColor.YELLOW+"��������� ���������", Material.POTION, new String[] {
				ChatColor.GRAY+"��������� ���������� �������� �������",
				ChatColor.GRAY+"� ������� �������� ��� ��������� ���������",
				ChatColor.GRAY+"�������� � ���� ����� ����� �������,",
				ChatColor.GRAY+"��� ����� ������������ ��� �� �����������.",
				ChatColor.GRAY+"� ��������� ����� ������� �������������",
				ChatColor.GRAY+"���������� �������� �������� � ���������",
				ChatColor.GRAY+"����������.",
				"",
				ChatColor.BLUE+"��� �� ����"});
		items.put("mobBottle", item);
		
		item = makeCustomItem("enchantLumberjack", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"��� ������",
						ChatColor.DARK_AQUA+"������� ����� ������"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Axe});
		items.put("enchantLumberjack", item);
		
		item = makeCustomItem("enchantAirbag", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"��� �����",
						ChatColor.DARK_AQUA+"�� ��������� ��������� ������� �� �������"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Elytra});
		items.put("enchantAirbag", item);
		
		item = makeCustomItem("enchantTunnel", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription, "",
						ChatColor.DARK_AQUA+"��� �����",
						ChatColor.DARK_AQUA+"��������� ������ 3x3 �����"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Pickaxe});
		items.put("enchantTunnel", item);
		
		item = makeCustomItem("enchantAccuracy", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription, "",
						ChatColor.DARK_AQUA+"��� �����",
						ChatColor.DARK_AQUA+"��������� �������� ��������� �����"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Pickaxe});
		items.put("enchantAccuracy", item);
		
		item = makeCustomItem("enchantMoreExp", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"��� ����",
						ChatColor.DARK_AQUA+"���� ���� �������� ������� �����",
						ChatColor.DARK_AQUA+"�� ����� �����"});
		item = CustomEnchants.setEnchantTags(item, 3, new ItemType[] {ItemType.Sword});
		items.put("enchantMoreExp", item);
		
		item = makeCustomItem("enchantSkullBeater", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"��� ����",
						ChatColor.DARK_AQUA+"��������� ���� ������ ����� ������� ����������"
						});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Sword});
		items.put("enchantSkullBeater", item);
		
		item = makeCustomItem("enchantVampire", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"��� ����",
						ChatColor.DARK_AQUA+"���� ���� ������������ ������� �� �� ����� �����"
						});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Sword});
		items.put("enchantVampire", item);
		
		item = makeCustomItem("enchantVoidProtection", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"��� �����",
						ChatColor.DARK_AQUA+"������ ���������� � ���"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Armor});
		items.put("enchantVoidProtection", item);
		
		item = makeCustomItem("enchantFireHook", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"��� ������",
						ChatColor.DARK_AQUA+"������������� ������ ������� �� ����� �������"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.FishingRod});
		items.put("enchantFireHook", item);
		
		item = makeCustomItem("enchantMovingHook", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"��� ������",
						ChatColor.DARK_AQUA+"��������� ������������� � ��������"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.FishingRod});
		items.put("enchantMovingHook", item);
		
		item = makeCustomItem("enchantLuckPlus", ChatColor.YELLOW+"����������� �����", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"��� ������",
						ChatColor.DARK_AQUA+"������� ����� ������� ����������� ����"});
		item = CustomEnchants.setEnchantTags(item, 10, new ItemType[] {ItemType.FishingRod});
		items.put("enchantLuckPlus", item);
		
		item = makeCustomItem("customExpBottle", ChatColor.YELLOW+"���� �����", Material.DRAGON_BREATH,
				new String[] {
						ChatColor.GRAY+"������ 100 ������ ������",
						ChatColor.GRAY+"�����, ����� � ����������",
						ChatColor.GRAY+"��� ����� ���� ������������",
						ChatColor.GRAY+"�����"
						});
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("customExpBottle", item);
		
		item = makeCustomItem("customExpBottle2", ChatColor.YELLOW+"���� �����", Material.DRAGON_BREATH,
				new String[] {
						ChatColor.GRAY+"������ 500 ������ ������",
						ChatColor.GRAY+"�����, ����� � ����������",
						ChatColor.GRAY+"��� ����� ���� ������������",
						ChatColor.GRAY+"�����"
						});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("customExpBottle2", item);
		
		item = makeCustomItem("voidEye", ChatColor.DARK_GRAY+"��������� ���", Material.ENDER_EYE, new String[] {ChatColor.GRAY+"����������� �� ������ ��� ������� �������."});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("voidEye", item);
		
		item = makeCustomItem("voidCrystal1", ChatColor.LIGHT_PURPLE+"��������� �������� 1", Material.MEDIUM_AMETHYST_BUD, new String[] {ChatColor.GRAY+"�������� � ������ 1-�� ������", ChatColor.GRAY+"������������ � �������"});
		items.put("voidCrystal1", item);
		
		item = makeCustomItem("voidCrystal2", ChatColor.LIGHT_PURPLE+"��������� �������� 2", Material.LARGE_AMETHYST_BUD, new String[] {ChatColor.GRAY+"�������� � ������ 2-�� ������", ChatColor.GRAY+"������������ � �������"});
		items.put("voidCrystal2", item);
		
		item = makeCustomItem("voidCrystal3", ChatColor.LIGHT_PURPLE+"��������� �������� 3", Material.AMETHYST_CLUSTER, new String[] {ChatColor.GRAY+"�������� � ������ 3-�� ������", ChatColor.GRAY+"������������ � �������"});
		items.put("voidCrystal3", item);
		
		item = makeCustomItem("strangeHeart", ChatColor.YELLOW+"�������� ������", Material.HEART_OF_THE_SEA, new String[] {ChatColor.GRAY+"������� ����������� � ���� ��������"});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("strangeHeart", item);
		
		item = makeCustomItem("hopperFilter", ChatColor.RED+"������ ��� �������", Material.PAPER, new String[] {
				ChatColor.GRAY+"��������� ����� � ������ ��������� ���������� ��� �������",
				ChatColor.GRAY+"��� �� �������, ����� ��������� ������",
				ChatColor.GRAY+"����������� "+ChatColor.AQUA+"������� �������� "+ChatColor.GRAY+"��� ���������"
				});
		items.put("hopperFilter", item);
		
		item = makeCustomItem("filterWorkbench", ChatColor.AQUA+"������� ��������", Material.LECTERN, new String[] {});
		items.put("filterWorkbench", item);
		
		item = makeCustomItem("eternalCoal", ChatColor.RED+"������ ������", Material.COAL, new String[] {ChatColor.GRAY+"� ������ 0.4% ����� �������� ��� �������� � ��������", ChatColor.GRAY+"�� ������� �� ��������", ChatColor.GRAY+"������������ � �������"});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("eternalCoal", item);
		
		item = makeCustomItem("defectiveChorus", ChatColor.WHITE+"��������� ���� ������", Material.CHORUS_FRUIT, new String[] {ChatColor.GRAY+"����� ����� � ������ ����", ChatColor.GRAY+"����� ���������� ��������", ChatColor.GRAY+"������������ � �������"});
		items.put("defectiveChorus", item);
		
		item = makeCustomItem("enchPrismarineShard", ChatColor.WHITE+"����������� ������� ����������", Material.PRISMARINE_SHARD, new String[] {ChatColor.GRAY+"�������� �� ����������", ChatColor.GRAY+"������������ � �������"});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("enchPrismarineShard", item);
		
		item = makeCustomItem("decoyOfDeath", ChatColor.RED+"�������� ������", Material.GOLDEN_CARROT, new String[] {ChatColor.GRAY+"��������� ������� � ����"});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("decoyOfDeath", item);
	}
}
