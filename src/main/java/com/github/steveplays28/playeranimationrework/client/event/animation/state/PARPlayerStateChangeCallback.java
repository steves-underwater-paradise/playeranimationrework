package com.github.steveplays28.playeranimationrework.client.event.animation.state;

import com.github.steveplays28.playeranimationrework.client.animation.state.PARState;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public interface PARPlayerStateChangeCallback {
	Event<PARPlayerStateChangeCallback> EVENT = EventFactory.createArrayBacked(PARPlayerStateChangeCallback.class,
			(listeners) -> (player, previousState, newState) -> {
				for (PARPlayerStateChangeCallback listener : listeners) {
					listener.onStateChanged(player, previousState, newState);
				}
			});

	void onStateChanged(PlayerEntity player, @NotNull PARState previousState, @NotNull PARState newState);
}
