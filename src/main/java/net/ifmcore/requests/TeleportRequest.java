package net.ifmcore.requests;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.ifmcore.ItemUtils;
import net.ifmcore.Localization;

public class TeleportRequest extends Request {
	int cost;
	public TeleportRequest(Player player1, Player player2, int cost) {
		super(player1, player2, 120);
		this.cost = cost;
	}
	
	public void acceptRequest() {
		if(ItemUtils.itemsCount(player1.getInventory(), new ItemStack(Material.DIAMOND)) < cost) {
			player1.sendMessage(Localization.parse("message.command.call.nodiamonds", cost));
			player2.sendMessage(Localization.parse("message.command.call.nodiamonds.target", player1.getName()));
			return;
		}
		ItemUtils.removeMultiplyItem(player1.getInventory(), new ItemStack(Material.DIAMOND, cost));
		player1.teleport(player2.getLocation());
		player1.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}¬ы были телепортированы к игроку {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player2.getName()));
		player2.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}¬ы прин€ли запрос на телепортацию от игрока {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player1.getName()));
		closeRequest();
	}
	public void rejectRequest() {
		player1.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}¬аш запрос на телепортацию к игроку {1}{3}{2} был отклонен.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player2.getName()));
		player2.sendMessage(MessageFormat.format("{0}Teleport {1}> {2}¬ы отклонили запрос на телепортацию от игрока {1}{3}{2}.", ChatColor.GREEN, ChatColor.GOLD, ChatColor.GRAY, player1.getName()));
		closeRequest();
	}
}
