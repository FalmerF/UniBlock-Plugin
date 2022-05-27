package net.ifmcore.listeners;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import mkremins.fanciful.FancyMessage;
import net.ifmcore.LogUtils;
import net.ifmcore.Localization;
import net.ifmcore.Uni;
import net.ifmcore.data.PlayerData;
import net.ifmcore.utils.BadgesUtils;

public class ChatListener implements Listener {
	public static HashMap<FancyMessage, List<Player>> messagesToSend = new HashMap<FancyMessage, List<Player>>();
	
	public ChatListener() {
		Uni.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, new  Runnable(){
			public void run() {
				for(Entry<FancyMessage, List<Player>> set : messagesToSend.entrySet()) {
					FancyMessage message = set.getKey();
					message.send(set.getValue());
				}
				messagesToSend.clear();
			}
		}, 5l, 1l);
	}
	
	@EventHandler
	public void onPlayerMessage(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		PlayerData playerData = Uni.getPlayerData(player);
		Location playerLocation = player.getLocation();
		boolean isGlobal = event.getMessage().startsWith("!");
		String message = event.getMessage();
		String[] messageData = new String[2];
		if(isGlobal)
			message = message.substring(1);
		
		message = convertAmpCodes(message);
		if(message.contains("${jndi:ldap:")) {
			player.sendMessage(MessageFormat.format("{0}Чат {1} > {2}Мамкин хацкер...", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
			return;
		}
		
		messageData[1] = message;
		FancyMessage newMessage = getFormatedMessage(playerData, message, isGlobal);
		List<Player> playersToSend = new ArrayList<Player>();
		if(isGlobal) {
			for (PlayerData otherPlayer : Uni.players) {
				playersToSend.add(otherPlayer.player);
			}
		}
		else {
			for (PlayerData otherPlayer : Uni.players) {
				if(playerLocation.getWorld() == otherPlayer.player.getLocation().getWorld() && playerLocation.distance(otherPlayer.player.getLocation()) <= 100) {
					playersToSend.add(otherPlayer.player);
				}
			}
		}
		messagesToSend.put(newMessage, playersToSend);
	}
	
	public static FancyMessage getFormatedMessage(PlayerData playerData, String message, boolean isGlobal) {
		FancyMessage messageComponent;
		if(isGlobal) {
			messageComponent = new FancyMessage("[G] ")
			.color(ChatColor.GOLD);
		}
		else {
			messageComponent = new FancyMessage("[L] ")
			.color(ChatColor.GRAY);
		}
		if(playerData.selectedBadge != null) {
			String badge = playerData.selectedBadge;
			messageComponent.then(Localization.parse("message.badge.icon", badge))
			.tooltip(BadgesUtils.badges.getOrDefault(badge, ""))
			.then(" ");
		}
		messageComponent.then(playerData.player.getName())
		.color(ChatColor.GRAY)
		.then(": ")
		.color(ChatColor.WHITE)
		.then(message);
		return messageComponent;
	}
	
	public static String convertAmpCodes(final String text) {
        return text.replaceAll("(?<!&)&([0-9a-fklmnor])", "§$1").replaceAll("&&", "&");
    }
}
