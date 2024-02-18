package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.Animation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class SneakAnimation extends Animation {

	private static final String IDLE_ANIMATION_NAME = "sneak_idle";
	private static final String WALK_ANIMATION_NAME = "sneak_walk";

	private float lastBodyYaw;

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		if (!player.isInSneakingPose()) {
			return;
		}

		super.tick(player);
		this.shouldPlay = true;
		this.lastBodyYaw = player.getBodyYaw();
	}

	@Override
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		if (!player.isInSneakingPose()) {
			return null;
		}

		boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;
		float bodyYawDelta = player.getBodyYaw() - this.lastBodyYaw;

		if (isMoving || Math.abs(bodyYawDelta) > 3) {
			return WALK_ANIMATION_NAME;
		} else {
			return IDLE_ANIMATION_NAME;
		}
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.SNEAK;
	}
}
