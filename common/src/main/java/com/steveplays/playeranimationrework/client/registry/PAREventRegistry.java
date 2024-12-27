package com.steveplays.playeranimationrework.client.registry;

import static com.steveplays.playeranimationrework.PlayerAnimationRework.MOD_ID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
import dev.architectury.event.Event;
import net.minecraft.util.Identifier;

public class PAREventRegistry {
	public static final Map<Identifier, Event<PARPlayerEvents.ClientPlayerEvent>> EVENT_REGISTRY = new ConcurrentHashMap<>() {
		{
			// when type animations
			put(Identifier.of(MOD_ID, "jump"), PARPlayerEvents.JUMP);
			put(Identifier.of(MOD_ID, "punch"), PARPlayerEvents.PUNCH);

			// while type animations
			put(Identifier.of(MOD_ID, "idle_start"), PARPlayerEvents.IDLE_START);
			put(Identifier.of(MOD_ID, "idle_stop"), PARPlayerEvents.IDLE_STOP);
			put(Identifier.of(MOD_ID, "walk_start"), PARPlayerEvents.WALK_START);
			put(Identifier.of(MOD_ID, "walk_stop"), PARPlayerEvents.WALK_STOP);
			put(Identifier.of(MOD_ID, "run_start"), PARPlayerEvents.RUN_START);
			put(Identifier.of(MOD_ID, "run_stop"), PARPlayerEvents.RUN_STOP);
		}
	};
}
