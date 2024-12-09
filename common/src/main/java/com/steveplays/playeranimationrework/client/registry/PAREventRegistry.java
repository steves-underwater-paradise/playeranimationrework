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
			put(new Identifier(MOD_ID, "jump"), PARPlayerEvents.JUMP);
			put(new Identifier(MOD_ID, "idle_start"), PARPlayerEvents.IDLE_START);
			put(new Identifier(MOD_ID, "idle_stop"), PARPlayerEvents.IDLE_STOP);
		}
	};
}
