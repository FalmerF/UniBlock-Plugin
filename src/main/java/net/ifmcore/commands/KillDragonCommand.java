package net.ifmcore.commands;

import java.text.MessageFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.ifmcore.listeners.EnderDragonListener;

public class KillDragonCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
	        try {
	        	if(((Player)sender).getGameMode() != GameMode.CREATIVE) return true;
	        	World world = Bukkit.getWorld("world_the_end");
	    		for(Entity entity : world.getEntities())
	    			if(entity.getType() == EntityType.ENDER_DRAGON)
	    				entity.remove();
	    		EnderDragonListener.setEnderDragon(null);
	        	sender.sendMessage(MessageFormat.format("{0}Dragon {1}> {2}Дракон убит.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
	        }
	        catch(Exception e) {
	        }
	    }
		return true;
	}
}
