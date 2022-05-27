package net.ifmcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.ifmcore.inventory.RecipeListInventory;

public class RecipesCommand implements CommandExecutor  {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
	        try {
	        	Player player = (Player) sender;
	        	RecipeListInventory.openRecipe(player, 0);
    		}
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
	    }
		return true;
	}
}
