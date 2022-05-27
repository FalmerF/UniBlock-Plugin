package net.ifmcore.inventory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import net.ifmcore.Localization;
import net.ifmcore.Uni;

public class InvSeeInventory implements Listener {
	public static ItemStack emptyInventorySlotItem;
	
	public Inventory inventory;
	public Player player;
	public Player targetPlayer;
	int taskId;
	List<Integer> lockedSlots = Arrays.asList(9, 10, 11, 12);
	public InvSeeInventory(Player player, Player targetPlayer) {
		inventory = Bukkit.createInventory(null, 45, "»нвентарь игрока " + targetPlayer.getName());
		Bukkit.getServer().getPluginManager().registerEvents(this, Uni.instance);
		this.player = player;
		this.targetPlayer = targetPlayer;
		player.openInventory(inventory);
		
		for(int i = 9; i < 18; i++) {
			inventory.setItem(i, emptyInventorySlotItem.clone());
		}
		updateInventory(targetPlayer.getInventory());
		
		taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, new  Runnable(){
			public void run(){
				Update();
			}
		}, 5l, 20l);
	}
	
	@EventHandler
    public void inventoryClick(InventoryClickEvent event)
	{
		if(event.getClickedInventory() == inventory) {
			if(player.hasPermission("ifm.invsee.useItems") && !event.isShiftClick() && !lockedSlots.contains(event.getSlot())) {
				BukkitRunnable task = new BukkitRunnable() {
			        @Override
			        public void run() {
			        	int slot = event.getSlot();
			        	int slot2 = getTargetInvSlotByInvSlot(slot);
			        	targetPlayer.getInventory().setItem(slot2, inventory.getItem(slot));
			        	//updateTargetInventory(inventory);
			        }
			    };
			    task.runTaskLater(Uni.instance, 1);
			}
			else
				event.setCancelled(true);
		}
		else if(event.getClickedInventory() == targetPlayer.getInventory()) {
			BukkitRunnable task = new BukkitRunnable() {
		        @Override
		        public void run() {
		        	int slot = event.getSlot();
		        	int slot2 = getInvSlotByTargetInvSlot(slot);
		        	inventory.setItem(slot2, targetPlayer.getInventory().getItem(slot));
		        	//updateInventory(targetPlayer.getInventory());
		        }
		    };
		    task.runTaskLater(Uni.instance, 1);
			
		}
	}
	
	public int getTargetInvSlotByInvSlot(int slot) {
		if(slot < 9)
			return slot;
		if(slot >= 13 && slot <= 17)
			return 40 + (13-slot);
		if(slot >= 18)
			return slot-9;
		return 0;
	}
	
	public int getInvSlotByTargetInvSlot(int slot) {
		if(slot < 9)
			return slot;
		if(slot >= 36 && slot <= 40)
			return 13 + (40-slot);
		if(slot >= 9)
			return slot+9;
		return 0;
	}
	
	@EventHandler
    public void inventoryDrag(InventoryDragEvent event)
	{
		if(event.getView().getTopInventory() == inventory) {
			event.setCancelled(true);
		}
	}
	
	void updateTargetInventory(Inventory evolvedInv) {
		PlayerInventory targetInventory = targetPlayer.getInventory();
		for(int i = 0; i < 9; i++) {
			targetInventory.setItem(i, evolvedInv.getItem(i));
				
		}
		for(int i = 9; i < targetInventory.getSize(); i++) {
			if(evolvedInv.getSize() > 9+i) {
				targetInventory.setItem(i, evolvedInv.getItem(i+9));
			}
		}
		targetInventory.setHelmet(evolvedInv.getItem(14));
		targetInventory.setChestplate(evolvedInv.getItem(15));
		targetInventory.setLeggings(evolvedInv.getItem(16));
		targetInventory.setBoots(evolvedInv.getItem(17));
		targetInventory.setItemInOffHand(evolvedInv.getItem(13));
	}
	
	void updateInventory(Inventory evolvedInv) {
		PlayerInventory targetInventory = (PlayerInventory) evolvedInv;
		for(int i = 0; i < 9; i++) {
			inventory.setItem(i, targetInventory.getItem(i));
		}
		for(int i = 9; i < targetInventory.getSize(); i++) {
			if(inventory.getSize() > 9+i) {
				inventory.setItem(i+9, targetInventory.getItem(i));
			}
		}
		inventory.setItem(14, targetInventory.getHelmet());
		inventory.setItem(15, targetInventory.getChestplate());
		inventory.setItem(16, targetInventory.getLeggings());
		inventory.setItem(17, targetInventory.getBoots());
		inventory.setItem(13, targetInventory.getItemInOffHand());
	}
	
	void Update() {
		if(player.getOpenInventory().getTopInventory() != inventory) {
			InventoryClickEvent.getHandlerList().unregister(this);
			InventoryDragEvent.getHandlerList().unregister(this);
			Bukkit.getServer().getScheduler().cancelTask(taskId);
			return;
		}
		ItemStack playerInfoStack = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta itemMeta = playerInfoStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GOLD+""+targetPlayer.getName());
		List<String> itemDescription = new ArrayList<String>();
		itemDescription.add(" ");
		itemDescription.add(MessageFormat.format("{0}HP: {1}{3}/{4}", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW, targetPlayer.getHealth(), targetPlayer.getMaxHealth()));
		itemDescription.add(MessageFormat.format("{0}Food: {1}{3}/20", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW, targetPlayer.getFoodLevel()));
		itemMeta.setLore(itemDescription);
		playerInfoStack.setItemMeta(itemMeta);
		inventory.setItem(10, playerInfoStack);
		
		ItemStack playerEffectsStack = new ItemStack(Material.GLASS_BOTTLE);
		itemMeta = playerEffectsStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BLUE+"Ёффекты");
		itemDescription = new ArrayList<String>();
		itemDescription.add(" ");
		for(PotionEffect effect : targetPlayer.getActivePotionEffects()) {
			itemDescription.add(MessageFormat.format("{0}{2} {1}({3})", ChatColor.GRAY, ChatColor.YELLOW, Localization.getPotionEffectName(effect), Localization.fromTicksToFormatedTime(effect.getDuration())));
		}
		itemMeta.setLore(itemDescription);
		playerEffectsStack.setItemMeta(itemMeta);
		
		inventory.setItem(11, playerEffectsStack);
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
