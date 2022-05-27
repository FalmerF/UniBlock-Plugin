package net.ifmcore.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import net.ifmcore.CustomEnchants;
import net.ifmcore.CustomItems;
import net.ifmcore.Uni;
import net.ifmcore.NBTEditor;

public class EnchantsListener implements Listener {
	static BlockBreakEvent blockBreakEvent = null;
	static List<Material> accuracyBlocks;
	
	public EnchantsListener() {
		Uni.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, new  Runnable(){
			public void run(){
				onArmorTick();
				onInventoryTick();
			}
		}, 5l, 5l);
	}
	
	void onArmorTick() {
		for(Player player : Bukkit.getOnlinePlayers())
			for(ItemStack armor : player.getInventory().getArmorContents())
				onPlayerArmorUpdate(player, armor);
	}
	
	void onInventoryTick() {
		for(Player player : Bukkit.getOnlinePlayers())
			for(ItemStack item : player.getInventory().getContents()) {
				if(item != null && CustomItems.isCustomItem(item) && CustomItems.getCustomItemName(item).equals("strangeHeart") && NBTEditor.getBoolean(item, "enable")) {
					World world = player.getWorld();
					Location loc = player.getLocation();
					Collection<Entity> itemsEntity = world.getNearbyEntities(player.getLocation(), 10, 10, 10, (entity -> entity.getType() == EntityType.DROPPED_ITEM));
					Iterator<Entity> iterator = itemsEntity.iterator();
					while (iterator.hasNext()) {
						Item itemEntity = (Item) iterator.next();
						itemEntity.setPickupDelay(0);
						itemEntity.teleport(player);
						world.spawnParticle(Particle.REVERSE_PORTAL, loc.getX()+0.5f, loc.getY()+0.5f, loc.getZ()+0.5f, 10, 0.5f, 0.5f, 0.5f);
			        }
				}
			}
	}
	
	void onPlayerArmorUpdate(Player player, ItemStack armor) {
		if(armor != null) {
			Map<String, Integer> enchants = CustomEnchants.getItemEnchants(armor);
			int lvl = enchants.getOrDefault("VoidProtection", 0);
			executeVoidProtectionEnchant(player, lvl);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if(event.isCancelled())
			return;
	    if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player) {
        	Player player = (Player) event.getDamager();
        	ItemStack item = player.getInventory().getItemInMainHand();
        	if(item != null) {
        		Map<String, Integer> enchants = CustomEnchants.getItemEnchants(item);
        		int lvl = enchants.getOrDefault("MoreExp", 0);
        		executeMoreExpEnchant(player, lvl, event.getDamage(), (LivingEntity) event.getEntity());
        		lvl = enchants.getOrDefault("SkullBeater", 0);
        		executeSkullBeater(player, lvl, event.getDamage(), (LivingEntity) event.getEntity());
        		lvl = enchants.getOrDefault("Vampire", 0);
        		executeVampire(player, lvl, event.getDamage(), (LivingEntity) event.getEntity());
        	}
	    }
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent event){
		if(!event.isCancelled() && event.getEntity() instanceof Player && (event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.FLY_INTO_WALL)) {
			Player player = (Player) event.getEntity();
			ItemStack elytra = player.getInventory().getChestplate();
			if(elytra != null && elytra.getType() == Material.ELYTRA && player.getHealth() - event.getFinalDamage() <= 0) {
				Map<String, Integer> enchants = CustomEnchants.getItemEnchants(elytra);
        		int lvl = enchants.getOrDefault("Airbag", 0);
        		if(lvl > 0) {
        			if(player.getFallDistance() >= 300) {
        				Uni.getPlayerData(player).giveBadge("\uE321");
        			}
        			player.setHealth(0.6f);
        			player.damage(0.1);
        			event.setCancelled(true);
        		}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if(event.isCancelled())
			return;
		
		ItemStack item = player.getInventory().getItemInMainHand();
    	if(item != null) {
    		Map<String, Integer> enchants = CustomEnchants.getItemEnchants(item);
    		int lvl = enchants.getOrDefault("Tunnel", 0);
    		if(event != blockBreakEvent) {
    			executeTunnelEnchant(player, lvl, block);
    		}
    		lvl = enchants.getOrDefault("Lumberjack", 0);
    		if(event != blockBreakEvent)
    			executeLumberjackEnchant(player, lvl, block);
    		lvl = enchants.getOrDefault("Accuracy", 0);
			executeAccuracyEnchant(player, lvl, block, event);
    	}
	}
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent event) {
		Player player = event.getPlayer();
		
		if(event.isCancelled())
			return;
		
		ItemStack item = player.getInventory().getItemInMainHand();
    	if(item != null) {
    		Map<String, Integer> enchants = CustomEnchants.getItemEnchants(item);
    		int lvl = enchants.getOrDefault("FireHook", 0);
    		executeFireHookEnchant(player, lvl, event);
    		lvl = enchants.getOrDefault("MovingHook", 0);
    		executeMovingHookEnchant(player, lvl, event);
    	}
	}
	
	public void executeFireHookEnchant(Player player, int enchantLevel, PlayerFishEvent event) {
		if(enchantLevel <= 0)
			return;
		
		if(event.getState() == State.CAUGHT_FISH && event.getCaught() instanceof Item) {
			Item item = (Item) event.getCaught();
			ItemStack itemStack = item.getItemStack();
			Iterator<Recipe> iter = Bukkit.recipeIterator();
			while (iter.hasNext()) {
				Recipe recipe = iter.next();
			    if (!(recipe instanceof FurnaceRecipe)) continue;
			    if (((FurnaceRecipe) recipe).getInput().getType() != itemStack.getType()) continue;
			    item.setItemStack(recipe.getResult());
			    return;
			}
		}
		else if(event.getState() == State.FISHING) {
			FishHook hook = event.getHook();
			hook.setVisualFire(true);
		}
	}
	
	public void executeMovingHookEnchant(Player player, int enchantLevel, PlayerFishEvent event) {
		if(enchantLevel <= 0)
			return;
		
		FishHook hook = event.getHook();
		if(event.getState() == State.IN_GROUND || (event.getState() == State.REEL_IN && hook.isInWater())) {
			Vector playerLoc = player.getLocation().toVector();
			Vector hookLoc = hook.getLocation().toVector();
			Vector boostVelocity = hookLoc.subtract(playerLoc).multiply(0.2f);
			boostVelocity.add(new Vector(0, 0.3f*boostVelocity.length(), 0));
			if(!player.isOnGround())
				boostVelocity.multiply(new Vector(2, 1.5f, 2));
			player.setVelocity(boostVelocity);
		}
	}
	
	public void executeVoidProtectionEnchant(Player player, int enchantLevel) {
		if(enchantLevel <= 0)
			return;
		
		World world = player.getWorld();
		Collection<Entity> endermans = world.getNearbyEntities(player.getLocation(), 10, 10, 10, (entity -> entity.getType() == EntityType.ENDERMAN));
		Iterator<Entity> iterator = endermans.iterator();
		while (iterator.hasNext()) {
			Entity enderman = iterator.next();
			((Creature)enderman).setTarget((LivingEntity) enderman);
        }
	}
	
	public void executeMoreExpEnchant(Player player, int enchantLevel, double damage, LivingEntity entity) {
		if(enchantLevel > 0) {
			int exp = (int)Math.ceil(damage*enchantLevel/15);
			player.giveExp(exp);
		}
	}
	
	public void executeSkullBeater(Player player, int enchantLevel, double damage, LivingEntity entity) {
		if(enchantLevel > 0 && entity.getType() == EntityType.WITHER_SKELETON) {
			if(entity.getHealth()-damage <= 0 && Uni.getRandomNumber(0, 3) <= 1) {
				World world = entity.getWorld();
				Location loc = entity.getLocation();
				world.dropItemNaturally(loc, new ItemStack(Material.WITHER_SKELETON_SKULL));
			}
		}
	}
	
	public void executeVampire(Player player, int enchantLevel, double damage, LivingEntity entity) {
		if(enchantLevel > 0) {
			if((int)Uni.getRandomNumber(0, 3) == 0) {
				float hp = (float) Math.ceil(damage/5);
				player.setHealth(Math.min(player.getHealth()+hp, player.getMaxHealth()));
				World world = player.getWorld();
				Location loc = player.getLocation().clone();
				loc.add(0, 1, 0);
				world.playSound(loc, Sound.ENTITY_GENERIC_EAT, 10, 1);
				world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 50, 0.5f, 0.5f, 0.5f);
			}
		}
	}
	
	public void executeTunnelEnchant(Player player, int enchantLevel, Block block) {
		ItemStack tool = player.getInventory().getItemInMainHand();
		if(player.isSneaking() || enchantLevel <= 0 || !NBTEditor.getBoolean(tool, "tunnelMode"))
			return;
		World world = block.getWorld();
		((ExperienceOrb)world.spawn(block.getLocation(), ExperienceOrb.class)).setExperience(1);
		RayTraceResult rayTrace = player.rayTraceBlocks(5);
		if(rayTrace == null)
			return;
		Location loc = block.getLocation();
		Location relativeLoc = new Location(block.getWorld(), 1, 1, 1);
		switch(rayTrace.getHitBlockFace()) {
			case DOWN:
			case UP:
				relativeLoc.setY(0);
				break;
			case EAST:
			case WEST:
				relativeLoc.setX(0);
				break;
			case SOUTH:
			case NORTH:
				relativeLoc.setZ(0);
				break;
			default:
				relativeLoc.setX(0);
				relativeLoc.setY(0);
				relativeLoc.setZ(0);
				break;
		}
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++)
				for(int z = -1; z <= 1; z++) {
					if(x == 0 && y == 0 && z == 0)
						continue;
					int xPos = loc.getBlockX()+relativeLoc.getBlockX()*x;
					int yPos = loc.getBlockY()+relativeLoc.getBlockY()*y;
					int zPos = loc.getBlockZ()+relativeLoc.getBlockZ()*z;
					Block b = world.getBlockAt(xPos, yPos, zPos);
					
					if(b.getType() != Material.BEDROCK && b.getType() != Material.AIR) {
						breakBlock(player, b, tool);
					}
				}
		}
	}
	
	public void executeLumberjackEnchant(Player player, int enchantLevel, Block block) {
		ItemStack tool = player.getInventory().getItemInMainHand();
		if(player.isSneaking() || enchantLevel <= 0 || !NBTEditor.getBoolean(tool, "lumberjackMode"))
			return;
		World world = block.getWorld();
		Location loc = block.getLocation();
		String blockName = block.getType().name().toLowerCase();
		if(blockName.endsWith("_log") || blockName.endsWith("_stem")) {
			int breakedBlocks = breakBlockWithLumberjackEnchant(player, block, world, tool, loc, 1);
			ItemMeta itemMeta = tool.getItemMeta();
			if(itemMeta instanceof Damageable) {
				Damageable meta = (Damageable) itemMeta;
				int unbreakingLvl = tool.containsEnchantment(Enchantment.DURABILITY) ? tool.getEnchantmentLevel(Enchantment.DURABILITY) : 0;
				int damage = unbreakingLvl > 0 ? breakedBlocks/unbreakingLvl : breakedBlocks;
				meta.setDamage(meta.getDamage()+damage);
				tool.setItemMeta((ItemMeta) meta);
			}
		}
	}
	
	int breakBlockWithLumberjackEnchant(Player player, Block block, World world, ItemStack tool, Location startLoc, int count) {
		Location loc = block.getLocation();
		for(int x = -1; x <= 1; x++)
			for(int y = -1; y <= 1; y++)
				for(int z = -1; z <= 1; z++) {
					if(count >= 1000)
						return count;
					if(x == 0 && y == 0 && z == 0)
						continue;
					int xPos = loc.getBlockX()+x;
					int yPos = loc.getBlockY()+y;
					int zPos = loc.getBlockZ()+z;
					Block b = world.getBlockAt(xPos, yPos, zPos);
					String blockName = b.getType().name().toLowerCase();
					if(blockName.endsWith("_log") || blockName.endsWith("_stem")) {
						blockBreakEvent = new BlockBreakEvent(b, player);
				        Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
				        if(blockBreakEvent.isCancelled())
				        	continue;
				        List<ItemStack> drops = (List<ItemStack>) b.getDrops(tool, player);
				        if(blockBreakEvent.isDropItems())
					        for(ItemStack d : drops)
								world.dropItemNaturally(b.getLocation(), d);
				        b.setType(Material.AIR);
						count++;
						breakBlockWithLumberjackEnchant(player, b, world, tool, startLoc, count);
					}
				}
		return count;
	}
	
	public boolean breakBlock(Player player, Block block, ItemStack tool) {
		World world = block.getWorld();
		Location loc = block.getLocation();
		List<ItemStack> drops = (List<ItemStack>) block.getDrops(tool, player);
		try {
	        if(drops.isEmpty())
				return false;
	        
	        blockBreakEvent = new BlockBreakEvent(block, player);
	        Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
	        if(blockBreakEvent.isCancelled())
	        	return false;
		} catch(Exception e) {
			e.printStackTrace();
		}
		block.breakNaturally(tool);
		ItemMeta itemMeta = tool.getItemMeta();
		if(itemMeta instanceof Damageable) {
			Damageable meta = (Damageable) itemMeta;
			int unbreakingLvl = tool.containsEnchantment(Enchantment.DURABILITY) ? tool.getEnchantmentLevel(Enchantment.DURABILITY) : 0;
			int damage = (int)Uni.getRandomNumber(0, unbreakingLvl*2) <= 0 ? 1 : 0;
			meta.setDamage(meta.getDamage()+damage);
			tool.setItemMeta((ItemMeta) meta);
		}
		return true;
	}
	
	public void executeAccuracyEnchant(Player player, int enchantLevel, Block block, BlockBreakEvent event) {
		Material mat = block.getType();
		if(enchantLevel <= 0 || !accuracyBlocks.contains(mat))
			return;
		
		World world = block.getWorld();
		world.dropItemNaturally(block.getLocation(), new ItemStack(mat));
		event.setExpToDrop(0);
		event.setDropItems(false);
	}
	
	static {
		accuracyBlocks = new ArrayList<Material>();
		accuracyBlocks.add(Material.SPAWNER);
		accuracyBlocks.add(Material.BUDDING_AMETHYST);
		accuracyBlocks.add(Material.ENDER_CHEST);
	}
}
