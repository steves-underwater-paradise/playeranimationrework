package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class EatingAnimation extends Animation {
	private static final String ANIMATION_NAME = "eating";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		shouldPlay = player.isUsingItem() && player.getActiveItem().getItem().isFood();
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.EATING;
	}

	@Override
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		return ANIMATION_NAME;
	}
}
