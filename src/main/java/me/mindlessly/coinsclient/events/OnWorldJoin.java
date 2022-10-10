package me.mindlessly.coinsclient.events;

import me.mindlessly.coinsclient.client.Client;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class OnWorldJoin {

	@SubscribeEvent
	public void onEntityJoinWorld(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		Client.checkIfUpdate();
	}
}
