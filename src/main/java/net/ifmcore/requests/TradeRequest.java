package net.ifmcore.requests;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.ifmcore.inventory.TradeInventory;

public class TradeRequest extends Request {
	public TradeRequest(Player player1, Player player2) {
		super(player1, player2, 120);
	}
	
	public void acceptRequest() {
		new TradeInventory(player1, player2);
		player1.sendMessage(MessageFormat.format("{0}Trade {1}> {2}����� {1}{3}{2} ������ ��� ������ �� ��������.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player2.getName()));
		player2.sendMessage(MessageFormat.format("{0}Trade {1}> {2}�� ������� ������ �� �������� �� ������ {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player1.getName()));
		closeRequest();
	}
	public void rejectRequest() {
		player1.sendMessage(MessageFormat.format("{0}Trade {1}> {2}��� ������ �� �������� � ������� {1}{3}{2} ��� ��������.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player2.getName()));
		player2.sendMessage(MessageFormat.format("{0}Trade {1}> {2}�� ��������� ������ �� �������� �� ������ {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player1.getName()));
		closeRequest();
	}
}
