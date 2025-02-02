package com.steveplays.playeranimationrework.client;

import static com.steveplays.playeranimationrework.PlayerAnimationRework.LOGGER;
import static com.steveplays.playeranimationrework.PlayerAnimationRework.MOD_NAME;
import com.steveplays.playeranimationrework.client.resource.PARResourceReloader;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resource.ResourceType;

public class PlayerAnimationReworkClient {
	public static void initialize() {
		LOGGER.info("Loading {}.", MOD_NAME);

		ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, new PARResourceReloader());
	}
}
