package com.github.steveplays28.playeranimationrework.animation.check;

import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import static com.github.steveplays28.playeranimationrework.util.AnimationUtil.getAnimationIdentifier;

public class EatingAnimationCheck implements AnimationCheck {
	private static final String ANIMATION_NAME = "eating";

	private boolean shouldPlay = false;

	@Override
	public void tick(AbstractClientPlayerEntity player) {
		this.shouldPlay = player.isUsingItem() && player.getActiveItem().getItem().isFood();
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(getAnimationIdentifier(ANIMATION_NAME));
		return new AnimationData(animation, 1.0f, 5);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.EATING;
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
