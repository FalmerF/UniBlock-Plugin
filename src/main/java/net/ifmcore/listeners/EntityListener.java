package net.ifmcore.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.ifmcore.CustomEnchants;
import net.ifmcore.CustomItems;
import net.ifmcore.NBTEditor;
import net.ifmcore.Uni;

public class EntityListener implements Listener {
	public static List<EntityType> raidersList = Arrays.asList(new EntityType[] {EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.RAVAGER, EntityType.EVOKER});
	
	@EventHandler
    public void raiderDeath(EntityDeathEvent event) {
        if(!raidersList.contains(event.getEntityType()))
            return;
        
        List<ItemStack> newDrop = new ArrayList<ItemStack>();
        for(ItemStack item : event.getDrops()) {
        	if((int)Uni.getRandomNumber(0, 3) == 0) continue;
        	newDrop.add(item);
        }
        event.getDrops().clear();
        event.getDrops().addAll(newDrop);
    }
	
	@EventHandler
    public void piglinDeath(EntityDeathEvent event) {
        if(event.getEntityType() == EntityType.ZOMBIFIED_PIGLIN)
            return;
        
        List<ItemStack> newDrop = new ArrayList<ItemStack>();
        for(ItemStack item : event.getDrops()) {
        	if((int)Uni.getRandomNumber(0, 3) == 0) continue;
        	newDrop.add(item);
        }
        event.getDrops().clear();
        event.getDrops().addAll(newDrop);
    }
	
	@EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if(event.getEntityType() == EntityType.GUARDIAN) {
			List<ItemStack> drops = event.getDrops();
			if(NBTEditor.isCustomEntity(entity)) {
				drops.clear();
				drops.add(CustomEnchants.makeEnchantBook("MovingHook", 1));
				event.setDroppedExp(10000);
			}
			else if((int)Uni.getRandomNumber(0, 100) <= 10) {
				drops.add(CustomItems.getCustomItem("enchPrismarineShard", 1));
			}
		}
	}
}
