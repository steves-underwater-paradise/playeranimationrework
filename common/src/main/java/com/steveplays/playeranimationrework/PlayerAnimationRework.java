package com.steveplays.playeranimationrework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerAnimationRework {
	public static final String MOD_ID = "playeranimationrework";
	public static final String MOD_NAME = "Player Animation Rework";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static void initialize() {
		LOGGER.info("Loading {}.", MOD_NAME);
	}
}
