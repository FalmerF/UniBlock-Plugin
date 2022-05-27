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

import net.ifmcore.ItemUtils;

public class GuideInventory implements Listener {
	static ItemStack nextItem;
	static ItemStack previousItem;
	static List<Inventory> pagesInventory = new ArrayList<Inventory>();
	public static GuideInventory instance;
	
	public GuideInventory() {
		instance = this;
		makePage1();
		makePage2();
		makePage3();
	}
	
	public static void makePage1() {
		Inventory inventory = Bukkit.createInventory(null, 45, "Базовая информация");
		
		ItemStack item = ItemUtils.setItemTags(new ItemStack(Material.RED_BED));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"/home");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Эта команда позволяет телепортироваться на",
				ChatColor.GRAY+"точку возрождения (Например: кровать)"
				}));
		inventory.setItem(10, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.COMPARATOR));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"/settings");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Эта команда позволяет настроить некоторые",
				ChatColor.GRAY+"игровые элементы"
				}));
		inventory.setItem(12, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.CRAFTING_TABLE));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"/recipes");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Эта команда позволяет посмотреть список",
				ChatColor.GRAY+"всех кастомных рецептов"
				}));
		inventory.setItem(14, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.EMERALD));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"/trade");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Эта команда позволяет торговаться с",
				ChatColor.GRAY+"игроками"
				}));
		inventory.setItem(16, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.AXOLOTL_BUCKET));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Значки");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Вы можете получить различные значки",
				ChatColor.GRAY+"участвуя в различных ивентах или",
				ChatColor.GRAY+"выполняя сложные достижения.",
				ChatColor.GRAY+"Значки будут отображаться перед",
				ChatColor.GRAY+"вашим ником в чате и табе"
				}));
		inventory.setItem(28, item);
		
		inventory.setItem(36, previousItem);
		inventory.setItem(44, nextItem);
		
		pagesInventory.add(inventory);
	}
	
	public static void makePage2() {
		Inventory inventory = Bukkit.createInventory(null, 45, "Дракон и Зачарования");
		
		ItemStack item = ItemUtils.setItemTags(new ItemStack(Material.ENCHANTED_BOOK));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Зачарования");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"На сервере присутствуют кастомные",
				ChatColor.GRAY+"зачарования для предметов.",
				ChatColor.GRAY+"Получить их можно как крафтами,",
				ChatColor.GRAY+"так и торговлей с жителями"
				}));
		inventory.setItem(10, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.VILLAGER_SPAWN_EGG));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Житель");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Когда житель получает проффесию",
				ChatColor.GRAY+"библиотекаря, то есть 5% шанс,",
				ChatColor.GRAY+"что он будет особенным, с",
				ChatColor.GRAY+"кастомными книгами зачарований"
				}));
		inventory.setItem(12, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.PAPER));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Больше про зачарования");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Хоть зачарования и дорогие, но",
				ChatColor.GRAY+"они достаточно полезны в",
				ChatColor.GRAY+"повседневной игре"
				}));
		inventory.setItem(14, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.ENDER_PEARL));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Особый дракон");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Используя Пустотное Око (рецепт",
				ChatColor.GRAY+"можно посмотреть /recipes)",
				ChatColor.GRAY+"можно призвать особого, усиленного",
				ChatColor.GRAY+"Эндер-дракона"
				}));
		inventory.setItem(28, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.ENDER_EYE));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Особый дракон");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Дракон имеет три уровня сложности:",
				ChatColor.GRAY+" 1-й уровень - 4 пустотных око",
				ChatColor.GRAY+" 2-й уровень - 8 пустотных око",
				ChatColor.GRAY+" 3-й уровень - 16 пустотных око",
				ChatColor.GRAY+"Для призыва необходимо разместить",
				ChatColor.GRAY+"Пустотное Око на алтаре в эндер-мире,",
				ChatColor.GRAY+"после чего нажать ПКМ по кафедре",
				ChatColor.GRAY+"в центре алтаря"
				}));
		inventory.setItem(30, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.DRAGON_EGG));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Особый дракон");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Мало того, что дракон имеет",
				ChatColor.GRAY+"больше хп (300, 500 и 750 хп",
				ChatColor.GRAY+"соответственно), он еще и будет",
				ChatColor.GRAY+"призывать мобов, телепортировать",
				ChatColor.GRAY+"вас и накладывать негативные эффекты"
				}));
		inventory.setItem(32, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.END_PORTAL_FRAME));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Особый дракон");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Рекомендуется убивать дракона",
				ChatColor.GRAY+"с командой, одному будет",
				ChatColor.GRAY+"достаточно сложно, удачи!"
				}));
		inventory.setItem(34, item);
		
		inventory.setItem(36, previousItem);
		inventory.setItem(44, nextItem);
		
		pagesInventory.add(inventory);
	}
	
	public static void makePage3() {
		Inventory inventory = Bukkit.createInventory(null, 45, "Рыбалка");
		
		ItemStack item = ItemUtils.setItemTags(new ItemStack(Material.FISHING_ROD));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Рыбалка");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Время ловли рыбы значительно",
				ChatColor.GRAY+"сокращено и получаемый лут",
				ChatColor.GRAY+"улучшен, но во время рыбалки",
				ChatColor.GRAY+"вы будите получать негативные",
				ChatColor.GRAY+"эффекты и вылавливать мобов."
				}));
		inventory.setItem(10, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.PRISMARINE_SHARD));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Босс");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Доступен новый морской босс!",
				ChatColor.GRAY+"Призвать его можно с помощью",
				ChatColor.RED+"Приманки смерти",
				ChatColor.GRAY+"Посмотреть крафт можно командой",
				ChatColor.GRAY+"/recipes",
				}));
		inventory.setItem(12, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.ENCHANTED_BOOK));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"Зачарования");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"Два новых зачарования, одно",
				ChatColor.GRAY+"из которых можно получить",
				ChatColor.GRAY+"во время рыбалки, а второе",
				ChatColor.GRAY+"убив босса"
				}));
		inventory.setItem(14, item);
		
		inventory.setItem(36, previousItem);
		inventory.setItem(44, nextItem);
		
		pagesInventory.add(inventory);
	}
	
	public static void openPage(Player player, int index) {
		index = Math.max(Math.min(index, pagesInventory.size()-1), 0);
		player.openInventory(pagesInventory.get(index));
	}
	
	@EventHandler
    public void inventoryClick(InventoryClickEvent event)
	{
		Inventory inventory = event.getView().getTopInventory();
		if(pagesInventory.contains(inventory)) {
			event.setCancelled(true);
			Inventory clickedInventory = event.getClickedInventory();
			if(clickedInventory == inventory) {
				if(event.getSlot() == 36) {
					openPage((Player) event.getWhoClicked(), pagesInventory.indexOf(inventory)-1);
				}
				else if(event.getSlot() == 44) {
					openPage((Player) event.getWhoClicked(), pagesInventory.indexOf(inventory)+1);
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
