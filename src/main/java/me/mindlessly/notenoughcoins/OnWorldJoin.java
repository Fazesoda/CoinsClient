package me.mindlessly.notenoughcoins;

import me.mindlessly.notenoughcoins.client.Client;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class OnWorldJoin {

	@SubscribeEvent
	public void onEntityJoinWorld(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		Client.start();
	}
}
