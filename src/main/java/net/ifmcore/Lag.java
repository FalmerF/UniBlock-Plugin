package net.ifmcore;

public class Lag implements Runnable
{
	public static double tps;
	private static long lastTime = System.currentTimeMillis();
	private static int ticks = 20;

	public static float getTPS()
	{
		return ((int)Math.round(tps*100))/100f;
	}

	public void run()
	{
		long currentTime = System.currentTimeMillis();
		if(currentTime/1000 == lastTime/1000) {
			ticks++;
		}
		else {
			long dif = currentTime - lastTime;
			if(ticks == 0)
				tps = 0;
			else
				tps = Math.min(1000d/(dif/ticks), 20);
			ticks = 0;
			lastTime = currentTime;
		}
	}
}
