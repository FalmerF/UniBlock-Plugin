package net.ifmcore.requests;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.ifmcore.Uni;
import net.ifmcore.data.PlayerData;

public class TeleportHereRequest extends TeleportRequest {
	public TeleportHereRequest(Player player1, Player player2) {
		super(player1, player2, 0);
	}
	
	public void acceptRequest() {
		player2.teleport(player1.getLocation());
		player1.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}����� {1}{3}{2} ��� ��������������� � ���.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player2.getName()));
		player2.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}�� ������� ������ �� ������������ �� ������ {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player1.getName()));
		closeRequest();
	}
	public void rejectRequest() {
		player1.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}��� ������ ������������ ������ {1}{3}{2} ��� ��������.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player2.getName()));
		player2.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}�� ��������� ������ �� ������������ �� ������ {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player1.getName()));
		closeRequest();
	}
}
