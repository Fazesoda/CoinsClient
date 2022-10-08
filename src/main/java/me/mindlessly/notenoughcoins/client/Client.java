package me.mindlessly.notenoughcoins.client;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.mindlessly.notenoughcoins.utils.ApiHandler;
import me.mindlessly.notenoughcoins.utils.Utils;

public class Client {
	public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
	static int increment = 0;
	static int totalPages = 0;
	
	public static void start() {
		scheduledExecutorService.shutdownNow();
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.schedule(() -> flip(), 1, TimeUnit.SECONDS);
	}

	private static void flip() {
		try {
			ApiHandler.getBins();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			totalPages = Objects.requireNonNull(Utils.getJson("https://api.hypixel.net/skyblock/auctions?page=" + 0))
					.getAsJsonObject().get("totalPages").getAsInt();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		scheduledExecutorService.scheduleAtFixedRate(() -> {
			try {
				ApiHandler.getFlips(increment);
			} catch (IOException e) {
				e.printStackTrace();
			}
			increment++;
			if(increment == totalPages) {
				increment = 0;
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
		/*scheduledExecutorService.scheduleAtFixedRate(() -> {
			ApiHandler.getBins();
		}, 0, 60000, TimeUnit.MILLISECONDS);*/
	}
}
