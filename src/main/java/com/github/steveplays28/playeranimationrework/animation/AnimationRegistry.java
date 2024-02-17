package com.github.steveplays28.playeranimationrework.animation;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AnimationRegistry {
	private final List<Animation> animations = new ArrayList<>();

	private KeyframeAnimation lastKeyframeAnimation;
	private KeyframeAnimation currentKeyframeAnimation;

	public void registerAnimation(Animation animation) {
		animations.add(animation);
	}

	public void registerAnimations(Animation... animations) {
		this.animations.addAll(List.of(animations));
	}

	public void invokeTick(AbstractClientPlayerEntity player) {
		for (Animation animation : animations) {
			animation.tick(player);
		}
	}

	public void invokeJump(AbstractClientPlayerEntity player) {
		for (Animation animation : animations) {
			animation.jump(player);
		}
	}

	public void invokeSwingHand(AbstractClientPlayerEntity player, Hand hand) {
		for (Animation animation : animations) {
			animation.swingHand(player, hand);
		}
	}

	public void invokeFall(AbstractClientPlayerEntity player, double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
		for (Animation animation : animations) {
			animation.fall(player, heightDifference, onGround, state, landedPosition);
		}
	}

	public void invokeCleanup() {
		for (Animation animation : animations) {
			animation.cleanup();
		}
	}

	public boolean isAnimationEqualToPreviousAnimation() {
		return this.currentKeyframeAnimation == this.lastKeyframeAnimation;
	}

	@Nullable
	public Animation getMostSuitableAnimation() {
		Animation mostSuitableAnimation = null;

		for (Animation animation : animations) {
			if (!animation.getShouldPlay()) {
				continue;
			}

			if (mostSuitableAnimation == null) {
				mostSuitableAnimation = animation;
			} else if (animation.getShouldPlay() && animation.getPriority().getValue() > mostSuitableAnimation.getPriority().getValue()) {
				mostSuitableAnimation = animation;
			}
		}

		if (mostSuitableAnimation == null) {
			return null;
		}

		this.lastKeyframeAnimation = this.currentKeyframeAnimation;
		this.currentKeyframeAnimation = mostSuitableAnimation.getAnimationData().getKeyframeAnimation();

		if (this.isAnimationEqualToPreviousAnimation()) {
			return null;
		} else {
			return mostSuitableAnimation;
		}
	}
}
