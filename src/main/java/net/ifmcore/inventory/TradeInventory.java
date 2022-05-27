package net.ifmcore.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.reflect.Reflection;

import net.ifmcore.Uni;
import net.ifmcore.ItemUtils;
import net.ifmcore.Localization;

public class TradeInventory implements Listener {
	public static ItemStack acceptedTradeSlotItem;
	public static ItemStack readyAcceptTradeSlotItem;
	public static ItemStack notAcceptedTradeSlotItem;
	public static ItemStack acceptTradeSlotItem;
	public static ItemStack emptyInventorySlotItem;
	public static ItemStack commissionDiamond;
	
	public Inventory inventory1;
	public Inventory inventory2;
	public Player player1;
	public Player player2;
	int taskId;
	boolean hasChanges = false;
	
	int acceptLevel1 = 0;
	int acceptLevel2 = 0;
	
	public TradeInventory(Player player1, Player player2) {
		inventory1 = Bukkit.createInventory(null, 45, "Обмен с игроком " + player2.getName());
		inventory2 = Bukkit.createInventory(null, 45, "Обмен с игроком " + player1.getName());
		makeInventory(inventory1);
		makeInventory(inventory2);
		Bukkit.getServer().getPluginManager().registerEvents(this, Uni.instance);
		this.player1 = player1;
		this.player2 = player2;
		player1.openInventory(inventory1);
		player2.openInventory(inventory2);
		
		taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, new  Runnable(){
			public void run(){
				Update();
			}
		}, 5l, 5l);
	}
	
	void makeInventory(Inventory inv){
		for(int i = 18; i < 27; i++)
			inv.setItem(i, emptyInventorySlotItem);
		//inv.setItem(19, createMoneyItem(0, true));
		inv.setItem(21, notAcceptedTradeSlotItem);
		inv.setItem(22, acceptTradeSlotItem);
		inv.setItem(23, notAcceptedTradeSlotItem);
		//inv.setItem(25, createMoneyItem(0, false));
	}
	
	@EventHandler
    public void inventoryClick(InventoryClickEvent event)
	{
		if(event.getView().getTopInventory() == inventory1 || event.getView().getTopInventory() == inventory2) {
			Inventory inv = event.getClickedInventory();
			if(inv == inventory1 || inv == inventory2) {
				if(event.getSlot() == 22) {
					if(event.isLeftClick())
						acceptTrade(inv, true);
					else if(event.isRightClick())
						acceptTrade(inv, false);
					event.setCancelled(true);
				}
				else if(event.getSlot() < 27) {
					event.setCancelled(true);
				}
				else {
					acceptTrade(inv, false);
					hasChanges = true;
				}
				
			}
			else if(event.isShiftClick())
				event.setCancelled(true);
		}
	}
	
	@EventHandler
    public void inventoryDrag(InventoryDragEvent event)
	{
		if(event.getView().getTopInventory() == inventory1 || event.getView().getTopInventory() == inventory2) {
			for(int i : event.getInventorySlots()) {
				if(i < 27) {
					event.setCancelled(true);
					return;
				}
			}
			acceptTrade(event.getView().getTopInventory(), false);
		}
	}
	
	void acceptTrade(Inventory inv, boolean accept) {
		if(inv == inventory1) {
			if(accept) {
				if(!hasDiamondsToTrade(false)) {
					player1.sendMessage(Localization.parse("message.trade.nodiamonds"));
					acceptLevel1 = 0;
				}
				else if(acceptLevel1 == 0) {
					acceptLevel1 = 1;
				}
				else if(acceptLevel1 == 1 && acceptLevel2 >= 1) {
					acceptLevel1 = 2;
				}
			}
			else {
				acceptLevel1 = 0;
			}
		}
		else if(inv == inventory2) {
			if(accept) {
				if(!hasDiamondsToTrade(true)) {
					player2.sendMessage(Localization.parse("message.trade.nodiamonds"));
					acceptLevel2 = 0;
				}
				else if(acceptLevel2 == 0) {	
					acceptLevel2 = 1;
				}
				else if(acceptLevel2 == 1 && acceptLevel1 >= 1) {
					acceptLevel2 = 2;
				}
			}
			else {
				acceptLevel2 = 0;
			}
		}
		
		if(acceptLevel1 == 2 && acceptLevel2 == 2) {
			completeTrade();
		}
	}
	
	int needDiamondsToTrade(Inventory inventory) {
		int itemsToTrade = 0;
		for(int i = 0; i < 18; i++) {
			ItemStack item = inventory.getItem(27+i);
			if(item != null && item.getType() != Material.AIR)
				itemsToTrade++;
		}
		return itemsToTrade;
	}
	
	boolean hasDiamondsToTrade(boolean secondPlayer) {
		Player player = secondPlayer ? player2 : player1;
		Inventory inventory = secondPlayer ? inventory2 : inventory1;
		int diamonds = 0;
		for(ItemStack item : player.getInventory().getContents()) {
			if(item != null && item.getType() == Material.DIAMOND)
				diamonds += item.getAmount();
		}
		
		return diamonds >= needDiamondsToTrade(inventory);
	}
	
	void updateDiamonds(Inventory inv) {
		int count = needDiamondsToTrade(inv);
		if(count == 0)
			inv.setItem(18, ItemUtils.emptyInventorySlotItem);
		else {
			ItemStack item = commissionDiamond.clone();
			item.setAmount(needDiamondsToTrade(inv));
			inv.setItem(18, item);
		}
	}
	
	void completeTrade() {
		ItemUtils.removeMultiplyItem(player1.getInventory(), new ItemStack(Material.DIAMOND, needDiamondsToTrade(inventory1)));
		ItemUtils.removeMultiplyItem(player2.getInventory(), new ItemStack(Material.DIAMOND, needDiamondsToTrade(inventory2)));
		for(int i = 0; i < 18; i++) {
			if(inventory2.getItem(27+i) != null) {
				ItemUtils.givePlayerItem(player1, inventory2.getItem(27+i));
			}
			if(inventory1.getItem(27+i) != null)
				ItemUtils.givePlayerItem(player2, inventory1.getItem(27+i));
		}
		player1.closeInventory();
		player2.closeInventory();
		player1.sendTitle(ChatColor.GREEN+"Обмен завершен", null);
		player2.sendTitle(ChatColor.GREEN+"Обмен завершен", null);
		unregister();
	}
	
	void unregister() {
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryDragEvent.getHandlerList().unregister(this);
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}
	
	void backItemsToPlayers() {
		for(int i = 0; i < 18; i++) {
			if(inventory1.getItem(27+i) != null)
				ItemUtils.givePlayerItem(player1, inventory1.getItem(27+i));
			if(inventory2.getItem(27+i) != null)
				ItemUtils.givePlayerItem(player2, inventory2.getItem(27+i));
		}
	}
	
	void Update() {
		if(hasChanges) {
			for(int i = 0; i < 18; i++) {
				inventory1.setItem(i, inventory2.getItem(27+i));
				inventory2.setItem(i, inventory1.getItem(27+i));
			}
			hasChanges = false;
		}
		if(player1.getOpenInventory().getTopInventory() != inventory1) {
			player2.closeInventory();
			player2.sendTitle(ChatColor.RED+"Игрок отменил обмен", null);
			backItemsToPlayers();
			unregister();
			return;
		}
		else if(player2.getOpenInventory().getTopInventory() != inventory2) {
			player1.closeInventory();
			player1.sendTitle(ChatColor.RED+"Игрок отменил обмен", null);
			backItemsToPlayers();
			unregister();
			return;
		}
		updateDiamonds(inventory1);
		updateDiamonds(inventory2);
		updateAcceptStatus();
	}
	
	void updateAcceptStatus() {
		ItemStack item = null;
		if(acceptLevel1 == 0)
			item = notAcceptedTradeSlotItem;
		else if(acceptLevel1 == 1)	
			item = readyAcceptTradeSlotItem;
		else if(acceptLevel1 == 2)
			item = acceptedTradeSlotItem;
		inventory1.setItem(21, item);
		inventory2.setItem(23, item);
		
		if(acceptLevel2 == 0)
			item = notAcceptedTradeSlotItem;
		else if(acceptLevel2 == 1)	
			item = readyAcceptTradeSlotItem;
		else if(acceptLevel2 == 2)
			item = acceptedTradeSlotItem;
		inventory1.setItem(23, item);
		inventory2.setItem(21, item);
	}
	
	ItemStack createMoneyItem(float money, boolean self) {
		ItemStack item = new ItemStack(Material.SUNFLOWER, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(ChatColor.GOLD+""+money+" монет");
		List<String> desc = new ArrayList<String>();
		if(self) {
			desc.add(ChatColor.GREEN+"Вы передаете");
			desc.add("");
			desc.add(ChatColor.YELLOW+"Нажмите чтобы добавить монеты");
		}
		else
			desc.add(ChatColor.GREEN+"Вы получите");
			
		itemMeta.setLore(desc);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	static {
		acceptedTradeSlotItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
		ItemMeta itemMeta = acceptedTradeSlotItem.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(ChatColor.GREEN+"Согласен");
		acceptedTradeSlotItem.setItemMeta(itemMeta);
		
		readyAcceptTradeSlotItem = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
		itemMeta = readyAcceptTradeSlotItem.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(ChatColor.YELLOW+"Готов");
		readyAcceptTradeSlotItem.setItemMeta(itemMeta);
		
		notAcceptedTradeSlotItem = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		itemMeta = notAcceptedTradeSlotItem.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(ChatColor.RED+"Не готов");
		notAcceptedTradeSlotItem.setItemMeta(itemMeta);
		
		acceptTradeSlotItem = new ItemStack(Material.PAPER, 1);
		itemMeta = acceptTradeSlotItem.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(ChatColor.GREEN+"Нажмите, чтобы принять обмен");
		List<String> desc = new ArrayList<String>();
		desc.add("");
		desc.add(ChatColor.YELLOW+"Нажмите ЛКМ, чтобы принять обмен");
		desc.add(ChatColor.YELLOW+"Нажмите ПКМ, чтобы отменить обмен");
		itemMeta.setLore(desc);
		acceptTradeSlotItem.setItemMeta(itemMeta);
		
		emptyInventorySlotItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		itemMeta = emptyInventorySlotItem.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(" ");
		emptyInventorySlotItem.setItemMeta(itemMeta);
		
		commissionDiamond = new ItemStack(Material.DIAMOND, 1);
		itemMeta = commissionDiamond.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.setDisplayName(ChatColor.AQUA+"Комиссия");
		commissionDiamond.setItemMeta(itemMeta);
	}
}
