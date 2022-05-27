package net.ifmcore.listeners;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import net.ifmcore.LogUtils;
import net.ifmcore.Log4jFilter;

public class LogsListener implements Listener {
	static String getItemName(ItemStack item) {
		if(item == null)
			return "null";
		return new StringBuilder(item.getType().name()).append(" x").append(item.getAmount()).toString();
	}
	
	static String getItemsName(List<ItemStack> items) {
		String itemsString = "";
		for(ItemStack item : items) 
			itemsString += getItemName(item)+" ";
		return itemsString;
	}
	
	static String getItemsName(Map<Integer,ItemStack> items) {
		String itemsString = "";
		for(Entry<Integer, ItemStack> item : items.entrySet()) 
			itemsString += getItemName(item.getValue())+" ";
		return itemsString;
	}
	
	static String getBlockName(Block block) {
		if(block == null)
			return "null";
		Location loc = block.getLocation();
		return block.getType().name()+" "+getLoc(loc);
	}
	
	static String getEntityName(Entity entity) {
		if(entity == null)
			return "null";
		Location loc = entity.getLocation();
		return entity.getType().name()+" "+getLoc(loc);
	}
	
	static String getItemEntityName(Item item) {
		if(item == null)
			return "null";
		Location loc = item.getLocation();
		return getItemName(item.getItemStack())+" "+getLoc(loc);
	}
	
	static String getLoc(Location loc) {
		if(loc == null)
			return "null";
		return "("+loc.getWorld().getName()+", "+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+")";
	}
	
	static String getInventory(Inventory inv) {
		if(inv == null)
			return "null";
		String name = inv.getType().name();
		if(inv.getLocation() != null)
			name += " "+getLoc(inv.getLocation());
		return name;
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ����� � ����");
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������� ����");
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerKickEvent(PlayerKickEvent event) {
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������ � �������, �������: "+event.getReason());
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������� ����� ���� �� "+event.getNewGameMode().name());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" �������� ��������� � ���: "+event.getMessage());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
		LogUtils.sendLog("����� "+event.getName()+" ("+event.getAddress().getHostName()+") �������� ������������ � �������, ��������� "+event.getKickMessage());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ��������������� �� ������� ��� �����, ArmorStandItem: "+getItemName(event.getArmorStandItem())+"  PlayerItem: "+getItemName(event.getPlayerItem()));
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������� � ��� "+event.getPlayer().getWorld().getName());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled()) return;
		for(String c : Log4jFilter.notLoggableCommands)
			if(event.getMessage().startsWith("/"+c))
				return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ���������� ������� "+event.getMessage());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������� ������� "+getItemEntityName(event.getItemDrop()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerEditBookEvent(PlayerEditBookEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" �������������� �����, ����� ����������: "+event.getNewBookMeta().getAsString());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerExpChangeEvent(PlayerExpChangeEvent event) {
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������� ���������� ����� �� "+event.getAmount());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerHarvestBlockEvent(PlayerHarvestBlockEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������ ������ � "+getBlockName(event.getHarvestedBlock())+", ����: "+getItemsName(event.getItemsHarvested()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ��������������� � entity "+getEntityName(event.getRightClicked()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ��������������� � ������ "+getBlockName(event.getClickedBlock())+", ��������: "+event.getAction().name()+", ������� � �����: "+getItemName(event.getItem()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerItemBreakEvent(PlayerItemBreakEvent event) {
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������ ������� "+getItemName(event.getBrokenItem()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ����������� ������� "+getItemName(event.getItem()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent event) {
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������� ���� ������� �� "+event.getNewLevel());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" �������������� � "+getLoc(event.getFrom())+" �� "+getLoc(event.getTo()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ���������� �� ����������� "+getLoc(event.getRespawnLocation()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" �������� ������� "+getItemEntityName(event.getItem())+" �� ����� �������� "+event.getRemaining());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getWhoClicked().getName()+" ��������������� � ���������� "+getInventory(event.getClickedInventory())+", ������ ��������� "+getInventory(event.getView().getBottomInventory())+", ������� ��������� "+getInventory(event.getView().getTopInventory())+", �������� "+event.getAction().name()+", ������� "+getItemName(event.getCurrentItem())+", ������ "+getItemName(event.getCursor()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onInventoryDragEvent(InventoryDragEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getWhoClicked().getName()+" ��������������� � ���������� "+getInventory(event.getInventory())+", ������ ��������� "+getInventory(event.getView().getBottomInventory())+", ������� ��������� "+getInventory(event.getView().getTopInventory())+", �������� "+getItemsName(event.getNewItems())+", ������ "+getItemName(event.getCursor()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onInventoryCreativeEvent(InventoryCreativeEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getWhoClicked().getName()+" ��������������� � ���������� "+getInventory(event.getClickedInventory())+", ������ ��������� "+getInventory(event.getView().getBottomInventory())+", ������� ��������� "+getInventory(event.getView().getTopInventory())+", �������� "+event.getAction().name()+", ������� "+getItemName(event.getCurrentItem())+", ������ "+getItemName(event.getCursor()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" ������ ���� "+getBlockName(event.getBlock()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreakEvent(BlockPlaceEvent event) {
		if(event.isCancelled()) return;
		LogUtils.sendLog("����� "+event.getPlayer().getName()+" �������� ���� "+getBlockName(event.getBlock()));
	}
}
