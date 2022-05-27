package net.ifmcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.ifmcore.Uni;
import net.ifmcore.data.PlayerData;
import net.ifmcore.utils.BadgesUtils;

public class BadgeTabCompleter implements TabCompleter {
	
	public BadgeTabCompleter() {
		Uni.instance.getCommand("badge").setTabCompleter(this);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> list = new ArrayList<>();
        if(sender instanceof Player) {
        	Player player = (Player) sender;
        	PlayerData playerData = Uni.getPlayerData(player);
        	if(args.length == 1) {
        		list.add("select");
        		if(player.isOp()) {
		            list.add("set");
		            list.add("give");
        		}
        	}
        	else if(args.length == 2) {
        		if(args[0].equals("select")) {
        			list.addAll(playerData.badges);
        			list.add("none");
        		}
        		else if(args[0].equals("set") && player.isOp()) {
        			for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            			list.add(onlinePlayer.getName());
            		}
        		}
        		else if(args[0].equals("give") && player.isOp()) {
        			for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            			list.add(onlinePlayer.getName());
            		}
        		}
        	}
        	else if(args.length == 3 && player.isOp() && ((args[0].equals("set") || args[0].equals("give")))) {
        		list.addAll(BadgesUtils.badges.keySet());
        	}
        	else if(args.length == 4 && player.isOp() && (args[0].equals("set"))) {
        		list.add("false");
        		list.add("true");
        	}
            return list;
        }
        return null;
	}
}
