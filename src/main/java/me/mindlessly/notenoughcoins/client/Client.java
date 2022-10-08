package me.mindlessly.notenoughcoins.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.mindlessly.notenoughcoins.utils.ApiHandler;
import me.mindlessly.notenoughcoins.utils.Auction;

public class Client {

	public static HashMap<String, Double> rawBinPrices = new HashMap<String, Double>();
	public static ArrayList<Auction> binPrices = new ArrayList<>();
	public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
	public static void start() {
		scheduledExecutorService.shutdownNow();
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.schedule(() -> flip(), 1, TimeUnit.SECONDS);
	}

	private static void flip() {
		try {
			ApiHandler.getBins();
			ApiHandler.itemIdsToNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
		scheduledExecutorService.scheduleAtFixedRate(() -> {
			ApiHandler.getFlips();
		}, 0, 10000, TimeUnit.MILLISECONDS);

	}
}
