package net.ifmcore.commands;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.ifmcore.Uni;

public class DeathpointCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
	        try {
	        	Player player = (Player) sender;
	        	Location deathLoc = (Location) Uni.getPlayerConstantData(player.getName()).getOrDefault("death_loc", null);
	        	if(deathLoc != null) {
	        		player.teleport(deathLoc);
	        		Uni.getPlayerConstantData(player.getName()).put("death_loc", null);
	    			sender.sendMessage(MessageFormat.format("{0}Death {1}> {2}Вы телепортированы на точку смерти {6}({3} {4} {5}){2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW, String.valueOf(deathLoc.getBlockX()), String.valueOf(deathLoc.getBlockY()), String.valueOf(deathLoc.getBlockZ()), ChatColor.GRAY));
	    		}
	        	else {
	        		sender.sendMessage(MessageFormat.format("{0}Death {1}> {2}У вас нет сохраненной точки смерти.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
	        	}
	        }
	        catch(Exception e) {
	        }
	    }
		return true;
	}
}
