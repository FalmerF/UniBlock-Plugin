package net.ifmcore.requests;

import org.bukkit.entity.Player;

public class Request {
	public float time;
	public Player player1, player2;
	
	public Request(Player player1, Player player2, float time) {
		this.time = time;
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public void update(float updateTime) {
		time -= updateTime;
		if(time <= updateTime)
			closeRequest();
	}
	public void closeRequest() {
		RequestManager.removeRequest(this);
	}
	public void acceptRequest() {}
	public void rejectRequest() {}
}
