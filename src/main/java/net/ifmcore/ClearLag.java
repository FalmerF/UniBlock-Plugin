package net.ifmcore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class ClearLag {
	public static ClearLag instance;
	public static List<EntityType> defaultBlackList = Arrays.asList(new EntityType[] {EntityType.VILLAGER, EntityType.PLAYER, EntityType.PAINTING, EntityType.ENDER_CRYSTAL, EntityType.ITEM_FRAME, EntityType.ARMOR_STAND, EntityType.MINECART, EntityType.MINECART_CHEST, EntityType.MINECART_COMMAND, EntityType.MINECART_FURNACE, EntityType.MINECART_HOPPER, EntityType.MINECART_MOB_SPAWNER, EntityType.MINECART_TNT, EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.BOAT, EntityType.IRON_GOLEM});
	boolean clearingStarted = false;
	BukkitTask task;
	BukkitTask chunkTask;
	public ClearLag() {
		instance = this;
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, new  Runnable(){
			public void run(){
				processClearingLag();
				clearAllChunks();
			}
		}, 5l, 20l);
	}
	
	public void processClearingLag() {
		List<Entity> allEntity = new ArrayList<Entity>();
		for(World world : Bukkit.getWorlds()) {
			allEntity.addAll(world.getEntities());
			for(Chunk chunk : world.getLoadedChunks()) {
				if((chunkTask == null || chunkTask.isCancelled()) && chunk.getEntities().length >= 1000) {
					clear(chunk);
				}
			}
		}
		if(allEntity.size() >= 15000) {
			sendInfoToDiscord();
			clearAllEntity();
		}
		else if(allEntity.size() >= 7000 && !clearingStarted) {
			sendInfoToDiscord();
			clear();
		}
	}
	
	public static void sendInfoToDiscord() {
		StringBuilder message = new StringBuilder();
		StringBuilder stats = new StringBuilder();
		StringBuilder chunkStats = new StringBuilder();
		message.append("**Большое количество существ на сервере!**");
		chunkStats.append("**Самые загруженные чанки**");
		
		int allChunks = 0;
		int allEntity = 0;
		
		for(World world : Bukkit.getWorlds()) {
			allEntity += world.getEntities().size();
			allChunks += world.getLoadedChunks().length;
			stats.append("\\n").append(world.getName()).append(": ");
			stats.append(world.getLoadedChunks().length).append(" Chunks").append(" - ");
			stats.append(world.getEntities().size()).append(" Entity");
			for(Chunk chunk : world.getLoadedChunks()) {
				if(chunk.getEntities().length >= 200) {
					chunkStats.append("\\n(").append(chunk.getX()).append(", ").append(chunk.getZ()).append(")");
					chunkStats.append(" - ").append("Entity ").append(chunk.getEntities().length);
					chunkStats.append("  `/tppos ").append(chunk.getX()*16).append(" 180 ").append(chunk.getZ()*16).append("`");
				}
			}
		}
		message.append("\\nChunks ").append(allChunks).append(", Entity ").append(allEntity);
		message.append("\\n").append(stats);
		message.append("\\n\\n").append(chunkStats);
	}
	
	public void clear() {
		clearingStarted = true;
		final int[] i = {0};
		task = Bukkit.getScheduler().runTaskTimer(Uni.instance, () -> {
            if(i[0] == 0) {
            	Bukkit.broadcastMessage(MessageFormat.format("{0}Clear {1} > {2}Все предметы и существа будут удалены через {1}30 секунд{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
            }
            else if(i[0] == 2) {
            	Bukkit.broadcastMessage(MessageFormat.format("{0}Clear {1} > {2}Все предметы и существа будут удалены через {1}10 секунд{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
            }
            else if(i[0] == 3) {
            	clearAllEntity();
            	Bukkit.broadcastMessage(MessageFormat.format("{0}Clear {1} > {2}Все предметы и существа удалены.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
            	clearingStarted = false;
            	task.cancel();
            }
			i[0]++;
        }, 0L, 200L);
	}
	
	public void clear(final Chunk chunk) {
		final int[] i = {0};
		chunkTask = Bukkit.getScheduler().runTaskTimer(Uni.instance, () -> {
            if(i[0] == 0) {
            	sendMessageInRadius(chunk.getWorld(), chunk, MessageFormat.format("{0}Clear {1} > {2}Все предметы и существа будут удалены в чанке {1}({3}, {4}){2} через {1}30 секунд{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW, chunk.getX(), chunk.getZ()));
            }
            else if(i[0] == 2) {
            	sendMessageInRadius(chunk.getWorld(), chunk, MessageFormat.format("{0}Clear {1} > {2}Все предметы и существа будут удалены в чанке {1}({3}, {4}){2} через {1}10 секунд{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW, chunk.getX(), chunk.getZ()));
            }
            else if(i[0] == 3) {
            	for(Entity entity : chunk.getEntities()) {
    				if(!isInDefaultBlackList(entity))
    					entity.remove();
    			}
            	sendMessageInRadius(chunk.getWorld(), chunk, MessageFormat.format("{0}Clear {1} > {2}Все предметы и существа удалены в чанке {1}({3}, {4}){2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW, chunk.getX(), chunk.getZ()));
            	chunkTask.cancel();
            }
			i[0]++;
        }, 0L, 200L);
	}
	
	public void sendMessageInRadius(World world, Chunk chunk, String message) {
		Location loc = new Location(world, chunk.getX()*16, 150, chunk.getZ()*16);
		for(Player player : world.getPlayers()) {
			if(player.getLocation().distance(loc) <= 300)
				player.sendMessage(message);
		}
	}
	
	public static boolean hasPlayerInRadius(Chunk chunk) {
		World world = chunk.getWorld();
		Location loc = new Location(world, chunk.getX()*16, 100, chunk.getZ()*16);
		for(Player player : world.getPlayers()) {
			if(player.getLocation().distance(loc) <= 150)
				return true;
		}
		return false;
	}
	
	public static void clearAllChunks() {
		for(World world : Bukkit.getWorlds()) {
			for(Chunk chunk : world.getLoadedChunks()) {
				Location loc = new Location(world, chunk.getX(), 180, chunk.getZ());
				if(!hasPlayerInRadius(chunk))
					world.unloadChunk(chunk);
			}
		}
	}
	
	public static void clearAllEntity() {
		for(World world : Bukkit.getWorlds()) {
			for(Entity entity : world.getEntities()) {
				if(!isInDefaultBlackList(entity))
					entity.remove();
			}
		}
	}
	
	public static void clearItemsEntity() {
		for(World world : Bukkit.getWorlds()) {
			for(Entity entity : world.getEntities()) {
				if(entity instanceof CraftItem)
					entity.remove();
			}
		}
	}
	
	public static void clearMobsEntity() {
		for(World world : Bukkit.getWorlds()) {
			for(Entity entity : world.getEntities()) {
				if(!isInDefaultBlackList(entity) && !(entity instanceof CraftItem))
					entity.remove();
			}
		}
	}
	
	public static boolean isInDefaultBlackList(Entity entity) {
		return defaultBlackList.contains(entity.getType()) || entity.getCustomName() != null;
	}
}
