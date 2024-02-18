package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: Split into fall and land animations
public class FallAnimation extends Animation {
	private static final String LONG_FALL_ANIMATION_NAME = "falling";
	private static final String[] SHORT_FALL_ANIMATION_NAMES = new String[]{"fall_first", "fall_second"};
	private static final String LANDING_ANIMATION_NAME = "landing";

	private String selectedAnimationName;
	private boolean isFallIndexOdd = false;
	private float lastFallDistance;

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		// NOOP
		// Stops the getNewSelectedAnimationName() method from running every tick when it's unnecessary to do so
		// Instead it's manually invoked in fall()
	}

	@Override
	public void fall(@NotNull AbstractClientPlayerEntity player, double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
		if ((lastFallDistance > 3 && onGround) || player.fallDistance > 3 || !onGround) {
			selectedAnimationName = getNewSelectedAnimationName(player, onGround);
			shouldPlay = true;
		}

		lastFallDistance = player.fallDistance;
	}

	@Override
	public @Nullable String getSelectedAnimationName() {
		return selectedAnimationName;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.FALL;
	}

	@Override
	protected @Nullable String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		// NOOP
		return null;
	}

	protected @Nullable String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player, boolean onGround) {
		if (onGround) {
			this.isFallIndexOdd = !this.isFallIndexOdd;
		}

		if (lastFallDistance > 3 && onGround) {
			return LANDING_ANIMATION_NAME;
		} else if (player.fallDistance > 3) {
			return LONG_FALL_ANIMATION_NAME;
		} else if (!player.isOnGround()) {
			return SHORT_FALL_ANIMATION_NAMES[isFallIndexOdd ? 1 : 0];
		}

		return null;
	}
}
