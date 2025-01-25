package com.steveplays.playeranimationrework.client.event;

import org.jetbrains.annotations.NotNull;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;

public interface PARPlayerEvents {
	Event<AfterAnimationEvent> AFTER_ANIMATION = EventFactory.createLoop();

	// when type animations
	Event<ClientPlayerEvent> JUMP = EventFactory.createLoop();
	Event<ClientPlayerEvent> LAND_SHORT_DISTANCE = EventFactory.createLoop();
	Event<ClientPlayerEvent> LAND_LONG_DISTANCE = EventFactory.createLoop();
	Event<ClientPlayerEvent> PUNCH = EventFactory.createLoop();
	Event<ClientPlayerEvent> USE_IGNITER = EventFactory.createLoop();
	Event<ClientPlayerEvent> SWITCH_TO_ITEM_ON_BACK_RIGHT_ARM = EventFactory.createLoop();
	Event<ClientPlayerEvent> SWITCH_TO_ITEM_IN_POCKET_RIGHT_ARM = EventFactory.createLoop();

	// while type animations
	Event<ClientPlayerEvent> IDLE_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> IDLE_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> WALK_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> WALK_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> RUN_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> RUN_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> SNEAK_IDLE_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> SNEAK_IDLE_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> SNEAK_WALK_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> SNEAK_WALK_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> SWIM_IDLE_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> SWIM_IDLE_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> SWIM_SLOW_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> SWIM_SLOW_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> SWIM_FAST_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> SWIM_FAST_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> FENCE_IDLE_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> FENCE_IDLE_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> FENCE_WALK_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> FENCE_WALK_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> FENCE_RUN_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> FENCE_RUN_STOP = EventFactory.createLoop();

	@FunctionalInterface
	interface AfterAnimationEvent {
		void onExecute(@NotNull AbstractClientPlayerEntity clientPlayer, @NotNull AnimationDefinition previousAnimation);
	}

	@FunctionalInterface
	interface ClientPlayerEvent {
		void onExecute(@NotNull AbstractClientPlayerEntity clientPlayer);
	}
}
