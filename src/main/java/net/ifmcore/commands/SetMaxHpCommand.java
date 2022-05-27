package net.ifmcore.commands;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.ifmcore.data.PlayerData;
import net.ifmcore.Uni;

public class SetMaxHpCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			try {
				if(((Player)sender).getGameMode() != GameMode.CREATIVE) return true;
				if(args.length < 1) {
	        		sender.sendMessage(MessageFormat.format("{0}Help {1} > {2}Не верно указаны аргументы: /setmaxhp <hp> <игрок>", ChatColor.GREEN, ChatColor.GOLD, ChatColor.RED));
	        		return true;
	        	}
				PlayerData playerData = Uni.getPlayerData((Player) sender);
	        	if(args.length > 1) {
	        		PlayerData playerDataTemp = Uni.getPlayerData(args[1]);
	        		if(playerDataTemp != null) {
	        			playerData = playerDataTemp;
		        	}
		        	else {
		        		sender.sendMessage(MessageFormat.format("{0}SetMaxHp {1} > {2}Игрок должен быть {0}онлайн{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
		        		return true;
		        	}
	        	}
	        	
	        	playerData.player.setMaxHealth(Float.parseFloat(args[0]));
	        	sender.sendMessage(MessageFormat.format("{0}SetMaxHp {1} > {2}Максимальное здоровье для игрока {1}{3}{2} установлено на {1}{4}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, playerData.player.getName(), String.valueOf(playerData.player.getMaxHealth())));
	        }
	        catch(Exception e) {
	        	sender.sendMessage(MessageFormat.format("{0}Help {1} > {2}"+e.getMessage(), ChatColor.GREEN, ChatColor.GOLD, ChatColor.RED));
	        }
		}
		return true;
	}
}
