package net.ifmcore;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.ifmcore.commands.BadgeTabCompleter;
import net.ifmcore.commands.ChatCommands;
import net.ifmcore.commands.DeathpointCommand;
import net.ifmcore.commands.KillDragonCommand;
import net.ifmcore.commands.MessageCommand;
import net.ifmcore.commands.RecipesCommand;
import net.ifmcore.commands.ReloadcfgCommand;
import net.ifmcore.commands.ReplyCommand;
import net.ifmcore.commands.SetMaxHpCommand;
import net.ifmcore.commands.ShopCommand;
import net.ifmcore.commands.TradeCommand;
import net.ifmcore.data.ConfigsManager;
import net.ifmcore.data.DataBaseManager;
import net.ifmcore.data.PlayerData;
import net.ifmcore.entity.GuardianBoss;
import net.ifmcore.inventory.CustomCreativeInventory;
import net.ifmcore.inventory.GuideInventory;
import net.ifmcore.inventory.RecipeListInventory;
import net.ifmcore.listeners.ChatListener;
import net.ifmcore.listeners.EnchantsListener;
import net.ifmcore.listeners.EnderDragonListener;
import net.ifmcore.listeners.EntityListener;
import net.ifmcore.listeners.EventsListener;
import net.ifmcore.listeners.LogsListener;
import net.ifmcore.listeners.PlayerListener;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeBase;
import net.minecraft.world.entity.ai.attributes.AttributeDefaults;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;

public class Uni extends JavaPlugin implements Listener {
	public static Uni instance;
	public static File pluginFolder;
	public static Random rand = new Random();
	public static List<PlayerData> players = new ArrayList<PlayerData>();
	public static String serverName = "";
	public static Map<String, Map<String, Object>> playerData = new TreeMap<String, Map<String, Object>>();
	public static List<String> chatHints = new ArrayList<String>();
	public static int updateTime = 0;
	public static int serverRestartTime = -10;
	public static boolean eventActive = false;
	public static int startDay = new Date().getDay();
	
	URLClassLoader childClassLoader;
	
