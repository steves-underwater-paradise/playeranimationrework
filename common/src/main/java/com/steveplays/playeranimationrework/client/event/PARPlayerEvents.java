package com.steveplays.playeranimationrework.client.event;

import org.jetbrains.annotations.NotNull;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;

public interface PARPlayerEvents {
	Event<AfterAnimationEvent> AFTER_ANIMATION = EventFactory.createLoop();
	Event<ClientPlayerEvent> JUMP = EventFactory.createLoop();
	Event<ClientPlayerEvent> IDLE_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> IDLE_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> WALK_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> WALK_STOP = EventFactory.createLoop();
	Event<ClientPlayerEvent> RUN_START = EventFactory.createLoop();
	Event<ClientPlayerEvent> RUN_STOP = EventFactory.createLoop();

	@FunctionalInterface
	interface AfterAnimationEvent {
		void onExecute(@NotNull AbstractClientPlayerEntity clientPlayer, @NotNull AnimationDefinition previousAnimation);
	}

	@FunctionalInterface
	interface ClientPlayerEvent {
		void onExecute(@NotNull AbstractClientPlayerEntity clientPlayer);
	}
}
