package me.mindlessly.notenoughcoins.utils;

import static me.mindlessly.notenoughcoins.utils.Utils.getJson;

import java.util.Map;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.mindlessly.notenoughcoins.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ApiHandler {

	public static void getBins() {
		Client.rawBinPrices.clear();
		try {
			JsonObject binJson = Objects.requireNonNull(getJson("https://moulberry.codes/lowestbin.json"))
					.getAsJsonObject();
			for (Map.Entry<String, JsonElement> auction : binJson.entrySet()) {
				Client.rawBinPrices.put(auction.getKey(), auction.getValue().getAsDouble());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void itemIdsToNames() {
		Client.binPrices.clear();

		try {
			JsonArray itemArray = Objects.requireNonNull(getJson("https://api.hypixel.net/resources/skyblock/items"))
					.getAsJsonObject().get("items").getAsJsonArray();

			for (JsonElement item : itemArray) {
				for (Map.Entry<String, Double> auction : Client.rawBinPrices.entrySet()) {
					if (item.getAsJsonObject().get("id").getAsString().equals(auction.getKey())) {
						Client.binPrices.add(new Auction(item.getAsJsonObject().get("name").getAsString(),
								item.getAsJsonObject().get("id").getAsString(), auction.getValue(),
								item.getAsJsonObject().has("tier") ? item.getAsJsonObject().get("tier").getAsString()
										: ""));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getFlips() {
		try {
			int totalPages = Objects.requireNonNull(getJson("https://api.hypixel.net/skyblock/auctions?page=" + 0))
					.getAsJsonObject().get("totalPages").getAsInt();

			for (int i = 0; i < totalPages; i++) {
				JsonObject auctionPage = Objects
						.requireNonNull(getJson("https://api.hypixel.net/skyblock/auctions?page=" + i))
						.getAsJsonObject();
				JsonArray auctionsArray = auctionPage.get("auctions").getAsJsonArray();
				for (JsonElement item : auctionsArray) {
					JsonObject current = item.getAsJsonObject();
					for (Auction auction : Client.binPrices) {
						System.out.println(current.get("tier").getAsString() + " " + auction.getTier());
						if (current.has("bin") && current.get("item_name").getAsString().contains(auction.getName())
						/* && current.get("tier").getAsString() == auction.getTier() */) {
							double price = current.get("starting_bid").getAsDouble();
							if (price < auction.getPrice()) {
								Minecraft.getMinecraft().thePlayer
										.addChatMessage(new ChatComponentText(current.get("item_name").getAsString()
												+ " +" + String.valueOf((auction.getPrice() - price))));
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
