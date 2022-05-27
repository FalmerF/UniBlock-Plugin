package net.ifmcore.commands;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.ifmcore.data.PlayerData;
import net.ifmcore.Uni;

public class MessageCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
	        try {
	        	if(args.length < 2) {
	        		sender.sendMessage(MessageFormat.format("{0}Help {1} > {2}Не верно указаны аргументы: /m <игрок> <сообщение>", ChatColor.GREEN, ChatColor.GOLD, ChatColor.RED));
	        		return true;
	        	}
	        	Player player = (Player) sender;
	        	PlayerData targetPlayerData = Uni.getPlayerData(args[0]);
	        	if(targetPlayerData != null) {
	        		String message = "";
	        		for(int i = 1; i < args.length; i++)
	        			message += args[i] + " ";
	        		player.sendMessage(MessageFormat.format("{0}[Я {1}->{0} {3}] {2}{4}", ChatColor.YELLOW, ChatColor.GRAY, ChatColor.WHITE, targetPlayerData.player.getName(), message));
	        		targetPlayerData.player.sendMessage(MessageFormat.format("{0}[{3} {1}->{0} Я] {2}{4}", ChatColor.YELLOW, ChatColor.GRAY, ChatColor.WHITE, player.getName(), message));
	        		targetPlayerData.whoLastMessage = player.getName();
        		}
	        	else {
	        		sender.sendMessage(MessageFormat.format("{0}Msg {1} > {2}Игрок должен быть {0}онлайн{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
	        	}
	        }
	        catch(Exception e) {
	        }
	    }
		return true;
	}
}
