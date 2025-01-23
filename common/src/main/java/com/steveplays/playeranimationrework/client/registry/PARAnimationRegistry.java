package com.steveplays.playeranimationrework.client.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition;
import net.minecraft.util.Identifier;

public class PARAnimationRegistry {
	public static final @NotNull Map<Identifier, AnimationDefinition> ANIMATION_REGISTRY = new ConcurrentHashMap<>();
	public static final @NotNull Map<String, Boolean> ENABLED_BODY_PARTS = new ConcurrentHashMap<>() {
		{
			put("body", true);
			put("head", true);
			put("torso", true);
			put("rightArm", true);
			put("leftArm", true);
			put("rightLeg", true);
			put("leftLeg", true);
		}
	};
}
