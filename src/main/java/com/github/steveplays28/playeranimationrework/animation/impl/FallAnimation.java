package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class FallAnimation extends Animation {
	private static final String LONG_FALL_ANIMATION_NAME = "falling";
	private static final String[] SHORT_FALL_ANIMATION_NAMES = new String[]{"fall_first", "fall_second"};
	private static final String LANDING_ANIMATION_NAME = "landing";

	private boolean isFallIndexOdd = false;
	private float lastFallDistance;

	@Override
	public void fall(@NotNull AbstractClientPlayerEntity player, double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
		super.tick(player);

		if ((lastFallDistance > 3 && onGround) || player.fallDistance > 3 || !onGround) {
			shouldPlay = true;
		}

		this.lastFallDistance = player.fallDistance;
	}

	@Override
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		if (player.isOnGround()) {
			this.isFallIndexOdd = !this.isFallIndexOdd;
		}

		if (lastFallDistance > 3 && player.isOnGround()) {
			return LANDING_ANIMATION_NAME;
		} else if (player.fallDistance > 3) {
			return LONG_FALL_ANIMATION_NAME;
		} else if (!player.isOnGround()) {
			return SHORT_FALL_ANIMATION_NAMES[isFallIndexOdd ? 1 : 0];
		}

		return null;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.FALL;
	}
}
