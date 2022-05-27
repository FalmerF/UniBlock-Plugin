package net.ifmcore.data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerIPManager {
	public static void addPlayerIp(String playerName, String ip) {
		String[] data = DataBaseManager.getData("ip", new String[] {"ip"}, "name=\""+playerName+"\"");
		String newIPData = "";
		if(data != null && data.length > 0 && data[0] != null && !data[0].equals("")) {
			for(String p : data[0].split(";")) {
				if(p.equalsIgnoreCase(ip))
					return;
			}
			newIPData = data[0] + ip + ";";
			DataBaseManager.setData("ip", "ip=\""+newIPData+"\"", "name=\""+playerName+"\"");
		}
		else {
			newIPData = ip + ";";
			DataBaseManager.insertData("ip", "\""+playerName+"\", \""+newIPData+"\", \"\", \"\"");
		}
	}
	
	public static void setPlayerOnlineAndPosition(Player player) {
		try {
			Location loc = player.getLocation();
			StringBuilder posBuilder = new StringBuilder().append(loc.getWorld().getName()).append(", ")
					.append(loc.getBlockX()).append(" ")
					.append(loc.getBlockY()).append(" ")
					.append(loc.getBlockZ());
			DataBaseManager.setData("ip", "lastonline=\""+LocalDateTime.now().toString()+"\", lastpos=\""+posBuilder.toString()+"\"", "name=\""+player.getName()+"\"");
		}
		catch(Exception e) {
			
		}
	}
	
	public static String getPlayerOnlineAndPosition(String playerName) {
		String info = "";
		if(Bukkit.getPlayer(playerName) == null) {
			String[] data = DataBaseManager.getData("ip", new String[] {"lastonline", "lastpos"}, "name=\""+playerName+"\"");
			if(data != null && data.length > 0 && data[0] != null && !data[0].equals("")) {
				LocalDateTime date = LocalDateTime.parse(data[0]);
				float difference = ChronoUnit.SECONDS.between(date, LocalDateTime.now());
				info = ChatColor.YELLOW + "\n    Был онлайн " + ChatColor.GOLD + PlayerData.ticksToTime((int)difference*20) + ChatColor.YELLOW  + " назад "+ ChatColor.GOLD +"("+data[1]+")";
			}
		}
		else {
			info = ChatColor.GREEN+"\n    (Онлайн)";
		}
		return info;
	}
	
	public static List<String> getLinkedAccounts(String playerName) {
		List<String> accounts = new ArrayList<String>();
		accounts.add(playerName);
		
		String[] data = DataBaseManager.getData("ip", new String[] {"ip"}, "name=\""+playerName+"\"");
		if(data != null && data.length > 0 && data[0] != null && !data[0].equals("")) {
			List<String> unCheckedIP = new ArrayList<String>();
			List<String> checkedIp = new ArrayList<String>();
			for(String p : data[0].split(";")) {
				if(checkedIp.indexOf(p) == -1 && unCheckedIP.indexOf(p) == -1)
					unCheckedIP.add(p);
			}
			List<List<String>> allIPData = DataBaseManager.getAllTableData("ip");
			while(unCheckedIP.size() > 0) {
				List<String> tempIp = new ArrayList<String>();
				for(List<String> ipData : allIPData) {
					String[] i = ipData.get(1).split(";");
					for(String p : i) {
						if(!p.equals("") && unCheckedIP.indexOf(p) != -1) {
							for(String p2 : i) {
								if(!p2.equals("") && checkedIp.indexOf(p2) == -1 && unCheckedIP.indexOf(p2) == -1 && tempIp.indexOf(p2) == -1) {
									tempIp.add(p2);
								}
							}
							if(accounts.indexOf(ipData.get(0)) == -1)
								accounts.add(ipData.get(0));
							break;
						}
					}
				}
				checkedIp.addAll(unCheckedIP);
				unCheckedIP.clear();
				unCheckedIP.addAll(tempIp);
				tempIp.clear();
			}
		}
		return accounts;
	}
}
