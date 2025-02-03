package com.steveplays.playeranimationrework.fabric.client;

import com.steveplays.playeranimationrework.client.PlayerAnimationReworkClient;
import net.fabricmc.api.ClientModInitializer;

public class PlayerAnimationReworkClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PlayerAnimationReworkClient.initialize();
	}
}
