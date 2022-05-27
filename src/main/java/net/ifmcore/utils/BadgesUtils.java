package net.ifmcore.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import mkremins.fanciful.FancyMessage;
import net.ifmcore.Localization;
import net.ifmcore.data.PlayerData;

public class BadgesUtils {
	public static Map<String, String> badges = new HashMap<String, String>();
	
	public static FancyMessage getBadgeChatComponent(String badge) {
		FancyMessage message = new FancyMessage(ChatColor.GOLD+"["+ChatColor.WHITE+badge+ChatColor.GOLD+"]")
        .tooltip(badges.getOrDefault(badge, ""));
		return message;
	}
	
	public static void checkAchievementsComplete(PlayerData playerData) {
		Location loc = playerData.player.getLocation();
		if(playerData.trevelled >= 999) {
			playerData.giveBadge("\uE110");
		}
		if(loc.getWorld().getName().equals("world") && (loc.getBlockX() >= 100000 || loc.getBlockX() <= -100000 || loc.getBlockZ() >= 100000 || loc.getBlockZ() <= -100000)) {
			playerData.giveBadge("\uE312");
		}
	}
	
	static {
    	badges.put("\uE347", "Поддержал проект");
    	badges.put("\uE110", "Пройти более 999км");
    	badges.put("\uE312", "Добраться до 100к координат в обычном мире");
    	badges.put("\uE321", "С элитрами успасть с высокого места и остаться живым");
    	badges.put("\uE316", "Довести Администратора");
    	badges.put("\uE186", "Один из первых игроков");
    	badges.put("\uE258", "Наиграно 200 часов");
    	badges.put("\uE265", "Наиграно 500 часов");
    	badges.put("\uE044", "Совершил 2000 сделок с жителями");
    }
}
