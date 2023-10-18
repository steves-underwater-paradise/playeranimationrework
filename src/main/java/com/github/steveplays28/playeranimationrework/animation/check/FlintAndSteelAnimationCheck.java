package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.FlintAndSteelItem;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getAnimation;

public class FlintAndSteelAnimationCheck implements AnimationCheck {
	private static final String ANIMATION_NAME = "flint_and_steel";
	private static final String SNEAK_ANIMATION_NAME = "flint_and_steel_sneak";

	private boolean shouldPlay = false;
	private String selectedAnimationName;

	@Override
	public void tick(AbstractClientPlayerEntity player) {
		if (!player.isUsingItem() || !(player.getActiveItem().getItem() instanceof FlintAndSteelItem)) {
			return;
		}

		this.shouldPlay = true;

		if (player.isSneaking()) {
			this.selectedAnimationName = SNEAK_ANIMATION_NAME;
		} else {
			this.selectedAnimationName = ANIMATION_NAME;
		}
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = getAnimation(selectedAnimationName);
		return new AnimationData(animation, 1.0f, 5);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.FLINT_AND_STEEL;
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
