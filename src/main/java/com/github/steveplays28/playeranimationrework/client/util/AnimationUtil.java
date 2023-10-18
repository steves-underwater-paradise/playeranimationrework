package com.github.steveplays28.playeranimationrework.client.util;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.MOD_NAMESPACE;

@Environment(EnvType.CLIENT)
public class AnimationUtil {
	public static @NotNull Identifier getAnimationIdentifier(String animationName) {
		return new Identifier(String.format("%s", MOD_NAMESPACE), animationName);
	}

	public static @NotNull KeyframeAnimation getAnimation(String animationName) {
		var keyframeAnimation = PlayerAnimationRegistry.getAnimation(getAnimationIdentifier(animationName));

		if (keyframeAnimation == null) {
			throw new IllegalStateException(String.format("Animation %s is null.", animationName));
		}

		return keyframeAnimation;
	}

	public static @NotNull List<Class<? extends Item>> getItemsWithThirdPersonArmAnimations() {
		return List.of(CrossbowItem.class, BowItem.class);
	}

	public static @NotNull List<Class<? extends Item>> getItemsWithThirdPersonRightArmAnimations() {
		return List.of(ShieldItem.class);
	}
}
