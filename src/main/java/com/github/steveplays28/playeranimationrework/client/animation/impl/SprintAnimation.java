package com.github.steveplays28.playeranimationrework.client.animation.impl;

import com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient;
import com.github.steveplays28.playeranimationrework.client.animation.IAnimation;
import com.github.steveplays28.playeranimationrework.client.animation.ModelPart;
import com.github.steveplays28.playeranimationrework.client.animation.state.PARState;
import com.github.steveplays28.playeranimationrework.client.event.animation.state.PARPlayerStateChangeEvents;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SprintAnimation implements IAnimation {
	// TODO: Fix inconsistent naming
	private static final Identifier SPRINTING_ANIMATION_IDENTIFIER = new Identifier(PlayerAnimationReworkClient.MOD_NAMESPACE, "running");

	private KeyframeAnimationPlayer animationPlayer;

	@Override
	public IAnimation register() {
		PARPlayerStateChangeEvents.PLAYER_STATE_CHANGED.register(this::onPlayerStateChanged);
		return this;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void start(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		animationPlayer = new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(SPRINTING_ANIMATION_IDENTIFIER));
		playerAnimationModifierLayer.replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(40, Ease.OUTSINE), animationPlayer, true
		);
	}

	@Override
	public void stop(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		playerAnimationModifierLayer.replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(10, Ease.OUTSINE), null);
	}

	@Override
	public void continueWithSpecificBodyPartsEnabled(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull KeyframeAnimation animation, @NotNull List<ModelPart> unanimatedBodyParts) {
		// TODO
	}

	@Override
	public void continueWithSpecificBodyPartsDisabled(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull KeyframeAnimation animation, @NotNull List<ModelPart> unanimatedBodyParts) {
		// TODO
	}

	private void onPlayerStateChanged(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState) {
		if (previousState.isSprinting() && newState.isSprinting()) {
			return;
		}

		if (!previousState.isSprinting() && newState.isSprinting()) {
			start(playerAnimationModifierLayer);
		}
	}
}
