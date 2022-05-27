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
	static String enchantDescription = ChatColor.YELLOW+"Перетащите книгу на предмет, чтобы зачаровать его.";
	
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
		ItemStack item = makeCustomItem("mobBottle", ChatColor.YELLOW+"Пустотная бутылочка", Material.POTION, new String[] {
				ChatColor.GRAY+"Используя магические свойства пустоты",
				ChatColor.GRAY+"и кусочка аметиста эта бутылочка позволяет",
				ChatColor.GRAY+"заточать в себя почти любых существ,",
				ChatColor.GRAY+"что можно использовать для их перемещения.",
				ChatColor.GRAY+"К сожалению после первого использования",
				ChatColor.GRAY+"магические свойства меняются и бутылочка",
				ChatColor.GRAY+"испаряется.",
				"",
				ChatColor.BLUE+"ПКМ по мобу"});
		items.put("mobBottle", item);
		
		item = makeCustomItem("enchantLumberjack", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"Для топора",
						ChatColor.DARK_AQUA+"Срубает целое дерево"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Axe});
		items.put("enchantLumberjack", item);
		
		item = makeCustomItem("enchantAirbag", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"Для элитр",
						ChatColor.DARK_AQUA+"Не позволяет владельцу умереть от падения"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Elytra});
		items.put("enchantAirbag", item);
		
		item = makeCustomItem("enchantTunnel", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription, "",
						ChatColor.DARK_AQUA+"Для кирки",
						ChatColor.DARK_AQUA+"Позволяет копать 3x3 блока"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Pickaxe});
		items.put("enchantTunnel", item);
		
		item = makeCustomItem("enchantAccuracy", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription, "",
						ChatColor.DARK_AQUA+"Для кирки",
						ChatColor.DARK_AQUA+"Позволяет собирать некоторые блоки"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Pickaxe});
		items.put("enchantAccuracy", item);
		
		item = makeCustomItem("enchantMoreExp", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"Для меча",
						ChatColor.DARK_AQUA+"Есть шанс получить немного опыта",
						ChatColor.DARK_AQUA+"во время битвы"});
		item = CustomEnchants.setEnchantTags(item, 3, new ItemType[] {ItemType.Sword});
		items.put("enchantMoreExp", item);
		
		item = makeCustomItem("enchantSkullBeater", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"Для меча",
						ChatColor.DARK_AQUA+"Повышеный шанс выбить череп скелета иссушителя"
						});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Sword});
		items.put("enchantSkullBeater", item);
		
		item = makeCustomItem("enchantVampire", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"Для меча",
						ChatColor.DARK_AQUA+"Есть шанс восстановить немного хп во время битвы"
						});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Sword});
		items.put("enchantVampire", item);
		
		item = makeCustomItem("enchantVoidProtection", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"Для брони",
						ChatColor.DARK_AQUA+"Сводит эндерменов с ума"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.Armor});
		items.put("enchantVoidProtection", item);
		
		item = makeCustomItem("enchantFireHook", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"Для удочки",
						ChatColor.DARK_AQUA+"Автоматически плавит ресурсы во время рыбалки"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.FishingRod});
		items.put("enchantFireHook", item);
		
		item = makeCustomItem("enchantMovingHook", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"Для удочки",
						ChatColor.DARK_AQUA+"Позволяет притягиваться к поплавку"});
		item = CustomEnchants.setEnchantTags(item, 1, new ItemType[] {ItemType.FishingRod});
		items.put("enchantMovingHook", item);
		
		item = makeCustomItem("enchantLuckPlus", ChatColor.YELLOW+"Чародейская книга", Material.ENCHANTED_BOOK,
				new String[] {"",enchantDescription,"",
						ChatColor.DARK_AQUA+"Для удочки",
						ChatColor.DARK_AQUA+"Снижает шансы поймать бесполезный хлам"});
		item = CustomEnchants.setEnchantTags(item, 10, new ItemType[] {ItemType.FishingRod});
		items.put("enchantLuckPlus", item);
		
		item = makeCustomItem("customExpBottle", ChatColor.YELLOW+"Опыт жизни", Material.DRAGON_BREATH,
				new String[] {
						ChatColor.GRAY+"Хранит 100 единиц вашего",
						ChatColor.GRAY+"опыта, чтобы в дальнейшем",
						ChatColor.GRAY+"его можно было использовать",
						ChatColor.GRAY+"вновь"
						});
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("customExpBottle", item);
		
		item = makeCustomItem("customExpBottle2", ChatColor.YELLOW+"Опыт жизни", Material.DRAGON_BREATH,
				new String[] {
						ChatColor.GRAY+"Хранит 500 единиц вашего",
						ChatColor.GRAY+"опыта, чтобы в дальнейшем",
						ChatColor.GRAY+"его можно было использовать",
						ChatColor.GRAY+"вновь"
						});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("customExpBottle2", item);
		
		item = makeCustomItem("voidEye", ChatColor.DARK_GRAY+"Пустотное око", Material.ENDER_EYE, new String[] {ChatColor.GRAY+"Используйте на алтаре для призыва дракона."});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("voidEye", item);
		
		item = makeCustomItem("voidCrystal1", ChatColor.LIGHT_PURPLE+"Пустотный кристалл 1", Material.MEDIUM_AMETHYST_BUD, new String[] {ChatColor.GRAY+"Выпадает с дркона 1-го уровня", ChatColor.GRAY+"Используется в крафтах"});
		items.put("voidCrystal1", item);
		
		item = makeCustomItem("voidCrystal2", ChatColor.LIGHT_PURPLE+"Пустотный кристалл 2", Material.LARGE_AMETHYST_BUD, new String[] {ChatColor.GRAY+"Выпадает с дркона 2-го уровня", ChatColor.GRAY+"Используется в крафтах"});
		items.put("voidCrystal2", item);
		
		item = makeCustomItem("voidCrystal3", ChatColor.LIGHT_PURPLE+"Пустотный кристалл 3", Material.AMETHYST_CLUSTER, new String[] {ChatColor.GRAY+"Выпадает с дркона 3-го уровня", ChatColor.GRAY+"Используется в крафтах"});
		items.put("voidCrystal3", item);
		
		item = makeCustomItem("strangeHeart", ChatColor.YELLOW+"Странное сердце", Material.HEART_OF_THE_SEA, new String[] {ChatColor.GRAY+"Кажется притягивает к себе предметы"});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("strangeHeart", item);
		
		item = makeCustomItem("hopperFilter", ChatColor.RED+"Фильтр для воронок", Material.PAPER, new String[] {
				ChatColor.GRAY+"Позволяет легко и быстро настроить фильтрацию для воронок",
				ChatColor.GRAY+"ПКМ по воронке, чтобы применить фильтр",
				ChatColor.GRAY+"Используйте "+ChatColor.AQUA+"Верстак фильтров "+ChatColor.GRAY+"для настройки"
				});
		items.put("hopperFilter", item);
		
		item = makeCustomItem("filterWorkbench", ChatColor.AQUA+"Верстак фильтров", Material.LECTERN, new String[] {});
		items.put("filterWorkbench", item);
		
		item = makeCustomItem("eternalCoal", ChatColor.RED+"Вечный уголек", Material.COAL, new String[] {ChatColor.GRAY+"С шансом 0.4% можно получить при торговле с пиглином", ChatColor.GRAY+"Он никогда не погаснет", ChatColor.GRAY+"Используется в крафтах"});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("eternalCoal", item);
		
		item = makeCustomItem("defectiveChorus", ChatColor.WHITE+"Дефектный плод хоруса", Material.CHORUS_FRUIT, new String[] {ChatColor.GRAY+"Можно найти в городе Энда", ChatColor.GRAY+"Имеет непонятные свойства", ChatColor.GRAY+"Используется в крафтах"});
		items.put("defectiveChorus", item);
		
		item = makeCustomItem("enchPrismarineShard", ChatColor.WHITE+"Зачарованый осколок призмарина", Material.PRISMARINE_SHARD, new String[] {ChatColor.GRAY+"Выпадает со стражников", ChatColor.GRAY+"Используется в крафтах"});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("enchPrismarineShard", item);
		
		item = makeCustomItem("decoyOfDeath", ChatColor.RED+"Приманка смерти", Material.GOLDEN_CARROT, new String[] {ChatColor.GRAY+"Притяните удочкой в воде"});
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		item.setItemMeta(meta);
		items.put("decoyOfDeath", item);
	}
}
