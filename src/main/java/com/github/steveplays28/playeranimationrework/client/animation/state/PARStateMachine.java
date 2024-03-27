package com.github.steveplays28.playeranimationrework.client.animation.state;

import com.github.steveplays28.playeranimationrework.client.event.animation.state.PARPlayerStateChangeCallback;
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
		PARPlayerStateChangeCallback.EVENT.invoker().onStateChanged(player, previousState, newState);
	}
}