	@Override
	public void onEnable() {
		instance = this;
		
		PluginManager pluginManager = Bukkit.getPluginManager();
        
		getServer().getConsoleSender().sendMessage("");
		getServer().getConsoleSender().sendMessage(ChatColor.BLUE+"  _____     _ _____         _             ");
		getServer().getConsoleSender().sendMessage(ChatColor.BLUE+" |  |  |___|_|   __|_ _ ___| |_ ___ _____  " + ChatColor.DARK_GRAY + "UniSystem v0.0.1");
		getServer().getConsoleSender().sendMessage(ChatColor.BLUE+" |  |  |   | |__   | | |_ -|  _| -_|     | " + ChatColor.DARK_GRAY + "by Falmer");
		getServer().getConsoleSender().sendMessage(ChatColor.BLUE+" |_____|_|_|_|_____|_  |___|_| |___|_|_|_| " + ChatColor.DARK_GRAY + "https://github.com/FalmerF");
		getServer().getConsoleSender().sendMessage(ChatColor.BLUE+"                   |___|                  ");
		getServer().getConsoleSender().sendMessage("");
		
		checkAndMakePluginFolder();
		new DataBaseManager();
		new ConfigsManager();
//		new MapSaver();
		new LogUtils();
		pluginManager.registerEvents(new PlayerListener(), this);
		pluginManager.registerEvents(new ChatListener(), this);
		pluginManager.registerEvents((Listener) this, this);
		pluginManager.registerEvents(new CustomEnchants(), this);
		pluginManager.registerEvents(new EnchantsListener(), this);
		pluginManager.registerEvents(new EnderDragonListener(), this);
		pluginManager.registerEvents(new RecipeListInventory(), this);
		pluginManager.registerEvents(new CustomCreativeInventory(), this);
		pluginManager.registerEvents(new GuideInventory(), this);
		pluginManager.registerEvents(new EventsListener(), this);
		pluginManager.registerEvents(new LogsListener(), this);
		pluginManager.registerEvents(new EntityListener(), this);
		
		this.getCommand("m").setExecutor(new MessageCommand());
		this.getCommand("r").setExecutor(new ReplyCommand());
		this.getCommand("reloadcfg").setExecutor(new ReloadcfgCommand());
		this.getCommand("setmaxhp").setExecutor(new SetMaxHpCommand());
		this.getCommand("trade").setExecutor(new TradeCommand());
		this.getCommand("shop").setExecutor(new ShopCommand());
		this.getCommand("killdragon").setExecutor(new KillDragonCommand());
		this.getCommand("recipes").setExecutor(new RecipesCommand());
		
		new BadgeTabCompleter();
		
		org.apache.logging.log4j.core.Logger logger;
        logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        logger.addFilter(new Log4jFilter());
		
		new ClearLag();
		CustomRecipes.MakeRecipes();
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new  Runnable(){
			public void run() {
				serverRestartUpdate();
				updatePlayerData();
				updateTime += 1;
			}
		}, 5l, 20l);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new  Runnable(){
			public void run() {
				String hint = ChatColor.GRAY+" > "+chatHints.get((int) getRandomNumber(0, chatHints.size()));
				for(PlayerData playerData : players)
					if(playerData.getBoolParam("chatHints"))
						playerData.player.sendMessage(hint);
			}
		}, 8l, 12000l);
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
		log("Finished loading");
    }
	
	void checkAndMakePluginFolder() {
		pluginFolder = new File(getPluginFolderPath());
		if(pluginFolder.isDirectory() && !pluginFolder.exists()) {
			pluginFolder.mkdirs();
			log("Maked IFM folder");
		}
	}
	
	public static String getPluginFolderPath() {
		try {
			return new File(Uni.class.getProtectionDomain().getCodeSource().getLocation()
			    .toURI()).getParentFile().getPath()+"/UniSystem/";
		} catch(Exception e) {
			return "";
		}
	}
	
	public static float getRandomNumber(int start, int end) {
		return rand.nextFloat()*(end-start)+start;
	}
	
	public static void updatePlayerData() {
		PlayerData[] playersArray = players.toArray(new PlayerData[] {});
    	
    	for(Player player : Bukkit.getOnlinePlayers()){
    		cycle:
    		{
	    		for(PlayerData playerData : playersArray) {
	    			if(player == playerData.player)
	    				break cycle;
	        	}
	    		players.add(new PlayerData(player));
    		}
    	}
    	
    	playersArray = players.toArray(new PlayerData[] {});
    	for(PlayerData playerData : playersArray) {
    		if(!Bukkit.getOnlinePlayers().contains(playerData.player))
    			players.remove(playerData);  
    		else {
        		playerData.update();
    		}
    	}
    }
    
    public static PlayerData playerData(Player player) {
    	for(PlayerData playerData : players) {
    		if(playerData.player == player)
    			return playerData;
    	}
    	return null;
    }
    
    public static PlayerData getPlayerData(String name) {
    	for(PlayerData playerData : players) {
    		if(playerData.player.getName().equalsIgnoreCase(name))
    			return playerData;
    	}
    	return null;
    }
    
    public static PlayerData getPlayerData(HumanEntity humanEntity) {
    	for(PlayerData playerData : players) {
    		if(playerData.player == humanEntity)
    			return playerData;
    	}
    	return null;
    }
    
    public static void log(String message) {
    	log(message, ChatColor.GOLD);
    }
    
    public static void log(String message, ChatColor color) {
    	Uni.instance.getServer().getConsoleSender().sendMessage(color+"[Uni] "+message);
    }
    
    public static void logToOp(String message) {
    	for(Player p : Bukkit.getOnlinePlayers())
    		if(p.isOp()) p.sendMessage(message);
    }
    
    public static Map<String, Object> getPlayerConstantData(String name) {
    	if(!playerData.containsKey(name))
    		playerData.put(name, new TreeMap<String, Object>());
    	return playerData.get(name);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	return ChatCommands.onCommand(sender, command, label, args);
    }
    
    @Override
    public void onDisable() {
    	LogUtils.fh.close();
    }
    
    void serverRestartUpdate() {
    	if(new Date().getDay() != startDay) {
    		serverRestartTime = 600;
    		startDay = new Date().getDay();
    	}
    	if(serverRestartTime == -10) return;
    	if(serverRestartTime == 600)
    		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Сервер будет перезапущен через 10 минут");
    	else if(serverRestartTime == 300)
    		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Сервер будет перезапущен через 5 минут");
    	else if(serverRestartTime == 60)
    		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Сервер будет перезапущен через 1 минуту");
    	else if(serverRestartTime == 30)
    		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Сервер будет перезапущен через 30 секунд");
    	else if(serverRestartTime == 10)
    		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Сервер будет перезапущен через 10 секунд");
    	else if(serverRestartTime == 5)
    		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Сервер будет перезапущен через 5 секунд");
    	else if(serverRestartTime <= 0) {
    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    		return;
    	}
    	serverRestartTime -= 1;
	}
    
    static {
    	chatHints.add("Вы можете отключить эти советы или отображение статистики, воспользовавшись командой /settings");
    	chatHints.add("Вы можете легко и быстро посмотреть список всех кастомных рецептов командой /recipes");
    	chatHints.add("Чтобы вернуться на точку возрождения используйте команду /home");
    	chatHints.add("/guide - узанть больше об изменениях на сервере");
    }
}
