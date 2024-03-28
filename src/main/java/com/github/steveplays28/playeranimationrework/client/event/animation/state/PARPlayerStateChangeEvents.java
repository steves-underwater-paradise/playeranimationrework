package com.github.steveplays28.playeranimationrework.client.event.animation.state;

import com.github.steveplays28.playeranimationrework.client.animation.state.PARState;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.NotNull;

public final class PARPlayerStateChangeEvents {
	public static final Event<PlayerStateChange> PLAYER_STATE_CHANGED = EventFactory.createArrayBacked(
			PlayerStateChange.class,
			(listeners) -> (playerAnimationModifierLayer, previousState, newState) -> {
				for (PlayerStateChange listener : listeners) {
					listener.onPlayerStateChanged(playerAnimationModifierLayer, previousState, newState);
				}
			}
	);

	public static final Event<PlayerSneakStateChange> PLAYER_SNEAK_STATE_CHANGED = EventFactory.createArrayBacked(
			PlayerSneakStateChange.class,
			(listeners) -> (playerAnimationModifierLayer, previousState, newState) -> {
				for (PlayerSneakStateChange listener : listeners) {
					listener.onPlayerSneakStateChanged(playerAnimationModifierLayer, previousState, newState);
				}
			}
	);

	public static final Event<PlayerSneak> PLAYER_SNEAK = EventFactory.createArrayBacked(
			PlayerSneak.class,
			(listeners) -> (playerAnimationModifierLayer, previousState, newState) -> {
				for (PlayerSneak listener : listeners) {
					listener.onPlayerSneak(playerAnimationModifierLayer, previousState, newState);
				}
			}
	);

	public static final Event<PlayerUnsneak> PLAYER_UNSNEAK = EventFactory.createArrayBacked(
			PlayerUnsneak.class,
			(listeners) -> (playerAnimationModifierLayer, previousState, newState) -> {
				for (PlayerUnsneak listener : listeners) {
					listener.onPlayerUnsneak(playerAnimationModifierLayer, previousState, newState);
				}
			}
	);

	@FunctionalInterface
	public interface PlayerStateChange {
		void onPlayerStateChanged(ModifierLayer<IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState);
	}

	@FunctionalInterface
	public interface PlayerSneakStateChange {
		void onPlayerSneakStateChanged(ModifierLayer<IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState);
	}

	@FunctionalInterface
	public interface PlayerSneak {
		void onPlayerSneak(ModifierLayer<IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState);
	}

	@FunctionalInterface
	public interface PlayerUnsneak {
		void onPlayerUnsneak(ModifierLayer<IAnimation> playerAnimationModifierLayer, @NotNull PARState previousState, @NotNull PARState newState);
	}
}
