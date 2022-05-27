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
		player1.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}»грок {1}{3}{2} был телепортированы к вам.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player2.getName()));
		player2.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}¬ы прин€ли запрос на телепортацию от игрока {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player1.getName()));
		closeRequest();
	}
	public void rejectRequest() {
		player1.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}¬аш запрос телепортации игрока {1}{3}{2} был отклонен.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player2.getName()));
		player2.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}¬ы отклонили запрос на телепортацию от игрока {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player1.getName()));
		closeRequest();
	}
}
