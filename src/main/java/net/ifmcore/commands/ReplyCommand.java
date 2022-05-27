package net.ifmcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.ifmcore.data.PlayerData;
import net.ifmcore.Uni;

public class ReplyCommand implements CommandExecutor  {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
	        try {
	        	PlayerData playerData = Uni.getPlayerData((Player) sender);
	        	List<String> newArgs = new ArrayList<String>();
	        	newArgs.add(playerData.whoLastMessage);
	        	for(String arg : args)
	        		newArgs.add(arg);
	        	Uni.instance.getCommand("m").execute(sender, label, newArgs.toArray(new String[] {}));
	        }
	        catch(Exception e) {
	        }
	    }
		return true;
	}
}
