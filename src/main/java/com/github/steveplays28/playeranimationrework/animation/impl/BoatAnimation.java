package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.jetbrains.annotations.NotNull;

public class BoatAnimation extends Animation {
	private static final String IDLE_ANIMATION_NAME = "boat_idle";
	private static final String FORWARD_ANIMATION_NAME = "boat_forward";
	private static final String LEFT_PADDLE_ANIMATION_NAME = "boat_left_paddle";
	private static final String RIGHT_PADDLE_ANIMATION_NAME = "boat_right_paddle";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		if (!(player.getControllingVehicle() instanceof BoatEntity)) {
			return;
		}

		super.tick(player);
		shouldPlay = true;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.BOAT;
	}

	@Override
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		if (player.getControllingVehicle() instanceof BoatEntity boatEntity) {
			boolean leftPaddleMoving = boatEntity.isPaddleMoving(0);
			boolean rightPaddleMoving = boatEntity.isPaddleMoving(1);

			if (leftPaddleMoving && rightPaddleMoving) {
				return FORWARD_ANIMATION_NAME;
			} else if (leftPaddleMoving) {
				return LEFT_PADDLE_ANIMATION_NAME;
			} else if (rightPaddleMoving) {
				return RIGHT_PADDLE_ANIMATION_NAME;
			} else {
				return IDLE_ANIMATION_NAME;
			}
		}

		return null;
	}
}
