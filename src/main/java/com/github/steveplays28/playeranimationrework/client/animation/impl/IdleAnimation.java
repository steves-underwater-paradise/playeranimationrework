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
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IdleAnimation implements IAnimation {
	// TODO: Fix naming inconsistency
	private static final Identifier IDLE_STANDING_ANIMATION_IDENTIFIER = new Identifier(
			PlayerAnimationReworkClient.MOD_NAMESPACE, "idle");
	// TODO: Fix naming inconsistency
	private static final Identifier IDLE_SNEAKING_ANIMATION_IDENTIFIER = new Identifier(
			PlayerAnimationReworkClient.MOD_NAMESPACE, "sneak_idle");

	private KeyframeAnimationPlayer animationPlayer;

	@Override
	public IAnimation register() {
		ServerLifecycleEvents.SERVER_STARTING.register(client -> animationPlayer = new KeyframeAnimationPlayer(
				PlayerAnimationRegistry.getAnimation(IDLE_STANDING_ANIMATION_IDENTIFIER)));
		PARPlayerStateChangeEvents.PLAYER_STATE_CHANGED.register(this::onPlayerStateChanged);
		PARPlayerStateChangeEvents.PLAYER_SNEAK.register(this::onPlayerSneak);
		PARPlayerStateChangeEvents.PLAYER_UNSNEAK.register(this::onPlayerUnsneak);
		if (PlayerAnimationReworkClient.IS_NOT_ENOUGH_ANIMATIONS_LOADED) {
			PARPlayerStateChangeEvents.PLAYER_EQUIP_MAIN_HAND_ITEM_STACK.register(this::onPlayerEquippedMainHandItemStack);
			PARPlayerStateChangeEvents.PLAYER_UNEQUIP_MAIN_HAND_ITEM_STACK.register(this::onPlayerUnequippedMainHandItemStack);
		}
		return this;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void start(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		// NO-OP
	}

	@Override
	public void stop(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		playerAnimationModifierLayer.replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(40, Ease.OUTSINE), null);
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	@Override
	public void continueWithSpecificBodyPartsEnabled(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull KeyframeAnimation animation, @NotNull List<ModelPart> animatedBodyParts) {
		var animationMutableCopy = animation.mutableCopy();
		for (int i = 0; i < animatedBodyParts.size(); i++) {
			var animatedBodyPart = animatedBodyParts.get(i);
			var animatedBodyPartStateCollection = animationMutableCopy.getPart(animatedBodyPart.getName());
			if (animatedBodyPartStateCollection == null) {
				throw new IllegalArgumentException(
						String.format("Tried enabling animations for body part %s, which does not exist!", animatedBodyPart.getName()));
			}

			animatedBodyPartStateCollection.setEnabled(true);
		}

		animationPlayer = new KeyframeAnimationPlayer(animationMutableCopy.build(), animationPlayer.getCurrentTick());
		playerAnimationModifierLayer.setAnimation(animationPlayer);
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	@Override
	public void continueWithSpecificBodyPartsDisabled(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull KeyframeAnimation animation, @NotNull List<ModelPart> unanimatedBodyParts) {
		var animationMutableCopy = animation.mutableCopy();
		for (int i = 0; i < unanimatedBodyParts.size(); i++) {
			var unanimatedBodyPart = unanimatedBodyParts.get(i);
			var unanimatedBodyPartStateCollection = animationMutableCopy.getPart(unanimatedBodyPart.getName());
			if (unanimatedBodyPartStateCollection == null) {
				throw new IllegalArgumentException(
						String.format("Tried disabling animations for body part %s, which does not exist!", unanimatedBodyPart.getName()));
			}

			unanimatedBodyPartStateCollection.setEnabled(false);
		}

		animationPlayer = new KeyframeAnimationPlayer(animationMutableCopy.build(), animationPlayer.getCurrentTick());
		playerAnimationModifierLayer.setAnimation(animationPlayer);
	}

	private void onPlayerStateChanged(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState) {
		if (previousState.isWalking() && !newState.isWalking()) {
			startStandingIdleAnimation(playerAnimationModifierLayer, 10);
		}
	}

	private void onPlayerSneak(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState) {
		if (!newState.isWalking()) {
			startSneakingIdleAnimation(playerAnimationModifierLayer);
		}
	}

	private void onPlayerUnsneak(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState) {
		if (!newState.isWalking()) {
			startStandingIdleAnimation(playerAnimationModifierLayer, 60);
		}
	}

	private void onPlayerEquippedMainHandItemStack(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState) {
		// TODO: Add support for dynamically resolving the main hand in ModelPart
		if (newState.getEquippedMainHandItemStack().isOf(Items.TORCH)) {
			continueWithSpecificBodyPartsDisabled(playerAnimationModifierLayer, animationPlayer.getData(), List.of(ModelPart.RIGHT_ARM));
		}
	}

	private void onPlayerUnequippedMainHandItemStack(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState) {
		// TODO: Add support for dynamically resolving the main hand in ModelPart
		continueWithSpecificBodyPartsEnabled(playerAnimationModifierLayer, animationPlayer.getData(), List.of(ModelPart.RIGHT_ARM));
	}

	private void startStandingIdleAnimation(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, int fadeLength) {
		animationPlayer = new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(IDLE_STANDING_ANIMATION_IDENTIFIER));
		playerAnimationModifierLayer.replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(fadeLength, Ease.OUTSINE), animationPlayer, true
		);
	}

	private void startSneakingIdleAnimation(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		animationPlayer = new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(IDLE_SNEAKING_ANIMATION_IDENTIFIER));
		playerAnimationModifierLayer.replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(60, Ease.OUTSINE), animationPlayer, true
		);
	}
}
