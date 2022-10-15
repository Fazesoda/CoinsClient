package me.mindlessly.coinsclient.utils;

import static me.mindlessly.coinsclient.utils.Utils.getInternalnameFromNBT;
import static me.mindlessly.coinsclient.utils.Utils.getJson;
import static me.mindlessly.coinsclient.utils.Utils.getNBTFromItemBytes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import gg.essential.universal.USound;
import me.mindlessly.coinsclient.Config;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ResourceLocation;

public class ApiHandler {

	static JsonObject binJson;
	public static double balance;

	public static void getBins() {
		binJson = Objects.requireNonNull(getJson("https://moulberry.codes/lowestbin.json")).getAsJsonObject();
	}

	public static void getFlips(int i) throws IOException {
		JsonObject auctionPage = Objects.requireNonNull(getJson("https://api.hypixel.net/skyblock/auctions?page=" + i))
				.getAsJsonObject();
		JsonArray auctionsArray = auctionPage.get("auctions").getAsJsonArray();
		for (JsonElement item : auctionsArray) {
			JsonObject current = item.getAsJsonObject();
			String internalID = getInternalnameFromNBT(getNBTFromItemBytes(current.get("item_bytes").getAsString()));
			double price = current.get("starting_bid").getAsDouble();
			if (price <= balance) {
				String name = new String(current.get("item_name").getAsString().getBytes(), StandardCharsets.UTF_8);
				if (current.has("bin") && current.get("bin").getAsString().equals("true")) {
					for (Map.Entry<String, JsonElement> auction : binJson.entrySet()) {
						if (Utils.calculateProfit(auction.getValue().getAsDouble(), price) > Config.minProfit
								&& internalID.equals(auction.getKey())) {
							Utils.sendMessageWithPrefix(
									name + " +" + Utils.format(auction.getValue().getAsDouble() - price),
									new ClickEvent(ClickEvent.Action.RUN_COMMAND,
											"/viewauction " + current.get("uuid").getAsString()));
							if (Config.alertSounds) {
								USound.INSTANCE.playSoundStatic(new ResourceLocation("note.pling"), 2F, 1.0F);
							}
							break;
						}
					}
				}
			}
		}
	}
}
