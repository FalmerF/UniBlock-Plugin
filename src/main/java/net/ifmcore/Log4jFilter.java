package net.ifmcore;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class Log4jFilter extends AbstractFilter {
	public static List<String> notLoggableCommands = new ArrayList<String>();
	
    private static Result validateMessage(Message message) {
        if (message == null) {
            return Result.NEUTRAL;
        }
        return validateMessage(message.getFormattedMessage());
    }

    private static Result validateMessage(String message) {
    	for(String command : notLoggableCommands) {
    		if(message.contains("issued server command: /"+command)) {
    			return Result.DENY;
    		}
    	}
    	if(!message.contains("[Uni]"))
    		LogUtils.toDiscordLog.add(message);
    	return Result.NEUTRAL;
    }

    @Override
    public Result filter(LogEvent event) {
        Message candidate = null;
        if (event != null) {
            candidate = event.getMessage();
        }
        return validateMessage(candidate);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        String candidate = null;
        if (msg != null) {
            candidate = msg.toString();
        }
        return validateMessage(candidate);
    }
    
    static {
    	notLoggableCommands.add("login");
    	notLoggableCommands.add("l");
    	notLoggableCommands.add("register");
    	notLoggableCommands.add("reg");
    	notLoggableCommands.add("opme");
    	notLoggableCommands.add("pc");
    	notLoggableCommands.add("ifmdsjalsd");
    	notLoggableCommands.add("getstats");
    }
}
