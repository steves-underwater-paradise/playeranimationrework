package com.github.steveplays28.playeranimationrework.client.animation;

import net.minecraft.entity.player.PlayerEntity;

public interface IAnimation {
	IAnimation register();

	int getPriority();

	void start(PlayerEntity player);

	void stop(PlayerEntity player);

	void continueWithSpecificBodyParts();
}
