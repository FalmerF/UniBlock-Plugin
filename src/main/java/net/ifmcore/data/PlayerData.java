package net.ifmcore.data;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import mkremins.fanciful.FancyMessage;
import net.ifmcore.Uni;
import net.ifmcore.utils.BadgesUtils;
import net.ifmcore.AuthManager;
import net.ifmcore.ItemUtils;
import net.ifmcore.Lag;
import net.ifmcore.Localization;

public class PlayerData {
	public Player player;
	public String whoLastMessage = "";
	public boolean isAFK = false;
	public boolean isAuthorized = false;
	public boolean isRegistered = false;
	public int lastActivity = 0;
	public HashMap<String, Object> settings = new HashMap<String, Object>();
	private Map<String, Integer> messagesDelay = new TreeMap<String, Integer>();
	public List<String> badges = new ArrayList<String>();
	public String selectedBadge;
	public long lastInteract = 0;
	public int trevelled = 0;
	public boolean keepInventory = false;
	
	public PlayerData(Player player) {
		this.player = player;
		this.lastActivity = Uni.updateTime;
		
		isRegistered = AuthManager.isRegistered(player);
		if(isRegistered)
			isAuthorized = AuthManager.isAuthorized(player);
		
		if(!isAuthorized)
			sendAuthorizeMessage();
		
		if(!DataBaseManager.checkInDataBase("settings", "name", "name=\""+player.getName()+"\"")) {
			DataBaseManager.insertData("settings", "\""+player.getName()+"\", \"\"");
			loadDefaultSettings();
		}
		else
			loadSettings();
		if(!DataBaseManager.checkInDataBase("stats", "badges", "name=\""+player.getName()+"\"")) {
			DataBaseManager.insertData("stats", "\""+player.getName()+"\", \"\"");
		}
		else
			loadBadges();
		
		List<String[]> items = DataBaseManager.getDataAll("items", new String[] {"item", "amount"}, "name=\""+player.getName().toLowerCase()+"\"");
		for(String[] data : items) {
			try {
				ItemStack item = new ItemStack(Material.valueOf(data[0]), Integer.parseInt(data[1]));
				player.sendMessage(Localization.parse("message.player.giveitem", ItemUtils.getItemName(item), item.getAmount()));
				ItemUtils.givePlayerItem(player, item);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		DataBaseManager.deleteData("items", "name=\""+player.getName().toLowerCase()+"\"");
	}
	
	void loadSettings() {
		this.settings.clear();
		String settings = DataBaseManager.getData("settings", new String[] {"settings"}, "name=\""+player.getName()+"\"")[0];
		String[] params = settings.split(" ");
		for(String param : params) {
			String[] data = param.split("=");
			try {
				if(data[1].equals("true") || data[1].equals("false")) {
					this.settings.put(data[0], data[1].equals("true"));
				}
				else {
					this.settings.put(data[0], Float.parseFloat(data[1]));
				}
			} catch(Exception e) {
				Uni.log("Error on parsing parametr \""+param+"\" of player "+player.getName());
			}
		}
	}
	
	void loadDefaultSettings() {
		settings.clear();
		settings.put("showStatistic", true);
		settings.put("chatHints", true);
		saveSettings();
	}
	
	void saveSettings() {
		String settings = "";
		for(Entry<String, Object> param : this.settings.entrySet()) {
			if(!settings.equals(""))
				settings += " ";
			settings += param.getKey()+"="+String.valueOf(param.getValue());
		}
		DataBaseManager.setData("settings", "settings=\""+settings+"\"", "name=\""+player.getName()+"\"");
	}
	
	public void setSettingParam(String param, Object value) {
		settings.put(param, value);
		saveSettings();
	}
	
	public boolean getBoolParam(String param) {
		return (boolean) settings.getOrDefault(param, false);
	}
	
	public float getFloatParam(String param) {
		return (float) settings.getOrDefault(param, 0);
	}
	
	public void loadBadges() {
		this.badges.clear();
		String badgesString = DataBaseManager.getData("stats", new String[] {"badges"}, "name=\""+player.getName()+"\"")[0];
		String[] badgesArray = badgesString.split(" ");
		if(badgesArray.length >= 0) {
			selectedBadge = badgesArray[0];
			if(selectedBadge.equals("null"))
				selectedBadge = null;
			for(int i = 1; i < badgesArray.length; i++) {
				badges.add(badgesArray[i]);
			}
		}
		else {
			selectedBadge = null;
		}
	}
	
	public void saveBadges() {
		String badgesString = selectedBadge;
		for(String b : badges)
			badgesString += " "+b;
		DataBaseManager.setData("stats", "badges=\""+badgesString+"\"", "name=\""+player.getName()+"\"");
	}
	
	public void setBadge(String badge, boolean active) {
		if(!badges.contains(badge) && active) {
			badges.add(badge);
			saveBadges();
		}
		else if(badges.contains(badge) && !active) {
			badges.remove(badge);
			saveBadges();
		}
	}
	
	public void giveBadge(String badge) {
		if(!badges.contains(badge)) {
			badges.add(badge);
			new FancyMessage(Localization.parse("message.badge.give", player.getName()))
			.then(Localization.parse("message.badge.icon", badge))
			.tooltip(BadgesUtils.badges.getOrDefault(badge, ""))
			.send(Bukkit.getOnlinePlayers());
			player.sendMessage(ChatColor.GREEN+"Используйте /badge select, чтобы установить значек");
			saveBadges();
		}
	}
	
	public void selectBadge(String badge) {
		if(badges.contains(badge)) {
			selectedBadge = badge;
			saveBadges();
		}
	}
	
	// Every 1 seconds
	public void update() {
		if(Uni.updateTime-lastActivity >= 300 && !isAFK)
			setAFK(true);
		updateTab();
		updateScoreBoard();
		BadgesUtils.checkAchievementsComplete(this);
		
		for(Entry<String, Integer> m : messagesDelay.entrySet()) {
			int delay = m.getValue();
			delay -= 20;
			if(delay <= 0)
				messagesDelay.remove(m.getKey());
			else
				messagesDelay.put(m.getKey(), delay);
		}
		
		int totalSeconds = player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20;
		if(totalSeconds < 57600 && !keepInventory) {
			keepInventory = true;
			player.sendMessage(Localization.parse("message.player.getkeepinventory", ticksToTime((57600-totalSeconds)*20)));
		}
		else if(totalSeconds >= 57600 && keepInventory) {
			keepInventory = false;
			player.sendMessage(Localization.parse("message.player.losekeepinventory"));
		}
		
		if(totalSeconds >= 1800000) giveBadge("\uE265");
		else if(totalSeconds >= 720000) giveBadge("\uE258");
		
		if(player.getStatistic(Statistic.TRADED_WITH_VILLAGER) >= 2000) giveBadge("\uE044");
	}
	
	public void updateTab() {
		player.setPlayerListHeader(MessageFormat.format("\n{0}{5}\n\n\n"
				+ "   {0}Игроков онлайн: {2}{3}{0} из {2}{4}   \n", ChatColor.WHITE, ChatColor.GOLD, ChatColor.YELLOW, Uni.players.size(), Bukkit.getMaxPlayers(), Uni.serverName));
		player.setPlayerListFooter("\n"+ChatColor.GOLD+"TPS: "+Lag.getTPS());
		String prefix = "";
		if(isAFK)
			prefix += ChatColor.GRAY+"AFK ";
		if(selectedBadge != null)
			prefix += ChatColor.GOLD+"["+ChatColor.WHITE+selectedBadge+ChatColor.GOLD+"] ";
		ChatColor color = ChatColor.GREEN;
		World world = player.getWorld();
		if(world.getName().equals("world_nether"))
			color = ChatColor.RED;
		else if(world.getName().equals("world_the_end"))
			color = ChatColor.LIGHT_PURPLE;
		player.setPlayerListName(prefix + color + player.getName());
	}
	
	public void updateScoreBoard() {
		if(!getBoolParam("showStatistic"))
			return;
		try {
	    	Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
	    	Objective objective = board.registerNewObjective("Stats", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName(ChatColor.GRAY+"      "+ChatColor.YELLOW+ChatColor.BOLD+"Статистика"+ChatColor.GRAY+"      ");
	    	
			objective.getScore(" ").setScore(11);
			
	    	objective.getScore(ChatColor.GREEN+"Наиграно").setScore(10);
	    	objective.getScore("\uE068 "+ChatColor.GOLD+ticksToTime(player.getStatistic(Statistic.PLAY_ONE_MINUTE))).setScore(9);
	    	
	    	objective.getScore(ChatColor.GREEN+"Пройдено").setScore(8);
	    	objective.getScore("\uE110 "+ChatColor.GOLD+getAllTrevelled()).setScore(7);
	    	objective.getScore("  ").setScore(6);
	    	
	    	objective.getScore(ChatColor.GREEN+"Мобов убито").setScore(5);
	    	objective.getScore("\uE050 "+ChatColor.RED+String.valueOf(player.getStatistic(Statistic.MOB_KILLS))).setScore(4);
	    	
	    	objective.getScore(ChatColor.GREEN+"Смертей").setScore(3);
	    	objective.getScore("\uE184 "+ChatColor.RED+String.valueOf(player.getStatistic(Statistic.DEATHS))).setScore(2);
	    	objective.getScore("     ").setScore(1);
	    	
	    	player.setScoreboard(board);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getAllTrevelled() {
		trevelled = 0;
		trevelled += player.getStatistic(Statistic.WALK_ONE_CM);
		trevelled += player.getStatistic(Statistic.WALK_ON_WATER_ONE_CM);
		trevelled += player.getStatistic(Statistic.WALK_UNDER_WATER_ONE_CM);
		trevelled += player.getStatistic(Statistic.SPRINT_ONE_CM);
		trevelled += player.getStatistic(Statistic.AVIATE_ONE_CM);
		trevelled += player.getStatistic(Statistic.BOAT_ONE_CM);
		trevelled += player.getStatistic(Statistic.CLIMB_ONE_CM);
		trevelled += player.getStatistic(Statistic.CROUCH_ONE_CM);
		trevelled += player.getStatistic(Statistic.FALL_ONE_CM);
		trevelled += player.getStatistic(Statistic.FLY_ONE_CM);
		trevelled += player.getStatistic(Statistic.HORSE_ONE_CM);
		trevelled += player.getStatistic(Statistic.MINECART_ONE_CM);
		trevelled += player.getStatistic(Statistic.PIG_ONE_CM);
		trevelled += player.getStatistic(Statistic.STRIDER_ONE_CM);
		trevelled += player.getStatistic(Statistic.SWIM_ONE_CM);
		String trevelledString = (Math.round(trevelled/1000.0f)/100.0)+"км";
		trevelled /= 100000;
		return trevelledString;
	}
	
	public static String ticksToTime(int ticks) {
		String time = "";
		int totalSec = ticks/20;
		int days = (totalSec-(totalSec%86400))/86400;
		int hours = ((totalSec%86400)-(totalSec%3600))/3600;
		int mins = ((totalSec%3600)-(totalSec%60))/60;
		int secs = totalSec%60;
		if(days > 0) time += days+"д ";
		if(hours > 0) time += hours+"ч ";
		if(mins > 0) time += mins+"м ";
		if(secs > 0) time += secs+"с ";
		return time;
	}
	
	public String getName() {
		return player.getName();
	}
	
	public void triggerActivity() {
		this.lastActivity = Uni.updateTime;
		if(isAFK)
			setAFK(false);
	}
	
	public void setAFK(boolean AFK) {
		isAFK = AFK;
		if(isAFK) {
			this.lastActivity = Uni.updateTime;
			Bukkit.broadcastMessage(MessageFormat.format("{0}Игрок {1} теперь AFK.", ChatColor.GRAY, player.getName()));
		}
		else
			Bukkit.broadcastMessage(MessageFormat.format("{0}Игрок {1} вышел из AFK.", ChatColor.GRAY, player.getName()));
	}
	
	public void sendAuthorizeMessage() {
		if(!isRegistered)
			sendMessageWithDelay(Localization.parse("message.need.registered"), "register", 100);
		else if(!isAuthorized)
			sendMessageWithDelay(Localization.parse("message.need.authorized"), "authorize", 100);
	}
	
	public boolean isAuthorized() {
		if(isRegistered && isAuthorized) {
			return true;
		}
		return false;
	}
	
	public void sendMessageWithDelay(String message, String key, int delay) {
		int currentDelay = messagesDelay.getOrDefault(key, 0);
		if(currentDelay <= 0) {
			player.sendMessage(message);
			messagesDelay.put(key, delay);
		}
	}
}
