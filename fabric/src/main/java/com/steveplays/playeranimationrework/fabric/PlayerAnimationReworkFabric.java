package com.steveplays.playeranimationrework.fabric;

import com.steveplays.playeranimationrework.PlayerAnimationRework;
import net.fabricmc.api.ModInitializer;

public class PlayerAnimationReworkFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		PlayerAnimationRework.initialize();
	}
}
