package net.ifmcore.listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import net.ifmcore.Uni;

public class EventsListener implements Listener {
	public static HashMap<Material, Material> materialsRandom = new HashMap<Material, Material>();
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if(Uni.eventActive) {
			event.setDropItems(false);
			Block block = event.getBlock();
			World world = event.getBlock().getWorld();
			Material mat = materialsRandom.get(block.getType());
			ItemStack item = new ItemStack(mat);
			try {
				if(item.getType() != Material.AIR)
					world.dropItemNaturally(block.getLocation(), item);
			} catch(Exception e) {
				
			}
		}
	}
	
	public static void configureEvent() {
		Material[] mats = Material.values();
		materialsRandom.clear();
		for(Material m : mats) {
			while(true) {
				Material rMat = mats[(int) Uni.getRandomNumber(0, mats.length)];
				if(!rMat.name().endsWith("SPAWN_EGG") && rMat != Material.AIR) {
					materialsRandom.put(m, rMat);
					break;
				}
			}
		}
	}
	
	static {
		configureEvent();
	}
}
