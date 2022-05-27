package net.ifmcore.commands;

import java.text.MessageFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkremins.fanciful.FancyMessage;
import net.ifmcore.Uni;
import net.ifmcore.data.PlayerData;
import net.ifmcore.requests.Request;
import net.ifmcore.requests.RequestManager;
import net.ifmcore.requests.TradeRequest;

public class TradeCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
	        try {
	        	if(args.length < 1) {
	        		sender.sendMessage(MessageFormat.format("{0}Help {1}> {2}Не верно указаны аргументы: /trade <игрок>", ChatColor.GREEN, ChatColor.GOLD, ChatColor.RED));
	        		return true;
	        	}
	        	Player player1 = (Player) sender;
	        	if(args[0].equals("accept")) {
	        		Request tradeRequest = RequestManager.getRequest(player1, TradeRequest.class);
		        	if(tradeRequest != null) {
		        		tradeRequest.acceptRequest();
		        	}
		        	else {
		        		sender.sendMessage(MessageFormat.format("{0}Trade {1}> {2}У вас нет активных запросов на торговлю.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
		        	}
	        	}
	        	else if(args[0].equals("deny")) {
	        		Request tradeRequest = RequestManager.getRequest(player1, TradeRequest.class);
		        	if(tradeRequest != null) {
		        		tradeRequest.rejectRequest();
		        	}
		        	else {
		        		sender.sendMessage(MessageFormat.format("{0}Trade {1}> {2}У вас нет активных запросов на торговлю.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
		        	}
	        	}
	        	else {
		        	Player player2 = Bukkit.getPlayer(args[0]);
		        	PlayerData player2Data = Uni.getPlayerData(args[0]);
		        	if(player2Data == null) {
		        		sender.sendMessage(MessageFormat.format("{0}Trade {1}> {2}Игрок должен быть {0}онлайн{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
		        		return true;
		        	}
		        	else if(player1 == player2) {
		        		sender.sendMessage(MessageFormat.format("{0}Trade {1}> {2}Вы не можете отправить запрос себе.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.YELLOW));
		        		return true;
		        	}
		        	TradeRequest tradeRequest = new TradeRequest(player1, player2);
	        		RequestManager.addRequest(tradeRequest);
	        		
	        		player1.sendMessage(MessageFormat.format("{0}Trade {1}> {2}Вы отправили запрос на торговлю игроку {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player2.getName()));
	        		
	        		new FancyMessage(MessageFormat.format("{0}Trade {1}> {1}{3}{2} отправил вам запрос на торговлю.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player1.getName()))
	                .then("\n    Используйте команду ")
	                .color(ChatColor.GRAY)
	                .then("/trade accept")
	                .color(ChatColor.GOLD)
	                .command("/trade accept")
	                .tooltip(ChatColor.GOLD+"/trade accept")
	                .then(" чтобы принять или ")
	                .color(ChatColor.GRAY)
	                .then("/trade deny")
	                .color(ChatColor.GOLD)
	                .command("/trade deny")
	                .tooltip(ChatColor.GOLD+"/trade deny")
	                .then(" чтобы отклонить запрос.")
	                .color(ChatColor.GRAY)
	                .send(player2);
	        	}
	        }
	        catch(Exception e) {
	        }
	    }
		return true;
	}
}
