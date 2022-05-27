package net.ifmcore.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mkremins.fanciful.FancyMessage;
import net.ifmcore.AuthManager;
import net.ifmcore.BackupLoader;
import net.ifmcore.ClearLag;
import net.ifmcore.ItemUtils;
import net.ifmcore.Localization;
import net.ifmcore.NBTEditor;
import net.ifmcore.NBTEditor.NBTCompound;
import net.ifmcore.Uni;
import net.ifmcore.data.DataBaseManager;
import net.ifmcore.data.PlayerData;
import net.ifmcore.data.PlayerIPManager;
import net.ifmcore.inventory.CustomCreativeInventory;
import net.ifmcore.inventory.GuideInventory;
import net.ifmcore.inventory.InvSeeInventory;
import net.ifmcore.inventory.SettingsInventory;
import net.ifmcore.listeners.EventsListener;
import net.ifmcore.requests.Request;
import net.ifmcore.requests.RequestManager;
import net.ifmcore.requests.TeleportHereRequest;
import net.ifmcore.requests.TeleportRequest;
import net.ifmcore.utils.BadgesUtils;
import net.minecraft.world.level.ChunkCoordIntPair;

public class ChatCommands {
	static HashMap<String, Method> commands = new HashMap<String, Method>();
	
	enum Result {
		Good,
		BadArgs,
		ForPlayer,
		ForConsole
	}
	
