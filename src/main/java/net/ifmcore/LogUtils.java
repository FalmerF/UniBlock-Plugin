package net.ifmcore;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class LogUtils {
	public static List<String> toDiscordLog = new ArrayList<String>();
	public static List<String> toLog = new ArrayList<String>();
	public static Logger logger;
	public static FileHandler fh;
	public static SimpleDateFormat loggerDateFormat = new SimpleDateFormat("HH:mm:ss");
	
	public LogUtils() {
		try {
			File dir = new File(Uni.getPluginFolderPath()+"/logs");
			if(!dir.exists()) dir.mkdir();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String logFileName = "";
			for(int i = 1; ; i++) {
				logFileName = dir+"/"+sdf.format(new Date())+"_"+i+".log";
				File logFile = new File(logFileName);
				if(!logFile.exists()) {
					logFile.createNewFile();
					break;
				}
			}
			
			logger = Logger.getLogger(this.getClass().getName());
			fh = new FileHandler(logFileName);
	        SimpleFormatter formatter = new SimpleFormatter() {
	        	@Override
	            public String format(LogRecord lr) {
	                return "["+loggerDateFormat.format(new Date(lr.getMillis()))+"] "+lr.getMessage()+"\n";
	            }
            };
	        fh.setFormatter(formatter);
	        fh.setEncoding("UTF-8");
	        logger.addHandler(fh);
	        logger.setUseParentHandlers(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
        
		Uni.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Uni.instance, new  Runnable(){
			public void run() {
				String message = "";
				for(int i = 0; i < 5; i++) {
					if(toDiscordLog.size() <= 0)
						break;
					message += "\\n"+toDiscordLog.remove(0);
				}
				if(!message.equals(""))
					sendWebhook(message);

				
//				message = "";
//				for(int i = 0; i < 15; i++) {
//					if(toLog.size() <= 0)
//						break;
//					message += "\\n"+toLog.remove(0);
//				}
//				if(!message.equals(""))
//					Uni.log(message);
			}
		}, 5l, 20l);
	}
	
	private static void sendWebhook(String message) {
		message = clearAmpCodes(message);
		String json = "{ \n"
        		+ "  \"content\": \""+message+"\""
        		+ "}";
		try {
	        
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost("https://discord.com/api/webhooks/976152362307686430/H55IO830uJ9wIf5Lk3c6zC2cL8c2RmMmIisuCLmwQjRIDT2cni2A0sCKNViknU3jktLi");
	        StringEntity params = new StringEntity(json.toString(), Charset.forName("UTF-8"));
	        request.addHeader("content-type", "application/json");
	        request.setEntity(params);
	        httpClient.execute(request);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendLog(String message) {
		logger.info(message);
	}
	
	public static String convertAmpCodes(final String text) {
        return text.replaceAll("(?<!&)&([0-9a-fklmnor])", "§$1").replaceAll("&&", "&");
    }
	
	public static String clearAmpCodes(String text) {
        return text.replaceAll("(?<!&)§([0-9a-fklmnor])", "");
    }
	
	public static boolean hasAmpCodes(String text) {
		return Pattern.compile("(?<!&)§([0-57-9a-fklmno])").matcher(text).find();
	}
}
