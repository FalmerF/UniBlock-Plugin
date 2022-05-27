package net.ifmcore.commands;

import java.text.MessageFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.ifmcore.Uni;
import net.ifmcore.data.ConfigsManager;

public class ReloadcfgCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ConfigsManager.instance.loadConfigs();
		Bukkit.broadcastMessage(MessageFormat.format("{0}Config {1} > {2}{1}{3}{2} reloaded configs.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GREEN, sender.getName()));
		return true;
	}
}
