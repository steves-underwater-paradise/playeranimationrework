package com.steveplays.playeranimationrework.client.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition;
import net.minecraft.util.Identifier;

public class PARAnimationRegistry {
	public static final Map<Identifier, AnimationDefinition> ANIMATION_REGISTRY = new ConcurrentHashMap<>();
}
