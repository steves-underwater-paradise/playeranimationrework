package com.github.steveplays28.playeranimationrework.client.animation.impl;

import com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient;
import com.github.steveplays28.playeranimationrework.client.animation.IAnimation;
import com.github.steveplays28.playeranimationrework.client.animation.state.PARState;
import com.github.steveplays28.playeranimationrework.client.event.animation.state.PARPlayerStateChangeEvents;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class IdleAnimation implements IAnimation {
	// TODO: Fix naming inconsistency
	private static final Identifier IDLE_STANDING_ANIMATION_IDENTIFIER = new Identifier(
			PlayerAnimationReworkClient.MOD_NAMESPACE, "idle");
	// TODO: Fix naming inconsistency
	private static final Identifier IDLE_SNEAKING_ANIMATION_IDENTIFIER = new Identifier(
			PlayerAnimationReworkClient.MOD_NAMESPACE, "sneak_idle");

	@Override
	public IAnimation register() {
		PARPlayerStateChangeEvents.PLAYER_STATE_CHANGED.register(this::onPlayerStateChanged);
		PARPlayerStateChangeEvents.PLAYER_SNEAK.register(this::onPlayerSneak);
		PARPlayerStateChangeEvents.PLAYER_UNSNEAK.register(this::onPlayerUnsneak);
		return this;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void start(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		playerAnimationModifierLayer.replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(60, Ease.OUTSINE),
				new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(IDLE_STANDING_ANIMATION_IDENTIFIER)), true
		);
	}

	@Override
	public void stop(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		playerAnimationModifierLayer.replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(40, Ease.OUTSINE), null);
	}

	@Override
	public void continueWithSpecificBodyParts() {
		// TODO
	}

	private void onPlayerStateChanged(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState) {
		if (previousState.isWalking() && !newState.isWalking()) {
			start(playerAnimationModifierLayer);
		}
	}

	private void onPlayerSneak(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState) {
		if (!newState.isWalking()) {
			startSneakingIdleAnimation(playerAnimationModifierLayer);
		}
	}

	private void onPlayerUnsneak(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState) {
		if (!newState.isWalking()) {
			start(playerAnimationModifierLayer);
		}
	}

	private void startSneakingIdleAnimation(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		playerAnimationModifierLayer.replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(60, Ease.OUTSINE),
				new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(IDLE_SNEAKING_ANIMATION_IDENTIFIER)), true
		);
	}
}
