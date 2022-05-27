package net.ifmcore;

import java.text.MessageFormat;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Localization {
	public static Map<String, String> messages = new TreeMap<String, String>();
	
	public static String getPotionEffectName(PotionEffect potionEffect) {
		PotionEffectType potionType = potionEffect.getType();
		if(potionType.equals(PotionEffectType.ABSORPTION))
			return "����������";
		else if(potionType.equals(PotionEffectType.BAD_OMEN))
			return "������ ��������";
		else if(potionType.equals(PotionEffectType.BLINDNESS))
			return "�������";
		else if(potionType.equals(PotionEffectType.CONDUIT_POWER))
			return "����";
		else if(potionType.equals(PotionEffectType.CONFUSION))
			return "�������";
		else if(potionType.equals(PotionEffectType.DAMAGE_RESISTANCE))
			return "�������������";
		else if(potionType.equals(PotionEffectType.DOLPHINS_GRACE))
			return "������ ��������";
		else if(potionType.equals(PotionEffectType.FAST_DIGGING))
			return "������";
		else if(potionType.equals(PotionEffectType.FIRE_RESISTANCE))
			return "�������������";
		else if(potionType.equals(PotionEffectType.GLOWING))
			return "��������";
		else if(potionType.equals(PotionEffectType.HARM))
			return "������������ ����";
		else if(potionType.equals(PotionEffectType.HEAL))
			return "���������";
		else if(potionType.equals(PotionEffectType.HEALTH_BOOST))
			return "������ ��������";
		else if(potionType.equals(PotionEffectType.HERO_OF_THE_VILLAGE))
			return "����� �������";
		else if(potionType.equals(PotionEffectType.HUNGER))
			return "�����";
		else if(potionType.equals(PotionEffectType.INCREASE_DAMAGE))
			return "����";
		else if(potionType.equals(PotionEffectType.INVISIBILITY))
			return "�����������";
		else if(potionType.equals(PotionEffectType.JUMP))
			return "����������";
		else if(potionType.equals(PotionEffectType.LEVITATION))
			return "���������";
		else if(potionType.equals(PotionEffectType.LUCK))
			return "�������";
		else if(potionType.equals(PotionEffectType.NIGHT_VISION))
			return "������ ������";
		else if(potionType.equals(PotionEffectType.POISON))
			return "����������";
		else if(potionType.equals(PotionEffectType.REGENERATION))
			return "�����������";
		else if(potionType.equals(PotionEffectType.SATURATION))
			return "������������";
		else if(potionType.equals(PotionEffectType.SLOW))
			return "��������������";
		else if(potionType.equals(PotionEffectType.SLOW_DIGGING))
			return "���������";
		else if(potionType.equals(PotionEffectType.SLOW_FALLING))
			return "������� �������";
		else if(potionType.equals(PotionEffectType.SPEED))
			return "��������";
		else if(potionType.equals(PotionEffectType.UNLUCK))
			return "���������";
		else if(potionType.equals(PotionEffectType.WATER_BREATHING))
			return "������ �������";
		else if(potionType.equals(PotionEffectType.WEAKNESS))
			return "��������";
		else if(potionType.equals(PotionEffectType.WITHER))
			return "���������";
		return "";
	}
	
	public static String fromTicksToFormatedTime(int ticks) {
		int seconds = ticks / 20;
		return (int)Math.floor((float)seconds / 60.00F) + ":" + seconds%60;
	}
	
	public static String parse(String messageKey, Object... args) {
		return MessageFormat.format(messages.getOrDefault(messageKey, ""), args);
	}
	
	public static String parse(String messageKey) {
		return messages.getOrDefault(messageKey, "");
	}
	
	static {
		messages.put("message.player.must.online", "�a{0} �6> �e����� ������ ���� �a������e.");
		messages.put("message.console.command.for.player", "Sorry, but this command is for the player");
		messages.put("message.command.for.console", "Sorry, but this command is for the console");
		messages.put("command.bad.args", "�aHelp �6> �c�� ����� ������� ���������: {0}");
		messages.put("command.error", "�a{0} �6> �c��������� �� ������������ ������.");
		messages.put("message.command.changepassword.uncorrect.password", "�aAuth �6> �c�� ������ ������!");
		messages.put("message.command.changepassword.uncorrect.newpassword1", "�aAuth �6> �c������ ������ ���������!");
		messages.put("message.command.changepassword.uncorrect.newpassword2", "�aAuth �6> �c� ������ ���������� �� ���������� �������!");
		messages.put("message.command.changepassword.uncorrect.newpassword3", "�aAuth �6> �c������ ������ ���� ������� �� ����� {0} ��������!");
		messages.put("message.command.changepassword.success", "�aAuth �6> �a������ �������� �������!");
		messages.put("message.command.clearlag.entity", "�aClearLag �6> �e��� Entity �������.");
		messages.put("message.command.clearlag.items", "�aClearLag �6> �e��� �������� �������.");
		messages.put("message.command.clearlag.mobs", "�aClearLag �6> �e��� ���� �������.");
		messages.put("message.command.clearlag.stats", "�aClearLag �6> �eChunks �6{0}�e, Entity �6{1}�e:{2}");
		messages.put("message.command.gamemode", "�aGameMode �6> �7����� ���� ���������� �� �6{0}�7.");
		messages.put("message.command.gamemode.stop", "�c������� ����� ������ ������!");
		messages.put("message.command.gamemodeplus", "�aGameMode �6> �7����� ���� ��� ������ �6{0}�7 ���������� �� �6{1}�7.");
		messages.put("message.command.getitemnbt", "�aItem �6> �eNBT: �7{0}");
		messages.put("message.command.getitemnbt.needitem", "�aItem �6> �c�� ������ ������� ������� � ����.");
		messages.put("message.command.login.already.authorized", "�aAuth �6> �a�� ��� ������������!");
		messages.put("message.command.login.authorized", "�aAuth �6> �a�� ������� ������������!");
		messages.put("message.command.login.wrongpassword", "�aAuth �6> �c�� ������ ������!");
		messages.put("message.command.register.already.authorized", "�aAuth �6> �a�� ��� ������������!");
		messages.put("message.command.register.already.registered", "�aAuth �6> �e�� ��� ����������������, �����������: �6/l <������>");
		messages.put("message.command.register.wrong1", "�aAuth �6> �c������ ������ ���������!");
		messages.put("message.command.register.wrong2", "�aAuth �6> �c� ������ ���������� �� ���������� �������!");
		messages.put("message.command.register.wrong3", "�aAuth �6> �c������ ������ ���� ������� �� ����� 8 ��������!");
		messages.put("message.command.register.success", "�aAuth �6> �a�� ������� ������������!");
		messages.put("message.command.seen.result", "�aSeen �6> �e������ ��������� ������ �6{0}�e: �f");
		messages.put("message.command.teleport.toplayer", "�aTeleport �6> �7�� ��������� ������ �� ������������ ������ �6{0}�7.");
		messages.put("message.command.teleport.totarget", "�aTeleport �6> �6{0}�7 �������� ��� ������ �� ������������.");
		messages.put("message.command.getstats.result", "�a����� �6{0}�a ������� �6{1}�a ����� �6{2}�a.");
		messages.put("message.command.restart.result", "�d������ ����� ����������� ����� {0} ������");
		messages.put("message.command.badge.notfound", "�e��������� ���� ������ �� ����������");
		messages.put("message.command.badge.notaccess", "�e�� ��� �� �������� ���� ������");
		messages.put("message.command.badge.select", "�a�� ������� ���������� ������ ");
		messages.put("message.command.badge.hide", "�a�� ������ ���� ������");
		messages.put("message.command.badge.set.true", "�7������ �6{0} �7���������� �� �atrue �7������ ");
		messages.put("message.command.badge.set.false", "�7������ �6{0} �7���������� �� �cfalse �7������ ");
		messages.put("message.command.badge.give.alreadyhas", "�7����� �6{0} �7��� ����� ���� ������");
		messages.put("message.command.toggleevent.true", "�7����� �a�������� �� ������ �7����������!");
		messages.put("message.command.toggleevent.false", "�7����� �a�������� �� ������ �7��������!");
		messages.put("message.command.eventreload", "�7����� ������������!");
		messages.put("message.command.tpaccept.norequests", "�e� ��� ��� �������� �������� �� ������������");
		messages.put("message.command.home.result", "�a��� ����� ���!");
		messages.put("message.command.call.nodiamonds", "�c��� ���������� �6{0} ���. �c��� ������������ � ����� ������!");
		messages.put("message.command.call.nodiamonds.target", "�c� �6{0} �c�� ���������� ������� ��� ������������!");
		messages.put("message.command.tp.result", "�7�� ��������������� � ������ �6{0}");
		messages.put("message.command.lighting.result", "�7����� �6{0} �7������� ���� �������");
		messages.put("message.command.setplaytime.result", "�7������ �6{0} �7����������� ���������� ����� �� �6{1} ���.");
		messages.put("message.command.loadchunk.notfound", "�c����� �6{0} �c�� ������");
		
		messages.put("command.changepassword.usage", "/changepassword <������� ������> <����� ������> <������ ������>");
		messages.put("command.clearlag.usage", "/clearlag <all/items/mobs/stats>");
		messages.put("command.gamemode.usage", "/gm <adventure/creative/survival/spectator>");
		messages.put("command.login.usage", "/login <������>");
		messages.put("command.register.usage", "/register <������> <������ ������>");
		messages.put("command.seen.usage", "/seen <�����>");
		messages.put("command.call.usage", "/call <�����>");
		messages.put("command.getstats.usage", "/getstats <�����> <����>");
		messages.put("command.restart.usage", "/restart <�����>");
		messages.put("command.badge.usage", "/badge select <������>");
		messages.put("command.badge.usage.set", "/badge set <�����> <������> <true/false>");
		messages.put("command.badge.usage.give", "/badge set <�����> <������>");
		messages.put("command.badge.usage.admin", "/badge <select/set/give>");
		messages.put("command.giveitem.usage", "/giveitem <player> <item> <amount>");
		messages.put("command.lighting.usage", "/lighting <player>");
		messages.put("command.setplaytime.usage", "/setplaytime <player> <time>");
		messages.put("command.loadchunk.usage", "/loadchunk <backup_name>");
		
		messages.put("gamemode.survival", "���������");
		messages.put("gamemode.creative", "����������");
		messages.put("gamemode.adventure", "�����������");
		messages.put("gamemode.spectator", "�����������");
		
		messages.put("title.welcome.1", "\uE344�a����� ����������!�f\uE344");
		
		messages.put("message.need.authorized", "�aAuth �6> �e�������������: �6/l <������>");
		messages.put("message.need.registered", "�aAuth �6> �e����������������� �� �������: �6/reg <������> <������ ������>");
		
		messages.put("message.badge.give", "�a����� �6{0} �a������� ����� ������ ");
		messages.put("message.badge.icon", "�6[�f{0}�6]");
		
		messages.put("message.trade.nodiamonds", "�c�� ���������� ������� ��� ������!");
		
		messages.put("message.player.giveitem", "�a�� �������� �6{0} x{1}");
		messages.put("message.player.getkeepinventory", "�a� ��� ��������� ���������� ���������, �������� �6{0}");
		messages.put("message.player.losekeepinventory", "�e� ��� ����������� ����� ������ ���������� ���������.");
		
		messages.put("message.backuploader.chunk.start", "�e������� �������� ����� �6({0}, {1}, {2}) �e�� ������ �6{3}");
	}
}
