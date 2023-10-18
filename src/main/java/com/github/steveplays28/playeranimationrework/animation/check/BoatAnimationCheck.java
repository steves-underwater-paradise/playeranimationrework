package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getAnimation;

public class BoatAnimationCheck implements AnimationCheck {
	private static final String IDLE_ANIMATION_NAME = "boat_idle";
	private static final String FORWARD_ANIMATION_NAME = "boat_forward";
	private static final String LEFT_PADDLE_ANIMATION_NAME = "boat_left_paddle";
	private static final String RIGHT_PADDLE_ANIMATION_NAME = "boat_right_paddle";

	private boolean shouldPlay = false;
	private String selectedAnimationName;

	@Override
	public void tick(AbstractClientPlayerEntity player) {
		Entity vehicle = player.getControllingVehicle();

		if (!(vehicle instanceof BoatEntity)) {
			return;
		}

		this.shouldPlay = true;

		boolean leftPaddleMoving = ((BoatEntity) vehicle).isPaddleMoving(0);
		boolean rightPaddleMoving = ((BoatEntity) vehicle).isPaddleMoving(1);

		if (leftPaddleMoving && rightPaddleMoving) {
			this.selectedAnimationName = FORWARD_ANIMATION_NAME;
		} else if (leftPaddleMoving) {
			this.selectedAnimationName = LEFT_PADDLE_ANIMATION_NAME;
		} else if (rightPaddleMoving) {
			this.selectedAnimationName = RIGHT_PADDLE_ANIMATION_NAME;
		} else {
			this.selectedAnimationName = IDLE_ANIMATION_NAME;
		}
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = getAnimation(selectedAnimationName);
		return new AnimationData(animation, 1.0f, 5);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.BOAT;
	}

	@Override
	public boolean getShouldPlay() {
		return this.shouldPlay;
	}

	@Override
	public void cleanup() {
		this.shouldPlay = false;
		this.selectedAnimationName = null;
	}
}
