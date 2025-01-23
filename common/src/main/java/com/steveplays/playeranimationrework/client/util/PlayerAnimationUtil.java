package com.steveplays.playeranimationrework.client.util;

import static com.steveplays.playeranimationrework.PlayerAnimationRework.TICKS_PER_SECOND;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.steveplays.playeranimationrework.client.registry.PARAnimationRegistry;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;

@Environment(EnvType.CLIENT)
public class PlayerAnimationUtil {
	public static final @NotNull String START_SUFFIX = "_start";
	public static final @NotNull String STOP_SUFFIX = "_stop";

	public static boolean shouldAnimatePlayer(@NotNull AbstractClientPlayerEntity clientPlayer) {
		return clientPlayer.isPartOfGame();
	}

	public static void stopAllAnimations(@NotNull AbstractClientPlayerEntity clientPlayer) {
		for (var animation : PARAnimationRegistry.ANIMATION_REGISTRY.entrySet()) {
			if (animation == null) {
				continue;
			}

			@NotNull var animationTriggerIdentifier = animation.getKey();
			@NotNull var animationDefinition = animation.getValue();
			@NotNull var interpolationDefinition = animationDefinition.getAnimationInterpolationDefinition();
			@NotNull var interpolationEaseType = interpolationDefinition.getConvertedType();
			@NotNull var interpolationLengthOutSeconds = Math.round(interpolationDefinition.getLengthOut() * TICKS_PER_SECOND);
			@SuppressWarnings("unchecked") @Nullable var playerAnimationLayer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(animationTriggerIdentifier);
			if (playerAnimationLayer == null) {
				continue;
			}

			playerAnimationLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(interpolationLengthOutSeconds, interpolationEaseType), null, true);
		}
	}

	public static void toggleBodyPartAnimations(@NotNull AbstractClientPlayerEntity clientPlayer, @NotNull String bodyPart, boolean enable) {
		if (PARAnimationRegistry.ENABLED_BODY_PARTS.get(bodyPart) == enable) {
			return;
		}

		for (var animation : PARAnimationRegistry.ANIMATION_REGISTRY.entrySet()) {
			if (animation == null) {
				continue;
			}

			@NotNull var animationTriggerIdentifier = animation.getKey();
			@SuppressWarnings("unchecked") @Nullable var playerAnimationLayer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(animationTriggerIdentifier);
			@Nullable var playerAnimation = playerAnimationLayer.getAnimation();
			if (playerAnimationLayer == null || !(playerAnimation instanceof KeyframeAnimationPlayer keyframeAnimationPlayer)) {
				continue;
			}

			keyframeAnimationPlayer.getPart(bodyPart).part.setEnabled(enable);
			PARAnimationRegistry.ENABLED_BODY_PARTS.put(bodyPart, enable);
		}
	}

	public static class BodyParts {
		public static final @NotNull String RIGHT_ARM = "rightArm";
		public static final @NotNull String LEFT_ARM = "leftArm";
	}
}
