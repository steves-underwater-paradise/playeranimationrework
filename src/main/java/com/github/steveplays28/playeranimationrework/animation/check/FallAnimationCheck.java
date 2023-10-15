package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.PlayerAnimationRework;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class FallAnimationCheck implements AnimationCheck {
	private static final String LONG_FALL_ANIMATION_NAME = "falling";
	private static final String[] SHORT_FALL_ANIMATION_NAMES = new String[]{"fall_first", "fall_second"};
	private static final String LANDING_ANIMATION_NAME = "landing";

	private boolean isFallIndexOdd = false;
	private boolean shouldPlay = false;
	private float lastFallDistance;
	private String selectedAnimationName;

	@Override
	public void fall(AbstractClientPlayerEntity player, double heightDifference, boolean onGround,
	                 BlockState state, BlockPos landedPosition) {
		if (onGround) {
			this.isFallIndexOdd = !this.isFallIndexOdd;
		}

		if (lastFallDistance > 3 && onGround) {
			this.selectedAnimationName = LANDING_ANIMATION_NAME;
			this.shouldPlay = true;
		} else if (player.fallDistance > 3) {
			this.selectedAnimationName = LONG_FALL_ANIMATION_NAME;
			this.shouldPlay = true;
		} else if (!onGround) {
			this.selectedAnimationName = SHORT_FALL_ANIMATION_NAMES[isFallIndexOdd ? 1 : 0];
			this.shouldPlay = true;
		}

		this.lastFallDistance = player.fallDistance;
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(
				new Identifier(PlayerAnimationRework.MOD_ID, selectedAnimationName)
		);

		return new AnimationData(animation, 1.0f, 5);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.FALL;
	}

	@Override
	public boolean getShouldPlay() {
		return this.shouldPlay;
	}

	@Override
	public void cleanup() {
		this.selectedAnimationName = null;
		this.shouldPlay = false;
	}
}
