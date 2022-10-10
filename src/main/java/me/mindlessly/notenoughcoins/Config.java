package me.mindlessly.notenoughcoins;

import java.io.File;
import java.net.URI;
import java.util.Arrays;

import gg.essential.universal.UDesktop;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import me.mindlessly.notenoughcoins.client.Client;

public class Config extends Vigilant {
	@Property(type = PropertyType.SWITCH, category = "Flipping", subcategory = "Basic", name = "Enabled", description = "Whether the mod should check for and send flips")
	public static boolean enabled = false;
	@Property(type = PropertyType.NUMBER, category = "Flipping", subcategory = "Basic", name = "Minimum Profit", description = "The minimum amount of profit that is required for the mod to send you a flip", max = Integer.MAX_VALUE, increment = 10000)
	public static int minProfit = 50000;
	@Property(type = PropertyType.SWITCH, category = "Flipping", subcategory = "Basic", name = "Alert Sounds", description = "Whether a pling sound should be played upon flip sent")
	public static boolean alertSounds = true;
	@Property(type = PropertyType.TEXT, category = "Confidential", name = "API Key", protectedText = true, description = "Run /api new to set it automatically, or paste one if you do not want to renew it")
	public static String apiKey = "";

	public static final File CONFIG_FILE = new File("config/cc.toml");
	public static final Config INSTANCE = new Config();

	public Config() {
		super(CONFIG_FILE, "Coins Client Menu");
		initialize();
		Arrays.asList("enabled", "apiKey").forEach(property -> registerListener(property, e -> Client.checkIfUpdate()));
		Arrays.asList("minProfit", "minProfitPercentage", "threads", "alertSounds")
				.forEach(property -> addDependency(property, "enabled"));
	}

	@Property(type = PropertyType.BUTTON, category = "Links", name = "Discord", description = "Join our Discord server!")
	public static void discord() {
		UDesktop.browse(URI.create("https://discord.gg/b3JBsh8fEd"));
	}

	@Property(type = PropertyType.BUTTON, category = "Links", name = "GitHub", description = "Help with the development!")
	public static void github() {
		UDesktop.browse(URI.create("https://github.com/mindlesslydev/NotEnoughCoins"));
	}
}