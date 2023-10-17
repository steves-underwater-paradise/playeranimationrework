package com.github.steveplays28.playeranimationrework.animation.check;

import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import static com.github.steveplays28.playeranimationrework.util.AnimationUtil.getAnimation;

public class SwimAnimationCheck implements AnimationCheck {
	private static final String SWIM_ANIMATION_NAME = "swimming";
	private static final String SWIM_IDLE_ANIMATION_NAME = "swim_idle";

	private boolean shouldPlay = false;
	private String selectedAnimationName;

	@Override
	public void tick(AbstractClientPlayerEntity player) {
		if (player.isInSwimmingPose()) {
			shouldPlay = true;
			selectedAnimationName = SWIM_ANIMATION_NAME;
		} else if (player.isInsideWaterOrBubbleColumn()) {
			shouldPlay = true;
			selectedAnimationName = SWIM_IDLE_ANIMATION_NAME;
		}
	}

	@Override
	public AnimationData getAnimationData() {
		return new AnimationData(getAnimation(selectedAnimationName), 1.0f, 5);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.SWIM;
	}

	@Override
	public boolean getShouldPlay() {
		return this.shouldPlay;
	}

	@Override
	public void cleanup() {
		this.shouldPlay = false;
	}
}
