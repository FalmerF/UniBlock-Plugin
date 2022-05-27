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
			return "Поглощение";
		else if(potionType.equals(PotionEffectType.BAD_OMEN))
			return "Дурное знамение";
		else if(potionType.equals(PotionEffectType.BLINDNESS))
			return "Слепота";
		else if(potionType.equals(PotionEffectType.CONDUIT_POWER))
			return "Сила";
		else if(potionType.equals(PotionEffectType.CONFUSION))
			return "Тошнота";
		else if(potionType.equals(PotionEffectType.DAMAGE_RESISTANCE))
			return "Сопротивление";
		else if(potionType.equals(PotionEffectType.DOLPHINS_GRACE))
			return "Грация дельфина";
		else if(potionType.equals(PotionEffectType.FAST_DIGGING))
			return "Спешка";
		else if(potionType.equals(PotionEffectType.FIRE_RESISTANCE))
			return "Огнестойкость";
		else if(potionType.equals(PotionEffectType.GLOWING))
			return "Свечение";
		else if(potionType.equals(PotionEffectType.HARM))
			return "Моментальный урон";
		else if(potionType.equals(PotionEffectType.HEAL))
			return "Исцеление";
		else if(potionType.equals(PotionEffectType.HEALTH_BOOST))
			return "Прилив здоровья";
		else if(potionType.equals(PotionEffectType.HERO_OF_THE_VILLAGE))
			return "Герой деревни";
		else if(potionType.equals(PotionEffectType.HUNGER))
			return "Голод";
		else if(potionType.equals(PotionEffectType.INCREASE_DAMAGE))
			return "Сила";
		else if(potionType.equals(PotionEffectType.INVISIBILITY))
			return "Невидимость";
		else if(potionType.equals(PotionEffectType.JUMP))
			return "Прыгучесть";
		else if(potionType.equals(PotionEffectType.LEVITATION))
			return "Левитация";
		else if(potionType.equals(PotionEffectType.LUCK))
			return "Везение";
		else if(potionType.equals(PotionEffectType.NIGHT_VISION))
			return "Ночное зрение";
		else if(potionType.equals(PotionEffectType.POISON))
			return "Отравление";
		else if(potionType.equals(PotionEffectType.REGENERATION))
			return "Регенерация";
		else if(potionType.equals(PotionEffectType.SATURATION))
			return "Насыщенность";
		else if(potionType.equals(PotionEffectType.SLOW))
			return "Медлительность";
		else if(potionType.equals(PotionEffectType.SLOW_DIGGING))
			return "Утомление";
		else if(potionType.equals(PotionEffectType.SLOW_FALLING))
			return "Плавное падение";
		else if(potionType.equals(PotionEffectType.SPEED))
			return "Скорость";
		else if(potionType.equals(PotionEffectType.UNLUCK))
			return "Невезение";
		else if(potionType.equals(PotionEffectType.WATER_BREATHING))
			return "Водное дыхание";
		else if(potionType.equals(PotionEffectType.WEAKNESS))
			return "Слабость";
		else if(potionType.equals(PotionEffectType.WITHER))
			return "Иссушение";
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
		messages.put("message.player.must.online", "§a{0} §6> §eИгрок должен быть §aонлайн§e.");
		messages.put("message.console.command.for.player", "Sorry, but this command is for the player");
		messages.put("message.command.for.console", "Sorry, but this command is for the console");
		messages.put("command.bad.args", "§aHelp §6> §cНе верно указаны аргументы: {0}");
		messages.put("command.error", "§a{0} §6> §cПроизошла не предвиденная ошибка.");
		messages.put("message.command.changepassword.uncorrect.password", "§aAuth §6> §cНе верный пароль!");
		messages.put("message.command.changepassword.uncorrect.newpassword1", "§aAuth §6> §cПароли должны совпадать!");
		messages.put("message.command.changepassword.uncorrect.newpassword2", "§aAuth §6> §cВ пароле содержатся не допустимые символы!");
		messages.put("message.command.changepassword.uncorrect.newpassword3", "§aAuth §6> §cПароль должен быть длинной не менее {0} символов!");
		messages.put("message.command.changepassword.success", "§aAuth §6> §aПароль успепшно изменен!");
		messages.put("message.command.clearlag.entity", "§aClearLag §6> §eВсе Entity удалены.");
		messages.put("message.command.clearlag.items", "§aClearLag §6> §eВсе предметы удалены.");
		messages.put("message.command.clearlag.mobs", "§aClearLag §6> §eВсе мобы удалены.");
		messages.put("message.command.clearlag.stats", "§aClearLag §6> §eChunks §6{0}§e, Entity §6{1}§e:{2}");
		messages.put("message.command.gamemode", "§aGameMode §6> §7Режим игры установлен на §6{0}§7.");
		messages.put("message.command.gamemode.stop", "§cНикакой смены режима больше!");
		messages.put("message.command.gamemodeplus", "§aGameMode §6> §7Режим игры для игрока §6{0}§7 установлен на §6{1}§7.");
		messages.put("message.command.getitemnbt", "§aItem §6> §eNBT: §7{0}");
		messages.put("message.command.getitemnbt.needitem", "§aItem §6> §cВы должны держать предмет в руке.");
		messages.put("message.command.login.already.authorized", "§aAuth §6> §aВы уже авторизованы!");
		messages.put("message.command.login.authorized", "§aAuth §6> §aВы успешно авторизованы!");
		messages.put("message.command.login.wrongpassword", "§aAuth §6> §cНе верный пароль!");
		messages.put("message.command.register.already.authorized", "§aAuth §6> §aВы уже авторизованы!");
		messages.put("message.command.register.already.registered", "§aAuth §6> §eВы уже зарегистрированы, используйте: §6/l <пароль>");
		messages.put("message.command.register.wrong1", "§aAuth §6> §cПароли должны совпадать!");
		messages.put("message.command.register.wrong2", "§aAuth §6> §cВ пароле содержатся не допустимые символы!");
		messages.put("message.command.register.wrong3", "§aAuth §6> §cПароль должен быть длинной не менее 8 символов!");
		messages.put("message.command.register.success", "§aAuth §6> §aВы успешно авторизованы!");
		messages.put("message.command.seen.result", "§aSeen §6> §eСписок аккаунтов игрока §6{0}§e: §f");
		messages.put("message.command.teleport.toplayer", "§aTeleport §6> §7Вы отправили запрос на телепортацию игроку §6{0}§7.");
		messages.put("message.command.teleport.totarget", "§aTeleport §6> §6{0}§7 отправил вам запрос на телепортацию.");
		messages.put("message.command.getstats.result", "§aИгрок §6{0}§a вскопал §6{1}§a блока §6{2}§a.");
		messages.put("message.command.restart.result", "§dСервер будет перезапущен через {0} секунд");
		messages.put("message.command.badge.notfound", "§eУказанный вами значек не существует");
		messages.put("message.command.badge.notaccess", "§eВы еще не получили этот значек");
		messages.put("message.command.badge.select", "§aВы успешно установили значек ");
		messages.put("message.command.badge.hide", "§aВы скрыли свой значек");
		messages.put("message.command.badge.set.true", "§7Игроку §6{0} §7установлен на §atrue §7значек ");
		messages.put("message.command.badge.set.false", "§7Игроку §6{0} §7установлен на §cfalse §7значек ");
		messages.put("message.command.badge.give.alreadyhas", "§7Игрок §6{0} §7уже имеет этот значек");
		messages.put("message.command.toggleevent.true", "§7Ивент §aПодстава от блоков §7начинается!");
		messages.put("message.command.toggleevent.false", "§7Ивент §aПодстава от блоков §7завершен!");
		messages.put("message.command.eventreload", "§7Ивент перезагружен!");
		messages.put("message.command.tpaccept.norequests", "§eУ вас нет активных запросов на телепортацию");
		messages.put("message.command.home.result", "§aДом милый дом!");
		messages.put("message.command.call.nodiamonds", "§cВам необходимо §6{0} алм. §cдля телепортации к этому игроку!");
		messages.put("message.command.call.nodiamonds.target", "§cУ §6{0} §cне достаточно алмазов для телепортации!");
		messages.put("message.command.tp.result", "§7Вы телепортированы к игроку §6{0}");
		messages.put("message.command.lighting.result", "§7Игрок §6{0} §7получил удар молнией");
		messages.put("message.command.setplaytime.result", "§7Игроку §6{0} §7установлено наигранное время на §6{1} сек.");
		messages.put("message.command.loadchunk.notfound", "§cБэкап §6{0} §cне найден");
		
		messages.put("command.changepassword.usage", "/changepassword <текущий пароль> <новый пароль> <повтор пароля>");
		messages.put("command.clearlag.usage", "/clearlag <all/items/mobs/stats>");
		messages.put("command.gamemode.usage", "/gm <adventure/creative/survival/spectator>");
		messages.put("command.login.usage", "/login <пароль>");
		messages.put("command.register.usage", "/register <пароль> <повтор пароля>");
		messages.put("command.seen.usage", "/seen <игрок>");
		messages.put("command.call.usage", "/call <игрок>");
		messages.put("command.getstats.usage", "/getstats <игрок> <блок>");
		messages.put("command.restart.usage", "/restart <время>");
		messages.put("command.badge.usage", "/badge select <значек>");
		messages.put("command.badge.usage.set", "/badge set <игрок> <значек> <true/false>");
		messages.put("command.badge.usage.give", "/badge set <игрок> <значек>");
		messages.put("command.badge.usage.admin", "/badge <select/set/give>");
		messages.put("command.giveitem.usage", "/giveitem <player> <item> <amount>");
		messages.put("command.lighting.usage", "/lighting <player>");
		messages.put("command.setplaytime.usage", "/setplaytime <player> <time>");
		messages.put("command.loadchunk.usage", "/loadchunk <backup_name>");
		
		messages.put("gamemode.survival", "Выживание");
		messages.put("gamemode.creative", "Творческий");
		messages.put("gamemode.adventure", "Приключение");
		messages.put("gamemode.spectator", "Наблюдатель");
		
		messages.put("title.welcome.1", "\uE344§aДобро пожаловать!§f\uE344");
		
		messages.put("message.need.authorized", "§aAuth §6> §eАвторизуйтесь: §6/l <пароль>");
		messages.put("message.need.registered", "§aAuth §6> §eЗарегистрируйтесь на сервере: §6/reg <пароль> <повтор пароля>");
		
		messages.put("message.badge.give", "§aИгрок §6{0} §aполучил новый значек ");
		messages.put("message.badge.icon", "§6[§f{0}§6]");
		
		messages.put("message.trade.nodiamonds", "§cНе достаточно алмазов для обмена!");
		
		messages.put("message.player.giveitem", "§aВы получили §6{0} x{1}");
		messages.put("message.player.getkeepinventory", "§aУ вас действует сохранение инвенторя, осталось §6{0}");
		messages.put("message.player.losekeepinventory", "§eУ вас закончилсоь время работы сохранения инвенторя.");
		
		messages.put("message.backuploader.chunk.start", "§eНачинаю загрузку чанка §6({0}, {1}, {2}) §eиз бэкапа §6{3}");
	}
}
