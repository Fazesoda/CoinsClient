package me.mindlessly.coinsclient.events;

import me.mindlessly.coinsclient.Config;
import me.mindlessly.coinsclient.utils.Utils;
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