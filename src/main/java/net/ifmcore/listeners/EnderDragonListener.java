package net.ifmcore.listeners;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.ifmcore.CustomItems;
import net.ifmcore.Uni;
import net.ifmcore.ItemUtils;
import net.ifmcore.VoidAltar;
import net.ifmcore.data.PlayerData;

public class EnderDragonListener implements Listener {
	private static EnderDragon enderDragon;
	private static int taskId = -1;
	private static int dragonPhase = 0;
	private static int dragonLevel = 0;
	private static int eventTimer = 0;
	private static List<PotionEffectType> negativePotionEffects;
	private static KeyedBossBar bar = null;
	private static NamespacedKey bossBarKey = new NamespacedKey(Uni.instance, "customEnderDragon");
	public static Map<String, Float> dragonDamage = new TreeMap<String, Float>();
	public EnderDragonListener() {
		World world = Bukkit.getWorld("world_the_end");
		for(Entity entity : world.getEntities())
			if(entity.getType() == EntityType.ENDER_DRAGON)
				setEnderDragon(entity);
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if(event.getEntityType() == EntityType.ENDER_DRAGON && !VoidAltar.spawningDragon)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEnderDragonChangePhase(EnderDragonChangePhaseEvent event) {
		if(event.getNewPhase() == Phase.FLY_TO_PORTAL && (dragonPhase != 1 || dragonLevel >= 4))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent event) {
		if(event.getEntity() == enderDragon) {
			setEnderDragon(null);
		}
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if(event.getEntity() == enderDragon && enderDragon != null)
			bar.setProgress(Math.max((enderDragon.getHealth()-event.getFinalDamage())/enderDragon.getMaxHealth(), 0));
	}
	
	public static void setEnderDragon(Entity entity) {
		if(entity == null || entity instanceof EnderDragon) {
			enderDragon = (EnderDragon) entity;
			if(bar != null) {
				bar.removeAll();
				bar.setVisible(false);
				Bukkit.getServer().removeBossBar(bossBarKey);
			}

			if(taskId != -1) {
				Bukkit.getServer().getScheduler().cancelTask(taskId);
			}
			if(enderDragon == null) {
				giveReward();
				dragonPhase = 0;
				dragonLevel = 0;
				return;
			}
			
			dragonLevel = getEnderDragonLevel(enderDragon);
			
			dragonDamage.clear();
			
			enderDragon.setPhase(Phase.CIRCLING);
			enderDragon.getDragonBattle().generateEndPortal(false);
			enderDragon.getDragonBattle().getBossBar().setVisible(false);
			
			bar = Bukkit.getServer().createBossBar(bossBarKey, "Дракон Эндера", BarColor.PURPLE, BarStyle.SEGMENTED_6);
	        bar.setVisible(true);
			
			dragonPhase = 1;
			eventTimer = 2;
			
			taskId = Uni.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, new  Runnable(){
				public void run() {
					if(dragonPhase == 1 && enderDragon.getHealth() <= enderDragon.getMaxHealth()/2)
						dragonPhase = 2;
					if((dragonPhase == 1 && dragonLevel > 1) || dragonLevel >= 4)
						enderDragon.setHealth(Math.min(enderDragon.getHealth()+1, enderDragon.getMaxHealth()));
					bar.setProgress(Math.max(enderDragon.getHealth()/enderDragon.getMaxHealth(), 0));
					bar.removeAll();
					List<Player> players = getNearPlayers();
					for(Player p : players) {
						bar.addPlayer(p);
						if(dragonLevel >= 4)
							p.setGliding(false);
					}
					if(eventTimer > 0)
						eventTimer--;
					else {
						if(dragonLevel == 1) {
							makeLvl1Events();
							eventTimer = 7;
						}
						else if(dragonLevel == 2) {
							eventTimer = 4;
							makeLvl2Events();
						}
						else if(dragonLevel == 3) {
							eventTimer = 2;
							makeLvl3Events();
						}
						else if(dragonLevel == 4) {
							eventTimer = 1;
							makeLvl4Events();
						}
					}
				}
			}, 0l, 20l);
		}
	}
	
