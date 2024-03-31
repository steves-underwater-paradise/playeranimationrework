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

public class WalkAnimation implements IAnimation {
	private static final String WALKING_ANIMATION_NAME = "walking";

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
		playerAnimationModifierLayer.replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(40, Ease.OUTSINE),
				new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(
						new Identifier(PlayerAnimationReworkClient.MOD_NAMESPACE, WALKING_ANIMATION_NAME))),
				true
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
		if (previousState.isWalking() && newState.isWalking()) {
			return;
		}

		if (!previousState.isWalking() && newState.isWalking()) {
			start(playerAnimationModifierLayer);
		}
	}
}
