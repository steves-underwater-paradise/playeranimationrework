package com.github.steveplays28.playeranimationrework.client.animation.state;

import com.github.steveplays28.playeranimationrework.client.event.animation.state.PARPlayerStateChangeEvents;
import com.github.steveplays28.playeranimationrework.client.extension.PlayerEntityExtension;
import net.minecraft.entity.player.PlayerEntity;

public class PARStateMachine {
	private PARState state;

	public PARStateMachine() {
		this.state = new PARStateBuilder().build();
	}

	public PARState getState() {
		return state;
	}

	public void setState(PlayerEntity player, PARState previousState, PARState newState) {
		this.state = newState;

		var playerAnimationModifierLayer = ((PlayerEntityExtension) player).playerAnimationRework$getModifierLayer();
		PARPlayerStateChangeEvents.PLAYER_STATE_CHANGED.invoker().onPlayerStateChanged(
				playerAnimationModifierLayer, previousState, newState);

		if (previousState.isSneaking() != newState.isSneaking()) {
			PARPlayerStateChangeEvents.PLAYER_SNEAK_STATE_CHANGED.invoker().onPlayerSneakStateChanged(
					playerAnimationModifierLayer, previousState, newState);

			if (!previousState.isSneaking() && newState.isSneaking()) {
				PARPlayerStateChangeEvents.PLAYER_SNEAK.invoker().onPlayerSneak(playerAnimationModifierLayer, previousState, newState);
			}
			if (previousState.isSneaking() && !newState.isSneaking()) {
				PARPlayerStateChangeEvents.PLAYER_UNSNEAK.invoker().onPlayerUnsneak(playerAnimationModifierLayer, previousState, newState);
			}
		}
	}
}