	static void makeLvl1Events() {
		List<Player> players = getNearPlayers();
		if(players.size() == 0)
			return;
		Player randomPlayer = players.get((int)Uni.getRandomNumber(0, players.size()));
		int eventId = 0;
		if(dragonPhase == 1) {
			eventId = (int)Uni.getRandomNumber(0, 4);
		}
		else {
			eventId = (int)Uni.getRandomNumber(0, 3);
		}
		if(eventId == 0) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.ENDERMAN, 1, 3);
		}
		else if(eventId == 1) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.ENDERMITE, 2, 3);
		}
		else if(eventId == 2) {
			moveUpPlayer(randomPlayer, 15);
		}
		else if(eventId == 3) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.SHULKER, 1, 1);
		}
	}
	
	static void makeLvl2Events() {
		List<Player> players = getNearPlayers();
		if(players.size() == 0)
			return;
		Player randomPlayer = players.get((int)Uni.getRandomNumber(0, players.size()));
		int eventId = 0;
		if(dragonPhase == 1) {
			eventId = (int)Uni.getRandomNumber(1, 6);
		}
		else {
			eventId = (int)Uni.getRandomNumber(0, 5);
		}
		if(eventId == 0) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.MAGMA_CUBE, 1, 2);
		}
		else if(eventId == 1) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.ENDERMAN, 2, 3);
		}
		else if(eventId == 2) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.SKELETON, 2, 3);
		}
		else if(eventId == 3) {
			moveUpPlayer(randomPlayer, 20);
		}
		else if(eventId == 4) {
			PotionEffectType effectType = negativePotionEffects.get((int)Uni.getRandomNumber(0, negativePotionEffects.size()));
			randomPlayer.addPotionEffect(new PotionEffect(effectType, (int) Uni.getRandomNumber(100, 200), 0));
		}
		else if(eventId == 5) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.SHULKER, 1, 2);
		}
		
	}
	
	static void makeLvl3Events() {
		List<Player> players = getNearPlayers();
		if(players.size() == 0)
			return;
		Player randomPlayer = players.get((int)Uni.getRandomNumber(0, players.size()));
		int eventId = 0;
		if(dragonPhase == 1) {
			eventId = (int)Uni.getRandomNumber(0, 7);
		}
		else {
			eventId = (int)Uni.getRandomNumber(0, 6);
		}
		if(eventId == 0) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.MAGMA_CUBE, 1, 3);
		}
		else if(eventId == 1) {
			Collection<PotionEffect> s = randomPlayer.getActivePotionEffects();
			for(PotionEffect effect : s) {
				if(!negativePotionEffects.contains(effect.getType()))
					randomPlayer.removePotionEffect(effect.getType());
			}
			PotionEffectType effectType = negativePotionEffects.get((int)Uni.getRandomNumber(0, negativePotionEffects.size()));
			randomPlayer.addPotionEffect(new PotionEffect(effectType, (int) Uni.getRandomNumber(200, 320), 1));
		}
		else if(eventId == 2) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.ENDERMAN, 3, 4);
		}
		else if(eventId == 3) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.SKELETON, 3, 4);
		}
		else if(eventId == 4) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.WITHER_SKELETON, 1, 3);
		}
		else if(eventId == 5) {
			moveUpPlayer(randomPlayer, 25);
		}
		else if(eventId == 6) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.SHULKER, 1, 4);
		}
	}
	
	static void makeLvl4Events() {
		List<Player> players = getNearPlayers();
		if(players.size() == 0)
			return;
		Player randomPlayer = players.get((int)Uni.getRandomNumber(0, players.size()));
		int eventId = 0;
		eventId = (int)Uni.getRandomNumber(0, 6);
		if(eventId == 0) {
			Location loc = randomPlayer.getLocation();
			for(int x = -10; x < 10; x++) {
				for(int z = -10; z < 10; z++) {
					for(int y = -10; y < 10; y++) {
						Location loc2 = loc.clone();
						loc2.add(x, y, z);
						Block block = loc2.getBlock();
						if(block.getType() == Material.WATER)
							block.setType(Material.AIR);
					}
				}
			}
		}
		else if(eventId == 1) {
			Collection<PotionEffect> s = randomPlayer.getActivePotionEffects();
			for(PotionEffect effect : s) {
				if(!negativePotionEffects.contains(effect.getType()))
					randomPlayer.removePotionEffect(effect.getType());
			}
			PotionEffectType effectType = negativePotionEffects.get((int)Uni.getRandomNumber(0, negativePotionEffects.size()));
			randomPlayer.addPotionEffect(new PotionEffect(effectType, (int) Uni.getRandomNumber(200, 320), 1));
		}
		else if(eventId == 2) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.SKELETON, 3, 4);
		}
		else if(eventId == 3) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.WITHER_SKELETON, 1, 3);
		}
		else if(eventId == 4) {
			moveUpPlayer(randomPlayer, 25);
		}
		else if(eventId == 5) {
			spawnMobsAroundPlayer(randomPlayer, EntityType.SHULKER, 1, 4);
		}
	}
	
	static void giveReward() {
		float fullDamage = 0;
		int rewardPerDamage = 0;
		dragonDamage = sortByValue(dragonDamage);
		
		rewardPerDamage = dragonLevel;
		
		StringBuilder builder = new StringBuilder(MessageFormat.format("{0}Босс {1}> {2}Дракон {3}-го уровня повержен! Самые ценные игроки:", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GREEN, dragonLevel));		
		int playersCounter = 0;
		boolean firstDamagerRewardGived = false;
		for(Entry<String, Float> set : dragonDamage.entrySet()) {
			PlayerData playerData = Uni.getPlayerData(set.getKey());
			if(playerData != null) {
				int reward = (int)(set.getValue()/70)*rewardPerDamage;
				ItemUtils.givePlayerItem(playerData.player, new ItemStack(Material.NETHERITE_INGOT, reward));
				if(!firstDamagerRewardGived) {
					ItemUtils.givePlayerItem(playerData.player, CustomItems.getCustomItem("voidCrystal"+dragonLevel, 1));
					firstDamagerRewardGived = true;
				}
				playerData.player.giveExp(reward*100);
				playerData.player.sendMessage(MessageFormat.format("{0}Босс {1}> {2}Вы получили {1}{3} незеритовых слитков{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, String.valueOf(reward)));
				playersCounter++;
				builder.append(MessageFormat.format("\n    {2}{3}. {0}{4}{2} - {1}{5} урона{2}", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, playersCounter, playerData.player.getName(), set.getValue()));
			}
		}
		Bukkit.broadcastMessage(builder.toString());
	}
	
	static void moveUpPlayer(Player player, int height) {
		World world = player.getWorld();
		Location loc = world.getHighestBlockAt(player.getLocation()).getLocation();
		loc.add(0, height, 0);
		player.teleport(loc);
	}
	
	static void spawnMobsAroundPlayer(Player player, EntityType entityType, int minCount, int maxCount) {
		Location loc = player.getLocation();
		World world = loc.getWorld();
		int mobCount = (int)Uni.getRandomNumber(minCount, maxCount);
		for(int i = 0; i < mobCount; i++) {
			loc.add((int)Uni.getRandomNumber(-10, 10), 0, (int)Uni.getRandomNumber(-10, 10));
			loc = world.getHighestBlockAt(loc).getLocation();
			LivingEntity entity = (LivingEntity) world.spawnEntity(loc, entityType);
			entity.setMetadata("isDragonEntity", new FixedMetadataValue(Uni.instance, "true"));
			if(entity instanceof Creature)
				((Creature)entity).setTarget(player);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity entity = event.getDamager();
		if (entity instanceof Arrow) {
            Arrow arrow = (Arrow) entity;
            if (arrow.getShooter() instanceof Entity){
            	entity = (Entity) arrow.getShooter();
            }
        }
		if(entity.getMetadata("isDragonEntity").size() > 0) {
			event.setDamage(event.getDamage()*(1+(0.5f*dragonLevel)));
		}
		
		if(event.getEntity() == enderDragon) {
			if (event.getDamager() instanceof Arrow) {
	            Arrow arrow = (Arrow) event.getDamager();
	            if (arrow.getShooter() instanceof Player){
	            	Player p = (Player) arrow.getShooter();
	            	float damage = dragonDamage.getOrDefault(p.getName(), 0f);
					damage += event.getFinalDamage();
					dragonDamage.put(p.getName(), damage);
	            }
	        }
			else if(event.getDamager() instanceof Player) {
				float damage = dragonDamage.getOrDefault(event.getDamager().getName(), 0f);
				damage += event.getFinalDamage();
				dragonDamage.put(event.getDamager().getName(), damage);
			}
		}
	}
	
	public static List<Player> getNearPlayers() {
		List<Player> players = new ArrayList<Player>();
		if(enderDragon != null) {
			World world = enderDragon.getWorld();
			Location loc = enderDragon.getLocation();
			for(Player p : world.getPlayers()) {
				if(p.getLocation().distance(loc) <= 300) {
					players.add(p);
				}
			}
		}
		return players;
	}
	
	public static EnderDragon getEnderDragon() {
		return enderDragon;
	}
	
	public static int getEnderDragonLevel(EnderDragon enderDragon) {
		if(enderDragon.getMaxHealth() == 500)
			return 1;
		else if(enderDragon.getMaxHealth() == 750)
			return 2;
		else if(enderDragon.getMaxHealth() == 1000)
			return 3;
		else if(enderDragon.getMaxHealth() == 2000)
			return 4;
		return 0;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
	
	static {
		negativePotionEffects = new ArrayList<PotionEffectType>();
		negativePotionEffects.add(PotionEffectType.POISON);
		negativePotionEffects.add(PotionEffectType.SLOW);
		negativePotionEffects.add(PotionEffectType.WITHER);
	}
}
