package com.github.steveplays28.playeranimationrework.util;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static com.github.steveplays28.playeranimationrework.PlayerAnimationRework.MOD_NAMESPACE;

public class AnimationUtil {
	public static @NotNull Identifier getAnimationIdentifier(String animationName) {
		return new Identifier(String.format("%s", MOD_NAMESPACE), animationName);
	}

	public static @NotNull KeyframeAnimation getAnimation(String animationName) {
		var keyframeAnimation = PlayerAnimationRegistry.getAnimation(getAnimationIdentifier(animationName));

		if (keyframeAnimation == null) {
			throw new IllegalStateException("Animation is null.");
		}

		return keyframeAnimation;
	}
}
