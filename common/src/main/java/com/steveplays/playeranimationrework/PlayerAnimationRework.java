package com.steveplays.playeranimationrework;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.steveplays.playeranimationrework.client.resource.PARResourceReloader;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resource.ResourceType;

public class PlayerAnimationRework {
	public static final @NotNull String MOD_ID = "playeranimationrework";
	public static final @NotNull String MOD_NAME = "Player Animation Rework";
	public static final @NotNull Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final float TICKS_PER_SECOND = 20f;

	public static void initialize() {
		LOGGER.info("Loading {}.", MOD_NAME);

		ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, new PARResourceReloader());
	}
}
