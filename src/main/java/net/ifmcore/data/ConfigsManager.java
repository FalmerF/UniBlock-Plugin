package net.ifmcore.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONObject;

import net.ifmcore.Uni;

public class ConfigsManager {
	public static ConfigsManager instance;
	public static Map<String, String> chatSettings = new TreeMap<String, String>();
	public static HashMap<String, String> localization = new HashMap<String, String>();
	
	public ConfigsManager() {
		instance = this;
		loadConfigs();
	}
	
	public void loadConfigs() {
		loadLang();
	}
	
	void loadLang() {
		chatSettings.clear();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Uni.pluginFolder+"/ru.lang")), StandardCharsets.UTF_8));
	        String line = "";
	        while ((line = br.readLine()) != null) {
	        	String[] data = line.split(": ");
	        	localization.put(data[0], StringEscapeUtils.unescapeJava(data[1]));
	        }
	        br.close();
	    }
		catch(Exception e) {
			
		}
	}
}
