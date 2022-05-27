package net.ifmcore.inventory;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.ifmcore.CustomEnchants;
import net.ifmcore.CustomItems;
import net.ifmcore.Uni;

public class CustomCreativeInventory implements Listener {
	public static CustomCreativeInventory instance;
	public static Inventory inv;
	
	public CustomCreativeInventory() {
		instance = this;
	}
	
	@EventHandler
    public void inventoryClick(InventoryClickEvent event)
	{
		Inventory inventory = event.getView().getTopInventory();
		if(inventory == inv) {
			Inventory clickedInventory = event.getClickedInventory();
			if(clickedInventory == inventory && event.getAction() != InventoryAction.CLONE_STACK) {
				event.setCancelled(true);
				event.getView().setCursor(inv.getItem(event.getSlot()));
			}
		}
	}
	
	static {
		inv = Bukkit.createInventory(null, 45, "Типо креативка");
		
		int index = 0;
		for(Entry<String, ItemStack> set : CustomItems.items.entrySet()) {
			ItemStack item = set.getValue();
			String name = set.getKey();
			if(name.startsWith("enchant")) {
				int maxLevel = CustomEnchants.getEnchantMaxLevel(item);
				for(int i = 1; i <= maxLevel; i++) {
					inv.setItem(index, CustomEnchants.makeEnchantBook(item, i));
					index++;
				}
			}
			else {
				inv.setItem(index, set.getValue());
				index++;
			}
		}
	}
}
