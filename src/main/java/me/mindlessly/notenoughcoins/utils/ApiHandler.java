package me.mindlessly.notenoughcoins.utils;

import static me.mindlessly.notenoughcoins.utils.Utils.getInternalnameFromNBT;
import static me.mindlessly.notenoughcoins.utils.Utils.getJson;
import static me.mindlessly.notenoughcoins.utils.Utils.getNBTFromItemBytes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

public class ApiHandler {

	static JsonObject binJson;

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
			String name = new String(current.get("item_name").getAsString().getBytes(), StandardCharsets.UTF_8);
			if (current.has("bin") && current.get("bin").getAsString().equals("true")) {
				for (Map.Entry<String, JsonElement> auction : binJson.entrySet()) {
					if (price < auction.getValue().getAsDouble() && internalID.equals(auction.getKey())) {
						ChatComponentText flip = new ChatComponentText(
								name + " +" + String.valueOf(Math.round(auction.getValue().getAsDouble() - price)));
						ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(
								ClickEvent.Action.RUN_COMMAND, "/viewauction " + current.get("uuid").getAsString()));
						flip.setChatStyle(style);
						Minecraft.getMinecraft().thePlayer.addChatMessage(flip);
						break;
					}
				}
			}
		}
	}
}
