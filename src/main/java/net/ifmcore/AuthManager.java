package net.ifmcore;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.ifmcore.data.DataBaseManager;
import net.ifmcore.data.PlayerData;

public class AuthManager {	
	public static String[] getLastAuthData(PlayerData playerData) {
		return getLastAuthData(playerData.player);
	}
	
	public static String[] getLastAuthData(Player player) {
		return getLastAuthData(player.getName());
	}
	
	public static String[] getLastAuthData(String playerName) {
		String[] data = DataBaseManager.getData("auth", new String[] {"name", "password", "ip", "lastAuthTime"}, "name=\""+playerName+"\"");
		if(data != null && data.length > 0 && data[0] != null && !data[0].equals("")) {
			return data;
		}
		return new String[4];
	}
	
	public static boolean isRegistered(Player player) {
		return DataBaseManager.checkInDataBase("auth", "name", "name=\""+player.getName()+"\"");
	}
	
	public static boolean isAuthorized(Player player) {
		String[] data = getLastAuthData(player.getName());
		float difference = 1000000;
		if(!data[3].equals("")) {
			LocalDateTime date = LocalDateTime.parse(data[3]);
			difference = ChronoUnit.SECONDS.between(date, LocalDateTime.now());
		}
		if(player.getAddress().getHostName().equals(data[2]) && difference <= 86400) {
			return true;
		}
		return false;
	}
	
	public static String getLastIp(Player player) {
		String[] data = getLastAuthData(player.getName());
		return data[2];
	}	
	
	public static void authorize(Player player) {
		PlayerData playerData = Uni.getPlayerData(player);
		if(playerData != null)
			playerData.isAuthorized = true;
		DataBaseManager.setData("auth", "lastAuthTime=\""+LocalDateTime.now().toString()+"\", ip=\""+player.getAddress().getHostName()+"\"", "name=\""+player.getName()+"\"");
	}
	
	public static void register(Player player, String password) {
		PlayerData playerData = Uni.getPlayerData(player);
		if(playerData != null) {
			playerData.isAuthorized = true;
			playerData.isRegistered = true;
		}
		DataBaseManager.insertData("auth", "\""+player.getName()+"\", \""+encodePassword(password)+"\", \""+player.getAddress().getHostName()+"\", \""+LocalDateTime.now().toString()+"\"");
	}
	
	public static boolean isValidPassword(String playerName, String password) {
		String[] data = getLastAuthData(playerName);
		return password.equals(decodePassword(data[1]));
	}
	
	public static String encodePassword(String password) {
		StringBuilder encodedPassword = new StringBuilder();
		char c1 = getRandomValidChar();
		char c2 = getRandomValidChar();
		int encodeType = ((int)c1+(int)c2)%2 == 0 ? 1 : 2;
		
		encodedPassword.append(c1);
		
		for(char c : password.toCharArray()) {
			if(encodeType == 1) {
				int i1 = (int) Uni.getRandomNumber(0, (int)c-35);
				int i2 = (int)c-35-i1;
				char c3 = (char)(i1+35);
				char c4 = (char)(i2+35);
				encodedPassword.append(c4).append(c3);
			}
			else {
				int i1 = (int) Uni.getRandomNumber((int)c-35, 90);
				int i2 = i1-((int)c-35);
				char c3 = (char)(i1+35);
				char c4 = (char)(i2+35);
				encodedPassword.append(c4).append(c3);
			}
		}
		encodedPassword.append(c2);
		return encodedPassword.toString();
	}
	
	public static String decodePassword(String password) {
		StringBuilder decodedPassword = new StringBuilder();
		char[] chars = password.toCharArray();
		char c1 = chars[0];
		char c2 = chars[chars.length-1];
		int decodeType = ((int)c1+(int)c2)%2 == 0 ? 1 : 2;
		
		for(int i = 1; i < chars.length-1; i+=2) {
			char c3 = chars[i];
			char c4 = chars[i+1];
			if(decodeType == 1) {
				char c5 = (char)(((int)c3-35)+((int)c4-35)+35);
				decodedPassword.append(c5);
			}
			else {
				char c5 = (char)(((int)c4-35)-((int)c3-35)+35);
				decodedPassword.append(c5);
			}
		}
		
		return decodedPassword.toString();
	}
	
	public static char getRandomValidChar() {
		char c = (char) Uni.getRandomNumber(35, 125);
		return c;
	}
	
	public static boolean isValidatePasswordChars(String password) {
		for(char c : password.toCharArray())
			if((int)c < 35 || (int)c > 125)
				return false;
		return true;
	}
}
