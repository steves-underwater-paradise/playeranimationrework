package com.github.steveplays28.playeranimationrework.animation;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.check.AnimationCheck;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AnimationCheckRegistry {
	private final List<AnimationCheck> checks = new ArrayList<>();
	private KeyframeAnimation lastKeyframeAnimation;
	private KeyframeAnimation currentKeyframeAnimation;

	public void registerAnimationCheck(AnimationCheck check) {
		checks.add(check);
	}

	public void invokeTick(AbstractClientPlayerEntity player) {
		for (AnimationCheck check : checks) {
			check.tick(player);
		}
	}

	public void invokeJump(AbstractClientPlayerEntity player) {
		for (AnimationCheck check : checks) {
			check.jump(player);
		}
	}

	public void invokeSwingHand(AbstractClientPlayerEntity player, Hand hand) {
		for (AnimationCheck check : checks) {
			check.swingHand(player, hand);
		}
	}

	public void invokeFall(AbstractClientPlayerEntity player, double heightDifference, boolean onGround,
	                       BlockState state, BlockPos landedPosition) {
		for (AnimationCheck check : checks) {
			check.fall(player, heightDifference, onGround, state, landedPosition);
		}
	}

	public void invokeCleanup() {
		for (AnimationCheck check : checks) {
			check.cleanup();
		}
	}

	public boolean animationSameAsPreviousOne() {
		return this.currentKeyframeAnimation == this.lastKeyframeAnimation;
	}

	@Nullable
	public AnimationCheck getMostSuitableAnimation() {
		AnimationCheck mostSuitableAnimation = null;

		for (AnimationCheck check : checks) {
			if (!check.getShouldPlay()) {
				continue;
			}

			if (mostSuitableAnimation == null) {
				mostSuitableAnimation = check;
			} else if (check.getShouldPlay() && check.getPriority().getValue() >
					mostSuitableAnimation.getPriority().getValue()) {
				mostSuitableAnimation = check;
			}
		}

		if (mostSuitableAnimation == null) {
			return null;
		}

		this.lastKeyframeAnimation = this.currentKeyframeAnimation;
		this.currentKeyframeAnimation = mostSuitableAnimation.getAnimationData().getKeyframeAnimation();

		if (this.animationSameAsPreviousOne()) {
			return null;
		} else {
			return mostSuitableAnimation;
		}
	}
}
