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
		Inventory inventory = Bukkit.createInventory(null, 45, "������� ����������");
		
		ItemStack item = ItemUtils.setItemTags(new ItemStack(Material.RED_BED));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"/home");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"��� ������� ��������� ����������������� ��",
				ChatColor.GRAY+"����� ����������� (��������: �������)"
				}));
		inventory.setItem(10, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.COMPARATOR));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"/settings");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"��� ������� ��������� ��������� ���������",
				ChatColor.GRAY+"������� ��������"
				}));
		inventory.setItem(12, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.CRAFTING_TABLE));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"/recipes");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"��� ������� ��������� ���������� ������",
				ChatColor.GRAY+"���� ��������� ��������"
				}));
		inventory.setItem(14, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.EMERALD));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"/trade");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"��� ������� ��������� ����������� �",
				ChatColor.GRAY+"��������"
				}));
		inventory.setItem(16, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.AXOLOTL_BUCKET));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"�� ������ �������� ��������� ������",
				ChatColor.GRAY+"�������� � ��������� ������� ���",
				ChatColor.GRAY+"�������� ������� ����������.",
				ChatColor.GRAY+"������ ����� ������������ �����",
				ChatColor.GRAY+"����� ����� � ���� � ����"
				}));
		inventory.setItem(28, item);
		
		inventory.setItem(36, previousItem);
		inventory.setItem(44, nextItem);
		
		pagesInventory.add(inventory);
	}
	
	public static void makePage2() {
		Inventory inventory = Bukkit.createInventory(null, 45, "������ � �����������");
		
		ItemStack item = ItemUtils.setItemTags(new ItemStack(Material.ENCHANTED_BOOK));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"�����������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"�� ������� ������������ ���������",
				ChatColor.GRAY+"����������� ��� ���������.",
				ChatColor.GRAY+"�������� �� ����� ��� ��������,",
				ChatColor.GRAY+"��� � ��������� � ��������"
				}));
		inventory.setItem(10, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.VILLAGER_SPAWN_EGG));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"����� ������ �������� ���������",
				ChatColor.GRAY+"������������, �� ���� 5% ����,",
				ChatColor.GRAY+"��� �� ����� ���������, �",
				ChatColor.GRAY+"���������� ������� �����������"
				}));
		inventory.setItem(12, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.PAPER));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"������ ��� �����������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"���� ����������� � �������, ��",
				ChatColor.GRAY+"��� ���������� ������� �",
				ChatColor.GRAY+"������������ ����"
				}));
		inventory.setItem(14, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.ENDER_PEARL));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"������ ������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"��������� ��������� ��� (������",
				ChatColor.GRAY+"����� ���������� /recipes)",
				ChatColor.GRAY+"����� �������� �������, ����������",
				ChatColor.GRAY+"�����-�������"
				}));
		inventory.setItem(28, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.ENDER_EYE));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"������ ������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"������ ����� ��� ������ ���������:",
				ChatColor.GRAY+" 1-� ������� - 4 ��������� ���",
				ChatColor.GRAY+" 2-� ������� - 8 ��������� ���",
				ChatColor.GRAY+" 3-� ������� - 16 ��������� ���",
				ChatColor.GRAY+"��� ������� ���������� ����������",
				ChatColor.GRAY+"��������� ��� �� ������ � �����-����,",
				ChatColor.GRAY+"����� ���� ������ ��� �� �������",
				ChatColor.GRAY+"� ������ ������"
				}));
		inventory.setItem(30, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.DRAGON_EGG));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"������ ������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"���� ����, ��� ������ �����",
				ChatColor.GRAY+"������ �� (300, 500 � 750 ��",
				ChatColor.GRAY+"��������������), �� ��� � �����",
				ChatColor.GRAY+"��������� �����, ���������������",
				ChatColor.GRAY+"��� � ����������� ���������� �������"
				}));
		inventory.setItem(32, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.END_PORTAL_FRAME));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"������ ������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"������������� ������� �������",
				ChatColor.GRAY+"� ��������, ������ �����",
				ChatColor.GRAY+"���������� ������, �����!"
				}));
		inventory.setItem(34, item);
		
		inventory.setItem(36, previousItem);
		inventory.setItem(44, nextItem);
		
		pagesInventory.add(inventory);
	}
	
	public static void makePage3() {
		Inventory inventory = Bukkit.createInventory(null, 45, "�������");
		
		ItemStack item = ItemUtils.setItemTags(new ItemStack(Material.FISHING_ROD));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"�������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"����� ����� ���� �����������",
				ChatColor.GRAY+"��������� � ���������� ���",
				ChatColor.GRAY+"�������, �� �� ����� �������",
				ChatColor.GRAY+"�� ������ �������� ����������",
				ChatColor.GRAY+"������� � ����������� �����."
				}));
		inventory.setItem(10, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.PRISMARINE_SHARD));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"����");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"�������� ����� ������� ����!",
				ChatColor.GRAY+"�������� ��� ����� � �������",
				ChatColor.RED+"�������� ������",
				ChatColor.GRAY+"���������� ����� ����� ��������",
				ChatColor.GRAY+"/recipes",
				}));
		inventory.setItem(12, item);
		
		item = ItemUtils.setItemTags(new ItemStack(Material.ENCHANTED_BOOK));
		ItemUtils.setItemName(item, ChatColor.YELLOW+"�����������");
		ItemUtils.setItemLore(item, Arrays.asList(new String[] {
				ChatColor.GRAY+"��� ����� �����������, ����",
				ChatColor.GRAY+"�� ������� ����� ��������",
				ChatColor.GRAY+"�� ����� �������, � ������",
				ChatColor.GRAY+"���� �����"
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
		itemMeta.setDisplayName(ChatColor.YELLOW+"������");
		nextItem.setItemMeta(itemMeta);
		
		previousItem = new ItemStack(Material.ARROW, 1);
		itemMeta = previousItem.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(ChatColor.YELLOW+"�����");
		previousItem.setItemMeta(itemMeta);
	}
}
