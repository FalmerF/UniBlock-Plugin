package net.ifmcore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.ifmcore.listeners.EnderDragonListener;

public class VoidAltar {
	public static boolean spawningDragon;
	
	public static boolean isAltar(Block block) {
		if(block == null || !block.getWorld().getName().equals("world_the_end"))
			return false;
		if(block.getType() != Material.LECTERN) {
			block = getBlockInRadius(block.getLocation(), Material.LECTERN, 5);
			if(block == null)
				return false;
		}
		int countOfEndPortalFrame = getBlocksInRadius(block.getLocation(), Material.END_PORTAL_FRAME, 5).size();
		if(countOfEndPortalFrame < 16)
			return false;
		return true;
	}
	
	public static Block getAltarCenter(Block block) {
		if(block == null || !block.getWorld().getName().equals("world_the_end"))
			return null;
		if(block.getType() == Material.LECTERN)
			return block;
		return getBlockInRadius(block.getLocation(), Material.LECTERN, 5);
	}
	
	public static int getAmountOfEyeOnAltar(Block block) {
		block = getAltarCenter(block);
		List<Block> blocks = getBlocksInRadius(block.getLocation(), Material.END_PORTAL_FRAME, 5);
		int amount = 0;
		for(Block b : blocks) {
			EndPortalFrame frame = (EndPortalFrame) b.getBlockData();
			if(frame.hasEye())
				amount++;
		}
		return amount;
	}
	
	static int taskId;
	static int timer = 0;
	static int dragonLevel = 0;
	public static void spawnEnderDragon(Player player, Block block) {
		World world = block.getWorld();
		block = getAltarCenter(block);
		Location loc = block.getLocation();
		List<Block> blocks = getBlocksInRadius(block.getLocation(), Material.END_PORTAL_FRAME, 5);
		List<Block> filledBlocks = new ArrayList<Block>();
		for(Block b : blocks) {
			if(((EndPortalFrame) b.getBlockData()).hasEye())
				filledBlocks.add(b);
		}
		dragonLevel = 0;
		if(filledBlocks.size() >= 24)
			dragonLevel = 4;
		else if(filledBlocks.size() >= 16)
			dragonLevel = 3;
		else if(filledBlocks.size() >= 8)
			dragonLevel = 2;
		else if(filledBlocks.size() >= 4)
			dragonLevel = 1;
		if(spawningDragon)
			return;
		else if(EnderDragonListener.getEnderDragon() != null) {
			player.sendMessage(MessageFormat.format("{0}Алтарь {1}> {2}Для начала убейте существующего дракона.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
			return;
		}
		else if(dragonLevel <= 0) {
			player.sendMessage(MessageFormat.format("{0}Алтарь {1}> {2}Не достаточно Око Пустоты для призыва дракона:\n"
					+ "    {2}1-й уровень {3}-{2} 4 Око Пустоты\n"
					+ "    {2}2-й уровень {3}-{2} 8 Око Пустоты\n"
					+ "    {2}3-й уровень {3}-{2} 16 Око Пустоты\n"
					+ "    {2}4-й уровень {3}-{2} 24 Око Пустоты", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GRAY));
			return;
		}
		Bukkit.broadcastMessage(MessageFormat.format("{0}Игрок {1}{2}{0} призвал дракона {1}{3}-го уровня{0}.", ChatColor.GREEN, ChatColor.GOLD, player.getName(), dragonLevel));
		spawningDragon = true;
		
		world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
		world.spawnParticle(Particle.REVERSE_PORTAL, loc.getX()+0.5f, loc.getY()+0.5f, loc.getZ()+0.5f, 10000, 4f, 4f, 4f);
		
		while(filledBlocks.size() > 0) {
			Block b = filledBlocks.remove(0);
			Location loc2 = b.getLocation();
			EndPortalFrame frame = (EndPortalFrame) b.getBlockData();
			frame.setEye(false);
			b.setBlockData(frame);
			world.playSound(loc2, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
			world.spawnParticle(Particle.ITEM_CRACK, loc2.getX()+0.5f, loc2.getY()+0.5f, loc2.getZ()+0.5f, 20, new ItemStack(Material.ENDER_EYE));
			world.spawnParticle(Particle.VILLAGER_HAPPY, loc.getX()+0.5f, loc.getY()+0.5f, loc.getZ()+0.5f, 30, 0.5f, 0.5f, 0.5f);
		}
		for(Entity e : world.getEntities()) {
			if(e.getType() == EntityType.ENDER_CRYSTAL && e.getLocation().distance(loc) <= 300)
				e.remove();
		}
		
		Chunk chunk = world.getChunkAt(loc);
		for(int chunkX = -10; chunkX < 10; chunkX++) {
			for(int chunkZ = -10; chunkZ < 10; chunkZ++) {
				Chunk c = world.getChunkAt(chunk.getX()+chunkX, chunk.getZ()+chunkZ);
				for(int x = 0; x < 16; x++) {
					for(int z = 0; z < 16; z++) {
						for(int y = 0; y < 256; y++) {
							Block b2 = c.getBlock(x, y, z);
							if(b2.getType() == Material.BEDROCK) {
								Location cLoc = b2.getLocation().clone();
								cLoc.add(0.5f, 1, 0.5f);
								world.spawnEntity(cLoc, EntityType.ENDER_CRYSTAL);
							}
						}
					}
				}
			}
		}
		Location spawnLoc = loc.clone();
		spawnLoc.add(0, 10, 0);
		world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
		EnderDragon enderDragon = (EnderDragon) world.spawnEntity(spawnLoc, EntityType.ENDER_DRAGON);
		if(dragonLevel == 1) {
			enderDragon.setMaxHealth(500);
			enderDragon.setHealth(500);
		}
		else if(dragonLevel == 2) {
			enderDragon.setMaxHealth(750);
			enderDragon.setHealth(750);
		}
		else if(dragonLevel == 3) {
			enderDragon.setMaxHealth(1000);
			enderDragon.setHealth(1000);
		}
		else if(dragonLevel == 4) {
			enderDragon.setMaxHealth(2000);
			enderDragon.setHealth(2000);
		}
		EnderDragonListener.setEnderDragon(enderDragon);
		spawningDragon = false;
	}
	
	public static Block getBlockInRadius(Location loc, Material mat, int radius) {
		World world = loc.getWorld();
		for(int x = loc.getBlockX()-radius; x < loc.getBlockX()+radius; x++) {
			for(int y = loc.getBlockY()-radius; y < loc.getBlockY()+radius; y++) {
				for(int z = loc.getBlockZ()-radius; z < loc.getBlockZ()+radius; z++) {
					Block block = world.getBlockAt(x, y, z);
					if(block != null && block.getType() == mat)
						return block;
				}
			}
		}
		return null;
	}
	
	public static List<Block> getBlocksInRadius(Location loc, Material mat, int radius) {
		World world = loc.getWorld();
		List<Block> blocks = new ArrayList<Block>();
		for(int x = loc.getBlockX()-radius; x < loc.getBlockX()+radius; x++) {
			for(int y = loc.getBlockY()-radius; y < loc.getBlockY()+radius; y++) {
				for(int z = loc.getBlockZ()-radius; z < loc.getBlockZ()+radius; z++) {
					Block block = world.getBlockAt(x, y, z);
					if(block != null && block.getType() == mat)
						blocks.add(block);
				}
			}
		}
		return blocks;
	}
}
