package com.github.steveplays28.playeranimationrework.animation.check;

import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getAnimation;

public class ElytraAnimationCheck implements AnimationCheck {
	private static final String ANIMATION_NAME = "elytra_fly";

	private boolean shouldPlay = false;

	@Override
	public void tick(AbstractClientPlayerEntity player) {
//		this.shouldPlay = player.isFallFlying();
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = getAnimation(ANIMATION_NAME);
		return new AnimationData(animation, 1.0f, 5);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.ELYTRA;
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
