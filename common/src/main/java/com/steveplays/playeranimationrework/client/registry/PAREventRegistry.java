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
			put(Identifier.of(MOD_ID, "switch_to_item_on_back_right_arm"), PARPlayerEvents.SWITCH_TO_ITEM_ON_BACK_RIGHT_ARM);

			// while type animations
			put(Identifier.of(MOD_ID, "idle_start"), PARPlayerEvents.IDLE_START);
			put(Identifier.of(MOD_ID, "idle_stop"), PARPlayerEvents.IDLE_STOP);
			put(Identifier.of(MOD_ID, "walk_start"), PARPlayerEvents.WALK_START);
			put(Identifier.of(MOD_ID, "walk_stop"), PARPlayerEvents.WALK_STOP);
			put(Identifier.of(MOD_ID, "run_start"), PARPlayerEvents.RUN_START);
			put(Identifier.of(MOD_ID, "run_stop"), PARPlayerEvents.RUN_STOP);
			put(Identifier.of(MOD_ID, "fence_idle_start"), PARPlayerEvents.FENCE_IDLE_START);
			put(Identifier.of(MOD_ID, "fence_idle_stop"), PARPlayerEvents.FENCE_IDLE_STOP);
			put(Identifier.of(MOD_ID, "fence_walk_start"), PARPlayerEvents.FENCE_WALK_START);
			put(Identifier.of(MOD_ID, "fence_walk_stop"), PARPlayerEvents.FENCE_WALK_STOP);
			put(Identifier.of(MOD_ID, "fence_run_start"), PARPlayerEvents.FENCE_RUN_START);
			put(Identifier.of(MOD_ID, "fence_run_stop"), PARPlayerEvents.FENCE_RUN_STOP);
		}
	};
}
