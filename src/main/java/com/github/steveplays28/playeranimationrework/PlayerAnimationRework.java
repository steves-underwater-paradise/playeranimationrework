package com.github.steveplays28.playeranimationrework;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class PlayerAnimationRework implements ClientModInitializer {
	public static final String MOD_ID = "player-animation-rework";
	public static final String MOD_NAMESPACE = "playeranimationrework";
	public static final String MOD_NAME = "Player Animation Rework";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
public static final String REAL_CAMERA_MOD_ID = "realcamera";

	@Override
	public void onInitializeClient() {
		LOGGER.info("Loading {}.", MOD_NAME);
	}
}
