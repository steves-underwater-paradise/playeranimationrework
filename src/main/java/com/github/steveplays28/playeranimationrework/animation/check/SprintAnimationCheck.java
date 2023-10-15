package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import static com.github.steveplays28.playeranimationrework.util.AnimationUtil.getAnimation;

public class SprintAnimationCheck implements AnimationCheck {
	private static final String ANIMATION_NAME = "running";
	private static final String STOP_ANIMATION_NAME = "sprint_stop";

	private boolean shouldPlay = false;
	private boolean lastSprinting = false;
	private String selectedAnimationName;
	private int fadeTime = 5;

	@Override
	public void tick(AbstractClientPlayerEntity player) {
		if (player.isSprinting()) {
			this.selectedAnimationName = ANIMATION_NAME;
			this.shouldPlay = true;
		} else if (lastSprinting) {
			this.selectedAnimationName = STOP_ANIMATION_NAME;
			this.fadeTime = 2;
			this.shouldPlay = true;
		}

		this.lastSprinting = player.isSprinting();
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = getAnimation(selectedAnimationName);
		return new AnimationData(animation, 1.0f, fadeTime);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.SPRINT;
	}

	@Override
	public boolean getShouldPlay() {
		return this.shouldPlay;
	}

	@Override
	public void cleanup() {
		this.shouldPlay = false;
		this.selectedAnimationName = null;
		this.fadeTime = 5;
	}
}
