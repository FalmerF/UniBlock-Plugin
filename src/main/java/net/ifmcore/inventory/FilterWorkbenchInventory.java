package net.ifmcore.inventory;

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

import net.ifmcore.CustomItems;
import net.ifmcore.ItemUtils;
import net.ifmcore.NBTEditor;
import net.ifmcore.NBTEditor.NBTCompound;
import net.ifmcore.Uni;
import net.minecraft.nbt.NBTTagCompound;

public class FilterWorkbenchInventory implements Listener {
	public Inventory inventory;
	public Player player;
	public List<ItemFilter> filters = new ArrayList<ItemFilter>();
	int taskId;
	
	public FilterWorkbenchInventory(Player player) {
		this.player = player;
		inventory = Bukkit.createInventory(null, 45, ChatColor.DARK_AQUA+"Верстак фильтров");
		makeInventory();
		Bukkit.getServer().getPluginManager().registerEvents(this, Uni.instance);
		player.openInventory(inventory);
		
		taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, new  Runnable(){
			public void run(){
				Update();
			}
		}, 5l, 20l);
	}
	
	void makeInventory() {
		inventory.setItem(7, ItemUtils.emptyInventorySlotItem);
		inventory.setItem(16, ItemUtils.emptyInventorySlotItem);
		inventory.setItem(25, ItemUtils.emptyInventorySlotItem);
		inventory.setItem(34, ItemUtils.emptyInventorySlotItem);
		inventory.setItem(43, ItemUtils.emptyInventorySlotItem);
		inventory.setItem(17, ItemUtils.emptyInventorySlotItem);
	}
	
	@EventHandler
    public void inventoryClick(InventoryClickEvent event)
	{
		if(event.getView().getTopInventory() == inventory) {
			event.setCancelled(true);
			int slot = event.getSlot();
			Inventory inv = event.getClickedInventory();
			if(inv == inventory) {
				ItemStack item = inventory.getItem(slot);
				if(item != null) {
					if(slot == 8) {
						ItemUtils.givePlayerItem(player, item);
						setFilter(null);
					}
					else if((slot+1)%9 <= 7) { // Left side of inventory
						if(event.isLeftClick()) {
							removeItemFromFilter(slot);
						}
						else if(event.isRightClick()) {
							offItemNBT(slot);
						}
					}
				}
			}
			else {
				ItemStack item = inv.getItem(slot);
				if(item != null) {
					if(CustomItems.getCustomItemName(item).equals("hopperFilter") && getFilterItem() == null) {
						setFilter(item);
						inv.setItem(slot, null);
					}
					else if(getFilterItem() != null) {
						addItemToFilter(item);
					}
				}
			}
		}
	}
	
	public ItemStack getFilterItem() {
		return inventory.getItem(8);
	}
	
	public void setFilter(ItemStack item) {
		inventory.setItem(8, item);
		loadFilter(item);
	}
	
	public void loadFilter(ItemStack item) {
		filters.clear();
		if(item != null) {
			String nbtString = NBTEditor.getString(item, "filter");
			if(nbtString == null || nbtString.equals("")) {
				displayFilter();
				return;
			}
			NBTCompound nbt = NBTEditor.getNBTCompound(nbtString);
			int size = NBTEditor.getInt(nbt, "size");
			for(int i = 0; i < size; i++) {
				Material mat = Material.valueOf(NBTEditor.getString(nbt, String.valueOf(i), "Material"));
				String nbtJson = NBTEditor.getString(nbt, String.valueOf(i), "NBT");
				if(nbtJson == null || nbtJson.equals(""))
					filters.add(new ItemFilter(mat));
				else
					filters.add(new ItemFilter(mat, nbtJson));
			}
		}
		displayFilter();
	}
	
	public void saveFilter() {
		ItemStack item = getFilterItem();
		if(item == null)
			return;
		NBTCompound nbt = NBTEditor.getEmptyNBTCompound();
		List<String> lore = new ArrayList<String>();
		for(int i = 0; i < filters.size(); i++) {
			ItemFilter itemFilter = filters.get(i);
			ItemStack itemStack = new ItemStack(itemFilter.mat);
			nbt.set(itemFilter.mat.toString(), String.valueOf(i), "Material");
			if(itemFilter.nbt != null && !itemFilter.nbt.equals("")) {
				nbt.set(itemFilter.nbt, String.valueOf(i), "NBT");
				itemStack = NBTEditor.getItemFromTag(NBTEditor.getNBTCompound(itemFilter.nbt));
			}
			
			if(i%2 == 0) {
				lore.add(ChatColor.RED+ItemUtils.getItemName(itemStack));
			}
			else {
				lore.set(lore.size()-1, lore.get(lore.size()-1)+" • "+ChatColor.RED+ItemUtils.getItemName(itemStack));
			}
		}
		nbt.set(filters.size(), "size");
		item = NBTEditor.set(item, nbt.toJson(), "filter");
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		inventory.setItem(8, item);
		displayFilter();
	}
	
	public void addItemToFilter(ItemStack item) {
		if(filters.size() >= 35)
			return;
		ItemFilter itemFilter = new ItemFilter(item.getType());
		NBTCompound nbt = NBTEditor.getNBTCompound(item);
		if(nbt != null && !nbt.toJson().equals("{}")) {
			NBTCompound nbtCopy = NBTEditor.getNBTCompound(nbt.toJson());
			nbtCopy.set(null, "Count");
			nbtCopy.set(null, "id");
			if(!nbtCopy.toJson().equals("{}") && !nbtCopy.toJson().equals(""))
				itemFilter.nbt = nbt.toJson();
		}
		filters.add(itemFilter);
		saveFilter();
	}
	
	public void removeItemFromFilter(int slot) {
		int index = slot-(int)Math.floor(slot/9.0f)*2;
		filters.remove(index);
		saveFilter();
	}
	
	public void offItemNBT(int slot) {
		if(filters.size() >= 35)
			return;
		int index = slot-(int)Math.floor(slot/9.0f)*2;
		ItemFilter itemFilter = filters.get(index);
		if(itemFilter.nbt != null && !itemFilter.nbt.equals("")) {
			itemFilter.nbt = null;
			saveFilter();
		}
	}
	
	public void displayFilter() {
		for(int i = 0; i < 35; i++) {
			inventory.setItem((int)Math.floor(i/7.0f)*2+i, null);
		}
		for(int i = 0; i < filters.size(); i++) {
			ItemFilter itemFilter = filters.get(i);
			ItemStack item = null;
			if(itemFilter.nbt != null) {
				item = NBTEditor.getItemFromTag(NBTEditor.getNBTCompound(itemFilter.nbt));
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore() == null ? new ArrayList<String>() : meta.getLore();
				lore.add(ChatColor.RED+"ПКМ, чтобы отключить NBT");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
			else
				item = new ItemStack(itemFilter.mat);
			inventory.setItem((int)Math.floor(i/7.0f)*2+i, item);
		}
	}
	
	@EventHandler
    public void inventoryDrag(InventoryDragEvent event)
	{
		if(event.getView().getTopInventory() == inventory) {
			event.setCancelled(true);
		}
	}
	
	void unregister() {
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryDragEvent.getHandlerList().unregister(this);
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}
	
	void Update() {
		if(player.getOpenInventory().getTopInventory() != inventory) {
			unregister();
			ItemStack item = getFilterItem();
			if(item != null)
				ItemUtils.givePlayerItem(player, item);
			return;
		}
	}
	
	class ItemFilter {
		Material mat = null;
		String nbt = null;
		public ItemFilter(Material mat) {
			this.mat = mat;
			
		}
		public ItemFilter(Material mat, String nbt) {
			this.mat = mat;
			this.nbt = nbt;
		}
	}
}
