package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.Animation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ElytraAnimation extends Animation {
	private static final String ANIMATION_NAME = "elytra_fly";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		// TODO: Implement elytra animation
//		this.shouldPlay = player.isFallFlying();
	}

	@Override
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		return ANIMATION_NAME;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.ELYTRA;
	}
}
