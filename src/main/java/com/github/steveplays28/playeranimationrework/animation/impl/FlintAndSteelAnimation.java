package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import org.jetbrains.annotations.NotNull;

public class FlintAndSteelAnimation extends Animation {
	private static final String ANIMATION_NAME = "flint_and_steel";
	private static final String SNEAK_ANIMATION_NAME = "flint_and_steel_sneak";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		super.tick(player);

		if (!player.isUsingItem() || !(player.getActiveItem().getItem() instanceof FlintAndSteelItem)) {
			return;
		}

		shouldPlay = true;
	}

	@Override
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		if (player.isSneaking()) {
			return SNEAK_ANIMATION_NAME;
		} else {
			return ANIMATION_NAME;
		}
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.FLINT_AND_STEEL;
	}
}
