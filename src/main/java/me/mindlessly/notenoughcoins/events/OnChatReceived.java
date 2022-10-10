package me.mindlessly.notenoughcoins.events;

import me.mindlessly.notenoughcoins.Config;
import me.mindlessly.notenoughcoins.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OnChatReceived {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void chat(ClientChatReceivedEvent event) {
		String message = event.message.getUnformattedText();
		if (message.startsWith("Your new API key is ")) {
			String key = message.split("key is ")[1];
			Config.apiKey = key;
			Utils.sendMessageWithPrefix("§aAPI Key set to " + key);
		}
	}
}