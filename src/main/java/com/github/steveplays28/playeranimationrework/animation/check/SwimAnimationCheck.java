package com.github.steveplays28.playeranimationrework.animation.check;

import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.PlayerAnimationRework;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class SwimAnimationCheck implements AnimationCheck {
	private static final String SWIM_IDLE_ANIMATION_NAME = "swim_idle";

	private boolean shouldPlay = false;

	@Override
	public void tick(AbstractClientPlayerEntity player) {
		this.shouldPlay = player.isInsideWaterOrBubbleColumn() && !player.isInSwimmingPose();
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(
				new Identifier(PlayerAnimationRework.MOD_ID, SWIM_IDLE_ANIMATION_NAME)
		);

		return new AnimationData(animation, 1.0f, 5);
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
