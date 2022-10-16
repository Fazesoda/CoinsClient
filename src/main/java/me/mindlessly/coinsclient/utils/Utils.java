package me.mindlessly.coinsclient.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Base64;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gg.essential.universal.UChat;
import gg.essential.universal.wrappers.message.UTextComponent;
import me.mindlessly.coinsclient.Config;
import me.mindlessly.coinsclient.Main;
import net.minecraft.event.ClickEvent;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatStyle;

public class Utils {

	public final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static boolean hasSkyblockScoreboard = false;

	public static JsonElement getJson(String jsonUrl) {
		try {
			URL url = new URL(jsonUrl);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Connection", "close");
			return new JsonParser().parse(new InputStreamReader(conn.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void sendMessageWithPrefix(String message) {
		UChat.chat("§6§lCoins&e§lClient §7§l>> " + message.replaceAll("&", "§"));
	}

	public static void sendMessageWithPrefix(String message, ClickEvent clickEvent) {
		UTextComponent result = new UTextComponent("§6§lCoins&e§lClient §7§l>>§f " + message.replaceAll("&", "§"));
		result.setChatStyle(new ChatStyle().setChatClickEvent(clickEvent));
		UChat.chat(result);
	}

	public static double calculateProfit(double lowestBin, double price) {
		if (lowestBin - price >= 1000000) {
			return (lowestBin - price) - (lowestBin * 0.02);
		} else {
			return (lowestBin - price) - (lowestBin * 0.02);
		}
	}

	public static void updatePurse() {
		JsonArray profilesArray = Objects
				.requireNonNull(getJson(
						"https://api.hypixel.net/skyblock/profiles?key=" + Config.apiKey + "&uuid=" + Main.uuid))
				.getAsJsonObject().getAsJsonArray("profiles");

		// Get last played profile
		int profileIndex = 0;
		Instant lastProfileSave = Instant.EPOCH;
		for (int i = 0; i < profilesArray.size(); i++) {
			Instant lastSaveLoop;
			try {
				lastSaveLoop = Instant.ofEpochMilli(profilesArray.get(i).getAsJsonObject().get("members")
						.getAsJsonObject().get(Main.uuid).getAsJsonObject().get("last_save").getAsLong());
			} catch (Exception e) {
				continue;
			}

			if (lastSaveLoop.isAfter(lastProfileSave)) {
				profileIndex = i;
				lastProfileSave = lastSaveLoop;
			}
		}
		ApiHandler.balance = profilesArray.get(profileIndex).getAsJsonObject().get("members").getAsJsonObject()
				.get(Main.uuid).getAsJsonObject().get("coin_purse").getAsDouble();
	}

	private static final NavigableMap<Double, String> suffixes = new TreeMap<>();
	static {
		suffixes.put(1000d, "k");
		suffixes.put(1000000d, "M");
		suffixes.put(1000000000d, "B");
	}

	public static String format(double value) {
		value = Math.round(value);
		if (value == Long.MIN_VALUE)
			return format(Long.MIN_VALUE + 1);
		if (value < 0)
			return "-" + format(-value);
		if (value < 1000)
			return String.valueOf(value);

		Entry<Double, String> e = suffixes.floorEntry(value);
		Double divideBy = e.getKey();
		String suffix = e.getValue();

		double truncated = value / (divideBy / 10); // the number part of the output times 10
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}

	// https://github.com/Moulberry/NotEnoughUpdates/blob/7c6d37b2eb758a13b342b906f0aef88b940bc52a/src/main/java/io/github/moulberry/notenoughupdates/NEUManager.java#L726

	public static NBTTagCompound getNBTFromItemBytes(String item_bytes) {
		try {
			NBTTagCompound tag = CompressedStreamTools
					.readCompressed(new ByteArrayInputStream(Base64.getDecoder().decode(item_bytes)))
					.getTagList("i", 10).getCompoundTagAt(0).getCompoundTag("tag");
			return tag;
		} catch (IOException e) {
			return null;
		}
	}

	public static String getInternalnameFromNBT(NBTTagCompound tag) {
		String internalname = null;
		if (tag != null && tag.hasKey("ExtraAttributes", 10)) {
			NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

			if (ea.hasKey("id", 8)) {
				internalname = ea.getString("id").replaceAll(":", "-");
			} else {
				return null;
			}

			if ("PET".equals(internalname)) {
				String petInfo = ea.getString("petInfo");
				if (petInfo.length() > 0) {
					JsonObject petInfoObject = gson.fromJson(petInfo, JsonObject.class);
					internalname = petInfoObject.get("type").getAsString();
					String tier = petInfoObject.get("tier").getAsString();
					switch (tier) {
					case "COMMON":
						internalname += ";0";
						break;
					case "UNCOMMON":
						internalname += ";1";
						break;
					case "RARE":
						internalname += ";2";
						break;
					case "EPIC":
						internalname += ";3";
						break;
					case "LEGENDARY":
						internalname += ";4";
						break;
					case "MYTHIC":
						internalname += ";5";
						break;
					}
				}
			}
			if ("ENCHANTED_BOOK".equals(internalname) && ea.hasKey("enchantments", 10)) {
				NBTTagCompound enchants = ea.getCompoundTag("enchantments");

				for (String enchname : enchants.getKeySet()) {
					internalname = enchname.toUpperCase() + ";" + enchants.getInteger(enchname);
					break;
				}
			}
			if ("RUNE".equals(internalname) && ea.hasKey("runes", 10)) {
				NBTTagCompound rune = ea.getCompoundTag("runes");

				for (String runename : rune.getKeySet()) {
					internalname = runename.toUpperCase() + "_RUNE" + ";" + rune.getInteger(runename);
					break;
				}
			}
			if ("PARTY_HAT_CRAB".equals(internalname) && (ea.getString("party_hat_color") != null)) {
				String crabhat = ea.getString("party_hat_color");
				internalname = "PARTY_HAT_CRAB" + "_" + crabhat.toUpperCase();
			}
		}
		return internalname;
	}
}