	static {
		try {
			Method[] methods = ChatCommands.class.getDeclaredMethods();
			for(Method m : methods) {
				if(m.getName().endsWith("Command")) {
					commands.put(m.getName().replace("Command", ""), m);
				}
			}
		}
		catch(Exception e) {}
	}
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean isConsole = !(sender instanceof Player);
		Result result = Result.Good;
		PlayerData playerData = null;
		if(!isConsole)
			playerData = Uni.getPlayerData((Player) sender);
		String commandName = command.getName();
		if(!commandName.equals(""))
			commandName = commandName.substring(0, 1).toUpperCase() + commandName.substring(1);
		try {
			Method method = commands.getOrDefault(command.getName(), null);
			if(method != null)
				result = (Result) method.invoke(ChatCommands.class, sender, command, label, args, isConsole, commandName, playerData);
			else
				throw new Exception();
		}
		catch(Exception e) {
			e.printStackTrace();
			sender.sendMessage(Localization.parse("command.error", commandName));
		}
		if(result == Result.BadArgs)
			sender.sendMessage(Localization.parse("command.bad.args", Localization.parse("command."+command.getName()+".usage")));
		else if(result == Result.ForPlayer)
			Uni.log(Localization.parse("message.console.command.for.player"));
		else if(result == Result.ForConsole)
			Uni.log(Localization.parse("message.command.for.console"));
		return true;
    }
	
	static Result changepasswordCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(args.length < 1) return Result.BadArgs;
		if(playerData.isRegistered) {
        	if(!AuthManager.isValidPassword(sender.getName(), args[0])) {
        		sender.sendMessage(Localization.parse("message.command.changepassword.uncorrect.password"));
        		return Result.Good;
        	}
        	String firstPassword = args[1];
        	String secondPassword = args[2];
        	
        	if(!firstPassword.equals(secondPassword)) {
        		sender.sendMessage(Localization.parse("message.command.changepassword.uncorrect.newpassword1"));
        		return Result.Good;
        	}
        	else if(!AuthManager.isValidatePasswordChars(firstPassword)) {
        		sender.sendMessage(Localization.parse("message.command.changepassword.uncorrect.newpassword2"));
        		return Result.Good;
        	}
        	else if(firstPassword.length() < 8) {
        		sender.sendMessage(Localization.parse("message.command.changepassword.uncorrect.newpassword3", 8));
        		return Result.Good;
        	}
        	DataBaseManager.setData("auth", "password=\""+AuthManager.encodePassword(firstPassword)+"\"", "name=\""+sender.getName()+"\"");
        	sender.sendMessage(Localization.parse("message.command.changepassword.success"));
		}
    	return Result.Good;
	}
	
	static Result clearlagCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(args.length < 1) return Result.BadArgs;
		if(args[0].equals("all")) {
    		ClearLag.clearAllEntity();
    		sender.sendMessage(Localization.parse("message.command.clearlag.entity"));
    		return Result.Good;
		}
    	else if(args[0].equals("items")) {
    		ClearLag.clearItemsEntity();
    		sender.sendMessage(Localization.parse("message.command.clearlag.items"));
    		return Result.Good;
    	}
    	else if(args[0].equals("mobs")) {
    		ClearLag.clearMobsEntity();
    		sender.sendMessage(Localization.parse("message.command.clearlag.mobs"));
    		return Result.Good;
    	}
    	else if(args[0].equals("stats")) {
    		StringBuilder stats = new StringBuilder();
    		int allChunks = 0;
    		int allEntity = 0;
    		
    		for(World world : Bukkit.getWorlds()) {
    			allEntity += world.getEntities().size();
    			allChunks += world.getLoadedChunks().length;
    			stats.append("\n    ").append(world.getName()).append(": ").append(ChatColor.GOLD);
    			stats.append(world.getLoadedChunks().length).append(" Chunks").append(ChatColor.GRAY).append(" - ").append(ChatColor.GOLD);
    			stats.append(world.getEntities().size()).append(" Entity").append(ChatColor.YELLOW);
			}
    		
    		sender.sendMessage(Localization.parse("message.command.clearlag.stats", String.valueOf(allChunks), String.valueOf(allEntity), stats.toString()));
    		return Result.Good;
    	}
    	else return Result.BadArgs;
	}
	
	static Result gamemodeCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		GameMode mode;
		try {
    		mode = GameMode.valueOf(args[0].toUpperCase());
    	}
    	catch(Exception e) {
    		switch(args[0]) {
	    		case "0":
	    			mode = GameMode.SURVIVAL;
	    			break;
	    		case "1":
	    			mode = GameMode.CREATIVE;
	    			break;
	    		case "2":
	    			mode = GameMode.ADVENTURE;
	    			break;
	    		case "3":
	    			mode = GameMode.SPECTATOR;
	    			break;
    			default:
    				return Result.BadArgs;
    		}
    	}
		String modeName = Localization.parse("gamemode."+mode.name().toLowerCase());
		
		Player player = (Player) sender;
    	if(args.length > 1 && player.hasPermission("ifm.gm.plus")) {
    		playerData = Uni.getPlayerData(args[1]);
    		if(playerData != null) {
    			playerData.player.setGameMode(mode);
    			for(Player p : Bukkit.getServer().getOnlinePlayers())
    		        if(p.isOp())
    		        	p.sendMessage(Localization.parse("message.command.gamemodeplus", args[1], modeName));
    			return Result.Good;
        	}
        	else {
        		sender.sendMessage(Localization.parse("message.player.must.online", commandName));
        		return Result.Good;
        	}
    	}
    	
    	player.setGameMode(mode);
		for(Player p : Bukkit.getServer().getOnlinePlayers())
	        if(p.isOp())
	        	p.sendMessage(Localization.parse("message.command.gamemodeplus", player.getName(), modeName));
		return Result.Good;
	}
	
	static Result getitemnbtCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		Player player = (Player) sender;
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item != null) {
			NBTCompound tag = NBTEditor.getNBTCompound(item);
			sender.sendMessage(Localization.parse("message.command.getitemnbt", tag.toJson()));
		}
		else {
			sender.sendMessage(Localization.parse("message.command.getitemnbt.needitem"));
		}
		return Result.Good;
	}
	
	static Result loginCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(playerData.isAuthorized) {
    		sender.sendMessage(Localization.parse("message.command.login.already.authorized"));
    		return Result.Good;
    	}
    	else if(!playerData.isRegistered) {
    		playerData.sendAuthorizeMessage();
    		return Result.Good;
    	}
    	else if(args.length < 1) {
    		return Result.BadArgs;
    	}
    	if(AuthManager.isValidPassword(sender.getName(), args[0])) {
    		AuthManager.authorize((Player) sender);
    		sender.sendMessage(Localization.parse("message.command.login.authorized"));
    		return Result.Good;
    	}
    	else {
    		sender.sendMessage(Localization.parse("message.command.login.wrongpassword"));
    	}
		return Result.Good;
	}
	
	static Result registerCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
    	if(playerData.isAuthorized) {
    		sender.sendMessage(Localization.parse("message.command.register.already.authorized"));
    		return Result.Good;
    	}
    	else if(playerData.isRegistered) {
    		sender.sendMessage(Localization.parse("message.command.register.already.registered"));
    		return Result.Good;
    	}
    	else if(args.length < 2) {
    		return Result.BadArgs;
    	}
    	String firstPassword = args[0];
    	String secondPassword = args[1];
    	if(!firstPassword.equals(secondPassword)) {
    		sender.sendMessage(Localization.parse("message.command.register.wrong1"));
    		return Result.Good;
    	}
    	else if(!AuthManager.isValidatePasswordChars(firstPassword)) {
    		sender.sendMessage(Localization.parse("message.command.register.wrong2"));
    		return Result.Good;
    	}
    	else if(firstPassword.length() < 8) {
    		sender.sendMessage(Localization.parse("message.command.register.wrong3"));
    		return Result.Good;
    	}
    	AuthManager.register((Player) sender, secondPassword);
    	sender.sendMessage(Localization.parse("message.command.register.success"));
    	
    	((Player) sender).sendTitle(Localization.parse("title.welcome.1"), "", 5, 70, 50);
    	
		return Result.Good;
	}
	
	static Result seenCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(args.length < 1)
    		return Result.BadArgs;
    	List<String> accounts = PlayerIPManager.getLinkedAccounts(args[0]);
    	StringBuilder playersBuilder = new StringBuilder();
    	if(accounts.size() <= 1)
    		playersBuilder.append(ChatColor.RED).append("Нет данных  ");
    	else
        	for(String a : accounts) {
        		playersBuilder.append(a).append(", ");
        	}
    	playersBuilder = new StringBuilder(playersBuilder.substring(0, playersBuilder.length()-2));
    	playersBuilder.append(PlayerIPManager.getPlayerOnlineAndPosition(args[0]));
    	if(sender.getName().equalsIgnoreCase("console")) {
    		playersBuilder.insert(0, "Accounts: ");
    	}
    	else {
    		playersBuilder.insert(0, Localization.parse("message.command.seen.result", args[0]));
    		sender.sendMessage(playersBuilder.toString());
    	}
		return Result.Good;
	}
	
	static Result afkCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
    	playerData.setAFK(!playerData.isAFK);
		return Result.Good;
	}
	
	static Result callCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(args.length < 1) return Result.BadArgs;
		Player player = (Player) sender;
    	PlayerData targetPlayerData = Uni.getPlayerData(args[0]);
    	if(targetPlayerData != null) {
    		if(RequestManager.getRequest(targetPlayerData.player, TeleportRequest.class) != null) {
    			RequestManager.removeRequest(RequestManager.getRequest(targetPlayerData.player, TeleportRequest.class));
    		}
    		Location loc1 = player.getLocation();
    		Location loc2 = targetPlayerData.player.getLocation();
    		float distance = 0;
    		if(loc1.getWorld() != loc2.getWorld()) {
    			distance += loc1.distance(new Location(loc1.getWorld(), 0, 0, 0));
    			distance += loc2.distance(new Location(loc2.getWorld(), 0, 0, 0));
    		}
    		else {
    			distance = (float) loc1.distance(loc2);
    		}
    		int cost = Math.max((int)(distance/500), 2);
    		if(ItemUtils.itemsCount(player.getInventory(), new ItemStack(Material.DIAMOND)) < cost) {
    			player.sendMessage(Localization.parse("message.command.call.nodiamonds", cost));
    			return Result.Good;
    		}
    		TeleportRequest teleportRequest = new TeleportRequest(player, targetPlayerData.player, cost);
    		RequestManager.addRequest(teleportRequest);
    		player.sendMessage(Localization.parse("message.command.teleport.toplayer", targetPlayerData.player.getName()));
    		new FancyMessage(Localization.parse("message.command.teleport.totarget", player.getName()))
            .then("\n    Используйте команду ")
            .color(ChatColor.GRAY)
            .then("/tpaccept")
            .color(ChatColor.GOLD)
            .command("/tpaccept")
            .tooltip(ChatColor.GOLD+"/tpaccept")
            .then(" чтобы принять или ")
            .color(ChatColor.GRAY)
            .then("/tpdeny")
            .color(ChatColor.GOLD)
            .command("/tpdeny")
            .tooltip(ChatColor.GOLD+"/tpdeny")
            .then(" чтобы отклонить запрос.")
            .color(ChatColor.GRAY)
            .send(targetPlayerData.player);
    	}
    	else {
    		sender.sendMessage(Localization.parse("message.player.must.online", commandName));
    	}
		return Result.Good;
	}
	
	static Result invseeCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(args.length < 1) return Result.BadArgs;
		Player player = (Player) sender;
    	PlayerData targetPlayerData = Uni.getPlayerData(args[0]);
    	if(targetPlayerData != null) {
    		new InvSeeInventory(player, targetPlayerData.player);
    	}
    	else {
    		sender.sendMessage(Localization.parse("message.player.must.online", commandName));
    	}
		return Result.Good;
	}
	
	static Result getstatsCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(args.length < 2) return Result.BadArgs;
    	PlayerData targetPlayerData = Uni.getPlayerData(args[0]);
    	if(targetPlayerData != null) {
    		Material mat = Material.valueOf(args[1].toUpperCase());
    		int stats = targetPlayerData.player.getStatistic(Statistic.MINE_BLOCK, mat);
    		sender.sendMessage(Localization.parse("message.command.getstats.result", targetPlayerData.player.getName(), stats, mat.name()));
    	}
    	else {
    		sender.sendMessage(Localization.parse("message.player.must.online", commandName));
    	}
		return Result.Good;
	}
	
	static Result restartCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(args.length < 1) return Result.BadArgs;
		try {
			int time = Integer.parseInt(args[0]);
			Uni.serverRestartTime = time;
			sender.sendMessage(Localization.parse("message.command.restart.result", args[0]));
		} catch(Exception e) {
			return Result.BadArgs;
		}
		return Result.Good;
	}
	
	static Result badgeCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(args.length < 2) return Result.BadArgs;
		Player player = playerData.player;
		if(args[0].equals("select")) {
			String badge = args[1];
			if(badge.equals("none")) {
				playerData.selectedBadge = null;
				playerData.saveBadges();
				sender.sendMessage(Localization.parse("message.command.badge.hide"));
				return Result.Good;
			}
			if(!BadgesUtils.badges.containsKey(badge)) {
				sender.sendMessage(Localization.parse("message.command.badge.notfound"));
				return Result.Good;
			}
			if(!playerData.badges.contains(badge)) {
				sender.sendMessage(Localization.parse("message.command.badge.notaccess"));
				return Result.Good;
			}
			playerData.selectBadge(badge);
			new FancyMessage(Localization.parse("message.command.badge.select", player.getName()))
			.then(Localization.parse("message.badge.icon", badge))
			.tooltip(BadgesUtils.badges.getOrDefault(badge, ""))
			.send(player);
		}
		else if(player.isOp()) {
			if(args[0].equals("set")) {
				if(args.length < 4) {
					sender.sendMessage(Localization.parse("command.bad.args", Localization.parse("command.badge.usage.set")));
					return Result.Good;
				}
				PlayerData targetPlayerData = Uni.getPlayerData(args[1]);
				if(targetPlayerData == null) {
					sender.sendMessage(Localization.parse("message.player.must.online", commandName));
				}
				String badge = args[2];
				if(!BadgesUtils.badges.containsKey(badge)) {
					sender.sendMessage(Localization.parse("message.command.badge.notfound"));
					return Result.Good;
				}
				if(args[3].equals("true")) {
					targetPlayerData.setBadge(badge, true);
					new FancyMessage(Localization.parse("message.command.badge.set.true", targetPlayerData.player.getName()))
					.then(Localization.parse("message.badge.icon", badge))
					.tooltip(BadgesUtils.badges.getOrDefault(badge, ""))
					.send(player);
				}
				else {
					targetPlayerData.setBadge(badge, false);
					new FancyMessage(Localization.parse("message.command.badge.set.false", targetPlayerData.player.getName()))
					.then(Localization.parse("message.badge.icon", badge))
					.tooltip(BadgesUtils.badges.getOrDefault(badge, ""))
					.send(player);
				}
			}
			else if(args[0].equals("give")) {
				if(args.length < 3) {
					sender.sendMessage(Localization.parse("command.bad.args", Localization.parse("command.badge.usage.give")));
					return Result.Good;
				}
				PlayerData targetPlayerData = Uni.getPlayerData(args[1]);
				if(targetPlayerData == null) {
					sender.sendMessage(Localization.parse("message.player.must.online", commandName));
				}
				String badge = args[2];
				if(!BadgesUtils.badges.containsKey(badge)) {
					sender.sendMessage(Localization.parse("message.command.badge.notfound"));
					return Result.Good;
				}
				if(targetPlayerData.badges.contains(badge)) {
					sender.sendMessage(Localization.parse("message.command.badge.give.alreadyhas", targetPlayerData.player.getName()));
					return Result.Good;
				}
				targetPlayerData.giveBadge(badge);
			}
			else {
				sender.sendMessage(Localization.parse("command.bad.args", Localization.parse("command.badge.usage.admin")));
				return Result.Good;
			}
		}
		else {
			return Result.Good;
		}
		return Result.Good;
	}
	
	static Result settingsCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
    	new SettingsInventory(playerData.player);
		return Result.Good;
	}
	
	static Result guideCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
    	GuideInventory.openPage(playerData.player, 0);
		return Result.Good;
	}
	
	static Result citemsCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(playerData.player.getGameMode() != GameMode.CREATIVE) return Result.Good;
		playerData.player.openInventory(CustomCreativeInventory.inv);
		return Result.Good;
	}
	
	static Result toggleeventCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(playerData.player.getGameMode() != GameMode.CREATIVE) return Result.Good;
		Uni.eventActive = !Uni.eventActive;
		if(Uni.eventActive)
			Bukkit.broadcastMessage(Localization.parse("message.command.toggleevent.true"));
		else
			Bukkit.broadcastMessage(Localization.parse("message.command.toggleevent.false"));
		return Result.Good;
	}
	
	static Result eventreloadCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(playerData.player.getGameMode() != GameMode.CREATIVE) return Result.Good;
		EventsListener.configureEvent();
		sender.sendMessage(Localization.parse("message.command.eventreload"));
		return Result.Good;
	}
	
	static Result tpacceptCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		Player player = (Player) sender;
    	Request teleportRequest = RequestManager.getRequest(player, TeleportRequest.class);
    	if(teleportRequest == null)
    		teleportRequest = RequestManager.getRequest(player, TeleportHereRequest.class);
    	if(teleportRequest != null) {
    		teleportRequest.acceptRequest();
    	}
    	else {
    		sender.sendMessage(Localization.parse("message.command.tpaccept.norequests"));
    	}
		return Result.Good;
	}
	
	static Result tpdenyCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		Player player = (Player) sender;
    	Request teleportRequest = RequestManager.getRequest(player, TeleportRequest.class);
    	if(teleportRequest == null)
    		teleportRequest = RequestManager.getRequest(player, TeleportHereRequest.class);
    	if(teleportRequest != null) {
    		teleportRequest.rejectRequest();
    	}
    	else {
    		sender.sendMessage(Localization.parse("message.command.tpaccept.norequests"));
    	}
		return Result.Good;
	}
	
	static Result homeCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		Player player = (Player) sender;
    	Location home = player.getBedSpawnLocation();
    	if(args.length > 0 && sender.isOp()) {
	    	PlayerData targetPlayerData = Uni.getPlayerData(args[0]);
	    	if(targetPlayerData != null) {
	    		home = targetPlayerData.player.getBedSpawnLocation();
	    	}
	    	else {
	    		sender.sendMessage(Localization.parse("message.player.must.online", commandName));
	    		return Result.Good;
	    	}
    	}
    	if(home == null) {
    		player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    	}
    	else {
    		player.teleport(home);
    	}
    	player.sendMessage(Localization.parse("message.command.home.result"));
		return Result.Good;
	}
	
	static Result giveitemCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(!isConsole) return Result.ForConsole;
		if(args.length < 3) return Result.BadArgs;
    	PlayerData targetPlayerData = Uni.getPlayerData(args[0]);
		try {
			ItemStack item = new ItemStack(Material.valueOf(args[1]), Integer.parseInt(args[2]));
			if(targetPlayerData != null) {
				targetPlayerData.player.sendMessage(Localization.parse("message.player.giveitem", ItemUtils.getItemName(item), item.getAmount()));
				ItemUtils.givePlayerItem(targetPlayerData.player, item);
				sender.sendMessage("Item gived to player!");
			}
			else {
				DataBaseManager.insertData("items", "\""+args[0].toLowerCase()+"\", \""+item.getType().name()+"\", \""+item.getAmount()+"\"");
				sender.sendMessage("Item gived to player when he join to game!");
			}
		} catch(Exception e) {
			return Result.BadArgs;
		}
		return Result.Good;
	}
	
	static Result lightingCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(args.length < 1) return Result.BadArgs;
		Player player = (Player) sender;
    	PlayerData targetPlayerData = Uni.getPlayerData(args[0]);
    	if(targetPlayerData != null) {
    		targetPlayerData.player.getWorld().strikeLightning(targetPlayerData.player.getLocation());
    		player.sendMessage(Localization.parse("message.command.lighting.result", targetPlayerData.player.getName()));
    	}
    	else {
    		sender.sendMessage(Localization.parse("message.player.must.online", commandName));
    	}
		return Result.Good;
	}
	
	static Result enderseeCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(args.length < 1) return Result.BadArgs;
		Player player = (Player) sender;
    	PlayerData targetPlayerData = Uni.getPlayerData(args[0]);
    	if(targetPlayerData != null) {
    		player.openInventory(targetPlayerData.player.getEnderChest());
    	}
    	else {
    		sender.sendMessage(Localization.parse("message.player.must.online", commandName));
    	}
		return Result.Good;
	}
	
	static Result setplaytimeCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(args.length < 2) return Result.BadArgs;
		Player player = (Player) sender;
    	PlayerData targetPlayerData = Uni.getPlayerData(args[0]);
    	int time = 0;
    	try {
    		time = Integer.parseInt(args[1]);
    	} catch(Exception e) {
    		return Result.BadArgs;
    	}
    	if(targetPlayerData != null) {
    		targetPlayerData.player.setStatistic(Statistic.PLAY_ONE_MINUTE, time*20);
    		sender.sendMessage(Localization.parse("message.command.setplaytime.result", targetPlayerData.player.getName(), args[1]));
    	}
    	else {
    		sender.sendMessage(Localization.parse("message.player.must.online", commandName));
    	}
		return Result.Good;
	}
	
	static Result loadchunkCommand(CommandSender sender, Command command, String label, String[] args, boolean isConsole, String commandName, PlayerData playerData) {
		if(isConsole) return Result.ForPlayer;
		if(args.length < 1) return Result.BadArgs;
		
    	Chunk chunk = playerData.player.getLocation().getChunk();
		World world = chunk.getWorld();
		
    	File backupFile = new File(Uni.getPluginFolderPath()+"/../../Backups/backup-"+args[0]+"-"+world.getName()+".zip");
    	if(!backupFile.exists()) {
    		sender.sendMessage(Localization.parse("message.command.loadchunk.notfound", args[0]));
    		return Result.Good;
    	}
    	BackupLoader.loadChunk(chunk, args[0], backupFile);
		return Result.Good;
	}
	

}
