package net.ifmcore.inventory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.ifmcore.Uni;
import net.ifmcore.data.PlayerData;
import net.ifmcore.inventory.InvSeeInventory;

public class SettingsInventory implements Listener {
	public Inventory inventory;
	public Player player;
	public PlayerData playerData;
	List<String> playerStatsDescription = new ArrayList<String>();
	List<String> playerHintsDescription = new ArrayList<String>();
	int page = 0;
	int maxPage = 0;
	int taskId;
	
	public SettingsInventory(Player player) {
		inventory = Bukkit.createInventory(null, 27, "Настройки");
		Bukkit.getServer().getPluginManager().registerEvents(this, Uni.instance);
		this.player = player;
		this.playerData = Uni.getPlayerData(player);
		
		playerStatsDescription.add(ChatColor.GRAY+"Отображение вашей статистики на экране");
		playerHintsDescription.add(ChatColor.GRAY+"Получение советов в чате");
		
		updateSettingsItem();
		for(int i = 0; i < 9; i++) inventory.setItem(i, InvSeeInventory.emptyInventorySlotItem);
		for(int i = 18; i < 27; i++) inventory.setItem(i, InvSeeInventory.emptyInventorySlotItem);
		
		player.openInventory(inventory);
		
		taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, new  Runnable(){
			public void run(){
				Update();
			}
		}, 5l, 20l);
	}
	
	void updateSettingsItem() {
		inventory.setItem(12, makeSettingItem(Material.PLAYER_HEAD, "Статистика", playerData.getBoolParam("showStatistic"), playerStatsDescription));
		inventory.setItem(14, makeSettingItem(Material.PAPER, "Советы", playerData.getBoolParam("chatHints"), playerHintsDescription));
	}
	
	ItemStack makeSettingItem(Material mat, String name, boolean enabled, List<String> description) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		meta.setDisplayName(ChatColor.WHITE+name);
		if(enabled)
			lore.add(MessageFormat.format("{0}Включено", ChatColor.GREEN));
		else
			lore.add(MessageFormat.format("{0}Выключено", ChatColor.RED));
		lore.add("");
		lore.addAll(description);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	@EventHandler
    public void inventoryClick(InventoryClickEvent event)
	{
		if(event.getView().getTopInventory() == inventory) {
			event.setCancelled(true);
			if(!event.isShiftClick()) {
				if(event.getClickedInventory() == inventory && player.getCooldown(event.getCurrentItem().getType()) <= 0) {
					if(event.getSlot() == 12) {
						boolean b = !playerData.getBoolParam("showStatistic");
						playerData.setSettingParam("showStatistic", b);
						if(!b)
							playerData.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
						updateSettingsItem();
						player.setCooldown(event.getCurrentItem().getType(), 40);
					}
					else if(event.getSlot() == 14) {
						playerData.setSettingParam("chatHints", !playerData.getBoolParam("chatHints"));
						updateSettingsItem();
						player.setCooldown(event.getCurrentItem().getType(), 40);
					}
				}
			}
		}
	}
	
	@EventHandler
    public void inventoryDrag(InventoryDragEvent event)
	{
		if(event.getView().getTopInventory() == inventory) {
			event.setCancelled(true);
		}
	}
	
	void Update() {
		if(player.getOpenInventory().getTopInventory() != inventory) {
			InventoryClickEvent.getHandlerList().unregister(this);
			InventoryDragEvent.getHandlerList().unregister(this);
			Bukkit.getServer().getScheduler().cancelTask(taskId);
		}
	}
}
