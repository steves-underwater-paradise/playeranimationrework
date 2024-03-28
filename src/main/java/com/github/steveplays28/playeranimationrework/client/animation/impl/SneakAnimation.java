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

public class SneakAnimation implements IAnimation {
	private static final Identifier SNEAK_IDLE_ANIMATION_IDENTIFIER = new Identifier(
			PlayerAnimationReworkClient.MOD_NAMESPACE, "sneak_idle");

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
				AbstractFadeModifier.standardFadeIn(60, Ease.OUTSINE),
				new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(SNEAK_IDLE_ANIMATION_IDENTIFIER)), true
		);
	}

	@Override
	public void stop(PlayerEntity player) {
		((PlayerEntityExtension) player).playerAnimationRework$getModifierLayer().replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(40, Ease.OUTSINE), null);
	}

	@Override
	public void continueWithSpecificBodyParts() {
		// TODO
	}

	private void onStateChanged(PlayerEntity player, @NotNull PARState previousState, @NotNull PARState newState) {
		if (previousState.isSneaking() && newState.isSneaking()) {
			return;
		}

		if (!previousState.isSneaking() && newState.isSneaking()) {
			start(player);
		}
	}
}
