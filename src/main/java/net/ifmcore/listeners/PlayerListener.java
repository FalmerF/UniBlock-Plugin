package net.ifmcore.listeners;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftCreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.ifmcore.CustomEnchants;
import net.ifmcore.CustomItems;
import net.ifmcore.CustomRecipes;
import net.ifmcore.ItemUtils;
import net.ifmcore.Localization;
import net.ifmcore.LogUtils;
import net.ifmcore.NBTEditor;
import net.ifmcore.NBTEditor.NBTCompound;
import net.ifmcore.Uni;
import net.ifmcore.VoidAltar;
import net.ifmcore.commands.ShopCommand;
import net.ifmcore.data.ConfigsManager;
import net.ifmcore.data.PlayerData;
import net.ifmcore.data.PlayerIPManager;
import net.ifmcore.entity.GuardianBoss;
import net.ifmcore.inventory.FilterWorkbenchInventory;
import net.minecraft.server.level.WorldServer;

public class PlayerListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerIPManager.addPlayerIp(player.getName(), player.getAddress().getHostName());
		Uni.updatePlayerData();
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayerIPManager.setPlayerOnlineAndPosition(player);
	}
	
	@EventHandler
	public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event) {
		Player player = event.getPlayer();
		if(event.getStatus() == Status.DECLINED) {
			player.kickPlayer(MessageFormat.format("{0}Чтобы играть на {1}UniBlock\n"
					+ "{0}включите ресурспак в настройках сервера:\n\n\n"
					+ "{0}1. Выберите {1}UniBLock{0} в списке серверов.\n"
					+ "{0}2. Нажмите {1}Настроить{0}.\n"
					+ "{0}3. Выберите {1}Наборы ресурсов: Включены{0}.\n"
					+ "{0}4. Можно заходить на сервер!", ChatColor.WHITE, ChatColor.GOLD));
		}
	}
	
	@EventHandler
	public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
		if(event.getBedEnterResult() != BedEnterResult.OK)
			return;
		int sleepingPlayers = 1;
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.isSleeping()) sleepingPlayers++;
		if(sleepingPlayers >= Uni.players.size()/2) {
			Bukkit.getWorld("world").setTime(8000);
			Bukkit.getWorld("world").setWeatherDuration(0);
			Bukkit.broadcastMessage(ChatColor.GREEN+"Ночь изменена на день!");
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Location deathLoc = player.getLocation();
		Uni.getPlayerConstantData(player.getName()).put("death_loc", deathLoc);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		Location deathLoc = (Location) Uni.getPlayerConstantData(player.getName()).getOrDefault("death_loc", null);
		if(deathLoc != null) {
			player.sendMessage(MessageFormat.format("{0}Death {1}> {2}Вы умерли на координатах {1}{3} {4} {5}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW, String.valueOf(deathLoc.getBlockX()), String.valueOf(deathLoc.getBlockY()), String.valueOf(deathLoc.getBlockZ())));
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		World world = player.getWorld();
		PlayerData playerData = Uni.getPlayerData(player);
		ItemStack item = player.getInventory().getItemInMainHand();
		
		if(System.currentTimeMillis() - playerData.lastInteract <= 5)
			return;
		else
			playerData.lastInteract = System.currentTimeMillis();
		
		try {
			if(CustomItems.isCustomItem(item) && CustomItems.getCustomItemName(item).equals("mobBottle") && block != null) {
				if(event.getAction() == Action.RIGHT_CLICK_BLOCK && item != null && player.getCooldown(item.getType()) <= 0 && NBTEditor.contains(item, "storedEntity")) {
					NBTCompound entityCompound = NBTEditor.getNBTCompound(NBTEditor.getString(item, "storedEntity"));
					Location loc = block.getLocation();
					loc.add(0.5f, 0, 0.5f);
					BlockFace blockFace = event.getBlockFace();
					if(blockFace == BlockFace.UP)
						loc.add(0, 1, 0);
					else if(blockFace == BlockFace.DOWN)
						loc.add(0, 2, 0);
					else if(blockFace == BlockFace.EAST)
						loc.add(1, 0, 0);
					else if(blockFace == BlockFace.WEST)
						loc.add(-1, 0, 0);
					else if(blockFace == BlockFace.SOUTH)
						loc.add(0, 0, 1);
					else if(blockFace == BlockFace.NORTH)
						loc.add(0, 0, -1);
					String entityType = NBTEditor.getString(item, "storedEntityType");
					if(block.getType() == Material.SPAWNER) {
						EntityType eType = EntityType.valueOf(entityType);
						if(!CustomItems.spawnerWhiteList.contains(eType)) {
							player.sendMessage(ChatColor.RED+"Это существо нельзя поместить в спавнер.");
							return;
						}
						CraftCreatureSpawner spawner = (CraftCreatureSpawner) block.getState();
						spawner.setSpawnedType(eType);
						block.getState().update();
					}
					else if(entityCompound != null) {
						Entity entity = world.spawnEntity(loc, EntityType.valueOf(entityType));
						NBTEditor.set(entity, entityCompound);
						entity.teleport(loc);
						
					}
					item.setAmount(item.getAmount()-1);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if(item != null && CustomItems.isCustomItem(item) && CustomItems.getCustomItemName(item).equals("customExpBottle") && player.getCooldown(item.getType()) <= 0) {
			item.setAmount(item.getAmount()-1);
			Location loc = player.getLocation();
			if(block != null)
				loc = block.getLocation();
			((ExperienceOrb)world.spawn(loc, ExperienceOrb.class)).setExperience(100);
			player.setCooldown(item.getType(), 2);
		}
		else if(item != null && CustomItems.isCustomItem(item) && CustomItems.getCustomItemName(item).equals("customExpBottle2") && player.getCooldown(item.getType()) <= 0) {
			item.setAmount(item.getAmount()-1);
			Location loc = player.getLocation();
			if(block != null)
				loc = block.getLocation();
			((ExperienceOrb)world.spawn(loc, ExperienceOrb.class)).setExperience(500);
			player.setCooldown(item.getType(), 2);
		}
		else if(item != null && player.isSneaking() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Map<String, Integer> enchants = CustomEnchants.getItemEnchants(item);
    		int lvl = enchants.getOrDefault("Tunnel", 0);
    		if(lvl > 0) {
    			boolean tunnelMode = !NBTEditor.getBoolean(item, "tunnelMode");
    			ItemStack newItem = NBTEditor.set(item, tunnelMode, "tunnelMode");
    			player.getInventory().setItemInMainHand(newItem);
    			if(tunnelMode)
    				player.sendMessage(MessageFormat.format("{0}Режим Туннеля {1}включен{0}.", ChatColor.YELLOW, ChatColor.GREEN));
    			else
    				player.sendMessage(MessageFormat.format("{0}Режим Туннеля {1}выключен{0}.", ChatColor.YELLOW, ChatColor.RED));
    		}
    		lvl = enchants.getOrDefault("Lumberjack", 0);
    		if(lvl > 0) {
    			boolean lumberjackMode = !NBTEditor.getBoolean(item, "lumberjackMode");
    			ItemStack newItem = NBTEditor.set(item, lumberjackMode, "lumberjackMode");
    			player.getInventory().setItemInMainHand(newItem);
    			if(lumberjackMode)
    				player.sendMessage(MessageFormat.format("{0}Режим Лесоруба {1}включен{0}.", ChatColor.YELLOW, ChatColor.GREEN));
    			else
    				player.sendMessage(MessageFormat.format("{0}Режим Лесоруба {1}выключен{0}.", ChatColor.YELLOW, ChatColor.RED));
    		}
    		if(CustomItems.isCustomItem(item) && CustomItems.getCustomItemName(item).equals("strangeHeart")) {
    			boolean enable = !NBTEditor.getBoolean(item, "enable");
    			ItemStack newItem = NBTEditor.set(item, enable, "enable");
    			player.getInventory().setItemInMainHand(newItem);
    			if(enable)
    				player.sendMessage(MessageFormat.format("{0}Режим магнита {1}включен{0}.", ChatColor.YELLOW, ChatColor.GREEN));
    			else
    				player.sendMessage(MessageFormat.format("{0}Режим магнита {1}выключен{0}.", ChatColor.YELLOW, ChatColor.RED));
    		}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && item != null && item.getType() == Material.ENDER_EYE && block.getType() == Material.END_PORTAL_FRAME) {
			boolean isAltar = VoidAltar.isAltar(block);
			boolean isVoidEye = CustomItems.getCustomItemName(item).equals("voidEye");
			if(isAltar && !isVoidEye || !isAltar && isVoidEye)
				event.setCancelled(true);
			else {
				event.setCancelled(true);
				EndPortalFrame frame = (EndPortalFrame) block.getBlockData();
				if(!frame.hasEye()) {
					ItemUtils.removeItem(player.getInventory(), CustomItems.getCustomItem("voidEye", 1));
					frame.setEye(true);
					block.setBlockData(frame);
					block.getWorld().playSound(block.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
				}
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && (item == null || item.getType() == Material.AIR) && block.getType() == Material.LECTERN) {
			boolean isAltar = VoidAltar.isAltar(block);
			if(isAltar) {
				VoidAltar.spawnEnderDragon(player, block);
				event.setCancelled(true);
			}
			else
				event.setCancelled(false);
		}
		
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.LECTERN) {
			String customName = NBTEditor.getString(block, "PublicBukkitValues", "customBlockName");
			if(customName != null && customName.equals("filterWorkbench")) {
				new FilterWorkbenchInventory(player);
				event.setCancelled(true);
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.HOPPER && player.isSneaking()) {
			if(item != null && CustomItems.getCustomItemName(item).equals("hopperFilter")) {
				event.setCancelled(true);
				String nbt = NBTEditor.getString(item, "filter");
				if(nbt == null || nbt.equals("")) {
					player.sendMessage(ChatColor.RED+"Для начала настройте фильтр!");
				}
				else {
					NBTEditor.set(block, nbt, "PublicBukkitValues", "filter");
					player.sendMessage(ChatColor.GREEN+"Настройки фильтра применены.");
				}
			}
			else if(item == null || item.getType() == Material.AIR) {
				event.setCancelled(true);
				String nbtJson = NBTEditor.getString(block, "PublicBukkitValues", "filter");
				if(nbtJson != null) {
					NBTCompound nbt = NBTEditor.getNBTCompound(nbtJson);
					int size = NBTEditor.getInt(nbt, "size");
					String items = "";
					for(int i = 0; i < size; i++) {
						Material mat = Material.valueOf(NBTEditor.getString(nbt, String.valueOf(i), "Material"));
						String itemNbtJson = NBTEditor.getString(nbt, String.valueOf(i), "NBT");
						ItemStack itemStack = new ItemStack(mat);
						if(itemNbtJson != null && !itemNbtJson.equals(""))
							itemStack = NBTEditor.getItemFromTag(NBTEditor.getNBTCompound(itemNbtJson));
						
						items += ChatColor.RED+ItemUtils.getItemName(itemStack)+ChatColor.RED+" • ";
					}
					player.sendMessage(ChatColor.GRAY+"Фильтр: "+items.substring(0, items.length()-3));
				}
				else {
					player.sendMessage(ChatColor.RED+"Для этой воронки не настроен фильтр.");
				}
			}
		}
	}
	
	@EventHandler()
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		ItemStack item = player.getInventory().getItemInMainHand();

		if(event.isCancelled())
			return;
		
		try {
			if(!entity.isSilent() && !CustomItems.mobBottleBlackList.contains(entity.getType()) && item != null && player.getCooldown(item.getType()) <= 0 && CustomItems.isCustomItem(item) && CustomItems.getCustomItemName(item).equals("mobBottle")) {
				ItemStack itemCopy = item.clone();
				itemCopy.setAmount(1);
				if(!NBTEditor.contains(itemCopy, "storedEntity") && (!NBTEditor.isCustomEntity(entity) || player.getGameMode() == GameMode.CREATIVE)) {
					itemCopy = NBTEditor.set(itemCopy, "minecraft:water", "Potion");
					itemCopy = NBTEditor.set(itemCopy, entity.getType().name(), "storedEntityType");
					itemCopy = NBTEditor.set(itemCopy, NBTEditor.getNBTCompound(entity).toJson(), "storedEntity");
					
					entity.setSilent(true);
					entity.remove();
					
					itemCopy.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					ItemMeta itemMeta = itemCopy.getItemMeta();
					List<String> lore = new ArrayList<>();
					lore.add(ChatColor.BLUE+ConfigsManager.localization.getOrDefault("entity.minecraft."+entity.getType().name().toLowerCase(), ""));
					itemMeta.setLore(lore);
					itemCopy.setItemMeta(itemMeta);
					
					item.setAmount(item.getAmount()-1);
					if(item.getAmount() <= 0)
						player.getInventory().setItemInMainHand(itemCopy);
					else
						player.getInventory().addItem(itemCopy);
					player.setCooldown(itemCopy.getType(), 5);
				}
			}
		}
		catch(Exception e) {}
	}
	
	@EventHandler
	public void onVillagerCareerChangeEvent(VillagerCareerChangeEvent event) {
		Villager villager = event.getEntity();
		if(event.getProfession() == Villager.Profession.LIBRARIAN && Uni.getRandomNumber(0, 100) <= 5) {
			villager.setProfession(Villager.Profession.LIBRARIAN);
			villager.setRecipes(ShopCommand.merchantRecipesLvl_1);
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onVillagerAcquireTradeEvent(VillagerAcquireTradeEvent event) {
		if(!(event.getEntity() instanceof Villager))
			return;
		Villager villager = (Villager) event.getEntity();
		int recipesCount = villager.getRecipeCount();
		if(recipesCount > 0 && villager.getRecipe(0).getResult().equals(ShopCommand.merchantRecipes.get(0).getResult())) {
			event.setCancelled(true);
			if(recipesCount == 3)
				villager.setRecipes(ShopCommand.merchantRecipesLvl_2);
			else if(recipesCount == 5) 
				villager.setRecipes(ShopCommand.merchantRecipesLvl_3);
			else if(recipesCount == 7) 
				villager.setRecipes(ShopCommand.merchantRecipesLvl_4);
			else if(recipesCount >= 8) 
				villager.setRecipes(ShopCommand.merchantRecipesLvl_5);
			else
				event.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onCraftItemEvent(CraftItemEvent event) {
		if(event.getWhoClicked() instanceof Player && event.getRecipe().getResult().equals(CustomRecipes.customBottleRecipe.getResult())) {
			Player player = (Player) event.getWhoClicked();
			if(player.getTotalExperience() >= 500 && !event.isShiftClick()) {
				player.giveExp(-500);
			}
			else {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		if(event.getFrom().distance(event.getTo()) >= 0.2f)
			Uni.getPlayerData(event.getPlayer()).triggerActivity();
	}
	
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			PlayerData playerData = Uni.getPlayerData((Player)event.getEntity());
			if(playerData.isAFK || !playerData.isAuthorized)
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			PlayerData playerData = Uni.getPlayerData((Player)event.getDamager());
			playerData.triggerActivity();
		}
	}

	@EventHandler
	public void onBlockPlaceEventEvent(BlockPlaceEvent event) {
		ItemStack item = event.getItemInHand();
		if(CustomItems.isCustomItem(item) && CustomItems.getCustomItemName(item).equals("filterWorkbench")) {
			NBTEditor.set(event.getBlock(), "filterWorkbench", "PublicBukkitValues", "customBlockName");
		}
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(block.getType() == Material.LECTERN) {
			String customName = NBTEditor.getString(block, "PublicBukkitValues", "customBlockName");
			if(customName != null && customName.equals("filterWorkbench")) {
				event.setDropItems(false);
				block.getWorld().dropItemNaturally(block.getLocation(), CustomItems.getCustomItem("filterWorkbench", 1));
			}
		}
	}
	
	@EventHandler
	public void InventoryMoveItemEvent(InventoryMoveItemEvent event) {
		Location loc = event.getDestination().getLocation();
		if(loc == null)
			return;
		Block block = loc.getBlock();
		if(block.getType() != Material.HOPPER)
			return;
		String nbt = NBTEditor.getString(block, "PublicBukkitValues", "filter");
		if(nbt != null && !nbt.equals("")) {
			event.setCancelled(!ItemUtils.checkItemInFilter(nbt, event.getItem()));
		}
	}
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		Player player = event.getPlayer();
		PlayerData playerData = Uni.getPlayerData(player);
		if(playerData != null && !playerData.isAuthorized()) {
			if(!command.startsWith("/l") && !command.startsWith("/login") && !command.startsWith("/reg") && !command.startsWith("/register")) {
				playerData.sendAuthorizeMessage();
				event.setCancelled(true);
			}
		}
		if(command.startsWith("/restart")) {
			event.setCancelled(true);
			String[] args = new String[0];
			String argsString = command.replace("/restart ", "").replace("/restart", "");
			if(!argsString.equals(""))
				args = argsString.split(" ");
			Uni.instance.getCommand("restart").execute(player, "restart", args);
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = Uni.getPlayerData(player);
		if(playerData != null && !playerData.isAuthorized()) {
			playerData.sendAuthorizeMessage();
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = Uni.getPlayerData(player);
		if(playerData != null && !playerData.isAuthorized()) {
			playerData.sendAuthorizeMessage();
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = Uni.getPlayerData(player);
		if(playerData != null && !playerData.isAuthorized()) {
			playerData.sendAuthorizeMessage();
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onExplosionPrimeEvent(ExplosionPrimeEvent event) {
		if(event.getEntityType() == EntityType.CREEPER)
			event.setRadius(0);
	}
	
	@EventHandler
	public void onEntityDropItemEvent(EntityDropItemEvent event) {
		if(event.getEntityType() == EntityType.PIGLIN) {
			if(Uni.getRandomNumber(0, 3000) <= 2) {
				event.getItemDrop().setItemStack(CustomItems.getCustomItem("eternalCoal", 1));
			}
		}
	}
	
	@EventHandler
	public void onLootGenerateEvent(LootGenerateEvent event) {
		if(event.getLootTable().getKey().getKey().equals("chests/end_city_treasure") && Uni.getRandomNumber(0, 100) <= 2) {
			event.getLoot().add(CustomItems.getCustomItem("defectiveChorus", 1));
		}
		else if(event.getLootTable().getKey().getKey().equals("chests/underwater_ruin_small") && Uni.getRandomNumber(0, 100) <= 5) {
			event.getLoot().add(CustomEnchants.makeEnchantBook("FireHook", 1));
		}
	}
	
	@EventHandler
	public void onFurnaceBurnEvent(FurnaceBurnEvent event) {
		if(CustomItems.getCustomItemName(event.getFuel()).equals("eternalCoal")) {
			event.setBurnTime(Integer.MAX_VALUE);
		}
	}
	
	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent event) {
		if(event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			if(player.getGameMode() == GameMode.CREATIVE) return;
			for(ItemStack item : event.getInventory().getContents()) {
				if(item != null && item.getType().name().endsWith("SPAWN_EGG") && player.getGameMode() != GameMode.CREATIVE) {
					item.setAmount(0);
				}
			}
			for(ItemStack item : player.getInventory().getContents()) {
				if(item != null && item.getType().name().endsWith("SPAWN_EGG") && player.getGameMode() != GameMode.CREATIVE) {
					item.setAmount(0);
				}
			}
		}
	}
	
	@EventHandler
	public void onSignChangeEvent(SignChangeEvent event) {
		String[] lines = event.getLines();
		for(int i = 0; i < lines.length; i++) {
			event.setLine(i, LogUtils.convertAmpCodes(lines[i]));
		}
	}
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		FishHook hook = event.getHook();
		Entity caughtEntity = event.getCaught();
		if(!hook.isInOpenWater()) return;
		if(event.getState() == State.CAUGHT_FISH) {
			event.setExpToDrop(event.getExpToDrop()*5);
			if((int)Uni.getRandomNumber(0, 7) <= 2) {
				event.setExpToDrop(event.getExpToDrop()*3);
				int eventId = (int) Uni.getRandomNumber(0, 3);
				if(eventId == 0) {
					LivingEntity entity = (LivingEntity) world.spawnEntity(hook.getLocation(), EntityType.SKELETON);
					entity.setVelocity(new Vector(0, 1f, 0));
					entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 600, 0));
					entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0));
					entity.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
					ItemStack bow = entity.getEquipment().getItemInMainHand();
					bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
					entity.getEquipment().setItemInMainHand(bow);
					((Creature)entity).setTarget(player);
				}
				else if(eventId == 1) {
					LivingEntity entity = (LivingEntity) world.spawnEntity(hook.getLocation(), EntityType.ZOMBIE);
					Vector playerLoc = player.getLocation().toVector();
					Vector hookLoc = hook.getLocation().toVector();
					entity.setVelocity(playerLoc.subtract(hookLoc).multiply(0.1f).add(new Vector(0, 2, 0)));
					entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0));
					ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);
					boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
					entity.getEquipment().setBoots(boots);
					entity.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
				}
				else if(eventId == 2) {
					int amplifier = 1;
					if(player.hasPotionEffect(PotionEffectType.REGENERATION))
						amplifier += player.getPotionEffect(PotionEffectType.REGENERATION).getAmplifier()+1;
					player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 300, amplifier));
				}
			}
			if(event.getCaught() instanceof Item && (int)Uni.getRandomNumber(0, 4) <= 1) {
				event.setExpToDrop(event.getExpToDrop()*2);
				Item item = (Item) event.getCaught();
				int itemId = (int) Uni.getRandomNumber(0, 4);
				if(itemId == 0) {
					item.setItemStack(new ItemStack(Material.GOLDEN_CARROT, (int) Uni.getRandomNumber(1, 4)));
				}
				else if(itemId == 1) {
					item.setItemStack(new ItemStack(Material.EMERALD, 1));
				}
				else if(itemId == 2) {
					item.setItemStack(CustomItems.getCustomItem("customExpBottle", 1));
				}
				else if(itemId == 3) {
					item.setItemStack(new ItemStack(Material.PRISMARINE_SHARD, (int) Uni.getRandomNumber(1, 4)));
				}
			}
			else if(event.getCaught() instanceof Item && (int)Uni.getRandomNumber(0, 200) <= 1) {
				event.setExpToDrop(event.getExpToDrop()*4);
				Item item = (Item) event.getCaught();
				item.setItemStack(CustomEnchants.makeEnchantBook("FireHook", 1));
			}
			else if(event.getCaught() instanceof Item && (int)Uni.getRandomNumber(0, 150) <= 1) {
				event.setExpToDrop(event.getExpToDrop()*4);
				Item item = (Item) event.getCaught();
				item.setItemStack(CustomEnchants.makeEnchantBook("LuckPlus", (int)Uni.getRandomNumber(4, 11)));
			}
		}
		if(event.getState() == State.CAUGHT_ENTITY && caughtEntity instanceof Item) {
			Item caughtItem = (Item) caughtEntity;
			if(CustomItems.getCustomItemName(caughtItem.getItemStack()).equals("decoyOfDeath") && caughtItem.isInWater() && (int)Uni.getRandomNumber(0, 100) <= 20) {
				GuardianBoss boss = new GuardianBoss(hook.getLocation());
				WorldServer worldServer = ((CraftWorld)world).getHandle();
				worldServer.addFreshEntity(boss, SpawnReason.CUSTOM);
				caughtItem.remove();
			}
		}
	}
}
