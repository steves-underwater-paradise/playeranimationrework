package com.github.steveplays28.playeranimationrework.client.animation.impl;

import com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient;
import com.github.steveplays28.playeranimationrework.client.animation.IAnimation;
import com.github.steveplays28.playeranimationrework.client.animation.state.PARState;
import com.github.steveplays28.playeranimationrework.client.event.animation.state.PARPlayerStateChangeCallback;
import com.github.steveplays28.playeranimationrework.client.extension.PlayerEntityExtension;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class WalkAnimation implements IAnimation {
	private static final String IDLE_ANIMATION_NAME = "idle";
	private static final String WALK_ANIMATION_NAME = "walking";

	@Override
	public IAnimation register() {
		PARPlayerStateChangeCallback.EVENT.register(this::onStateChanged);
		return this;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void start(PlayerEntity player) {
		((PlayerEntityExtension) player).playerAnimationRework$getModifierLayer().replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(40, Ease.OUTSINE),
				new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(
						new Identifier(PlayerAnimationReworkClient.MOD_NAMESPACE, WALK_ANIMATION_NAME)))
		);
	}

	@Override
	public void stop(PlayerEntity player) {
		((PlayerEntityExtension) player).playerAnimationRework$getModifierLayer().replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(20, Ease.OUTSINE), null);
	}

	@Override
	public void continueWithSpecificBodyParts() {
//		PlayerAnimationRegistry.getAnimation(new Identifier(PlayerAnimationReworkClient.MOD_NAMESPACE, IDLE_ANIMATION_NAME)).;
	}

	private void onStateChanged(PlayerEntity player, @NotNull PARState previousState, @NotNull PARState newState) {
		if (previousState.isWalking() && newState.isWalking()) {
			return;
		}

		if (!previousState.isWalking() && newState.isWalking()) {
			start(player);
			return;
		}

		stop(player);
	}
}
