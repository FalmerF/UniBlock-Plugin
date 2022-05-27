package net.ifmcore.requests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class RequestManager {
	static List<Request> requests = new ArrayList<Request>();
	
	public static void addRequest(Request request) {
		requests.add(request);
	}
	
	public static void removeRequest(Request request) {
		requests.remove(request);
	}
	
	public static Request getRequest(Player player, Class requestClass) {
		for(Request r : requests) {
			if(r.player2 == player && r.getClass() == requestClass) {
				return r;
			}
		}
		return null;
	}
	
	public static void updateRequests(float updateTime) {
		for(Request r : requests) {
			r.update(updateTime);
		}
	}
}
