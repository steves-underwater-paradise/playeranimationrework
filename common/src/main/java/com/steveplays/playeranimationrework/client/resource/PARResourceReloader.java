package com.steveplays.playeranimationrework.client.resource;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.steveplays.playeranimationrework.PlayerAnimationRework;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition.AnimationPriorityDefinition;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition.AnimationTriggerDefinition.Type;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
import com.steveplays.playeranimationrework.client.registry.PARAnimationRegistry;
import com.steveplays.playeranimationrework.client.registry.PAREventRegistry;
import com.steveplays.playeranimationrework.client.util.PlayerAnimationUtil;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import static com.steveplays.playeranimationrework.PlayerAnimationRework.TICKS_PER_SECOND;
import static com.steveplays.playeranimationrework.client.util.PlayerAnimationUtil.START_SUFFIX;
import static com.steveplays.playeranimationrework.client.util.PlayerAnimationUtil.STOP_SUFFIX;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class PARResourceReloader extends SinglePreparationResourceReloader<Void> {
	private static final @NotNull Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	private static final @NotNull String JSON_FILE_SUFFIX = ".json";
	private static final @NotNull String ANIMATION_DEFINITIONS_FOLDER_NAME = "par_animations";

	/**
	 * The preparation stage, ran on worker threads.
	 */
	@Override
	protected @Nullable Void prepare(ResourceManager resourceManager, Profiler profiler) {
		loadAnimations(resourceManager);
		return null;
	}

	/**
	 * The apply stage, ran on the main thread.
	 */
	@Override
	protected void apply(@Nullable Void prepared, ResourceManager resourceManager, Profiler profiler) {
		registerPlayerAnimationLayers();
	}

	private void loadAnimations(@NotNull ResourceManager resourceManager) {
		PARAnimationRegistry.ANIMATION_REGISTRY.clear();

		@NotNull var jsonAnimations = resourceManager.findResources(ANIMATION_DEFINITIONS_FOLDER_NAME, identifier -> identifier.toString().endsWith(JSON_FILE_SUFFIX));
		for (@NotNull var jsonAnimation : jsonAnimations.entrySet()) {
			@NotNull var jsonAnimationPath = jsonAnimation.getKey();
			@NotNull var jsonAnimationPathSplit = jsonAnimationPath.getPath().replace(JSON_FILE_SUFFIX, "").split("/");
			@NotNull var animationIdentifier = Identifier.of(jsonAnimationPath.getNamespace(), jsonAnimationPathSplit[jsonAnimationPathSplit.length - 1]);

			if (PARAnimationRegistry.ANIMATION_REGISTRY.containsKey(animationIdentifier)) {
				continue;
			}

			try {
				@NotNull var animationDefinitionOrError =
						AnimationDefinition.CODEC.parse(JsonOps.INSTANCE, GSON.fromJson(new String(jsonAnimation.getValue().getInputStream().readAllBytes()), JsonElement.class));
				animationDefinitionOrError.ifSuccess(animationDefinition -> PARAnimationRegistry.ANIMATION_REGISTRY.put(animationIdentifier, animationDefinition));
				animationDefinitionOrError.ifError(partialAnimationDefinition -> PlayerAnimationRework.LOGGER.error("Exception thrown while deserializing an animation definition (identifier: {}): {}",
						animationIdentifier, partialAnimationDefinition.message()));
			} catch (IOException e) {
				PlayerAnimationRework.LOGGER.error("Exception thrown while deserializing an animation definition (identifier: {}): {}", animationIdentifier, e);
			}
		}
	}

	private static void registerPlayerAnimationLayers() {
		if (PARAnimationRegistry.ANIMATION_REGISTRY.isEmpty()) {
			return;
		}

		for (var animation : PARAnimationRegistry.ANIMATION_REGISTRY.entrySet()) {
			@NotNull var animationTriggerIdentifier = animation.getKey();
			@NotNull var animationDefinition = animation.getValue();
			@NotNull var animationPriorityDefinition = animationDefinition.getAnimationPriorityDefinition();
			PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(animationTriggerIdentifier, Integer.MAX_VALUE - animationPriorityDefinition.getLayer(), clientPlayer -> {
				return new ModifierLayer<>();
			});

			@NotNull var animationTriggerDefinition = animationDefinition.getAnimationTriggerDefinition();
			@NotNull var animationTriggerType = animationTriggerDefinition.getConvertedType();
			@NotNull var animationIdentifier = animationDefinition.getIdentifier();
			@NotNull var interpolationDefinition = animationDefinition.getAnimationInterpolationDefinition();
			@NotNull var interpolationEaseType = interpolationDefinition.getConvertedType();
			@NotNull var interpolationLengthInSeconds = Math.round(interpolationDefinition.getLengthIn() * TICKS_PER_SECOND);
			@NotNull var interpolationLengthOutSeconds = Math.round(interpolationDefinition.getLengthOut() * TICKS_PER_SECOND);
			if (animationTriggerType == Type.WHEN) {
				for (var triggerIdentifier : animationTriggerDefinition.getIdentifiers()) {
					PAREventRegistry.EVENT_REGISTRY.get(triggerIdentifier).register(clientPlayer -> {
						if (!PlayerAnimationUtil.shouldAnimatePlayer(clientPlayer)) {
							return;
						}

						@SuppressWarnings("unchecked") @Nullable var playerAnimationLayer =
								(ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(animationTriggerIdentifier);
						if (playerAnimationLayer == null) {
							return;
						}

						@NotNull var keyframeAnimationPlayer = new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(animationIdentifier), 0, true);
						keyframeAnimationPlayer = disableAnimationBodyPartsBasedOnPriority(animationTriggerIdentifier, animationPriorityDefinition, keyframeAnimationPlayer);

						playerAnimationLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(interpolationLengthInSeconds, interpolationEaseType), keyframeAnimationPlayer, true);
					});
				}
			} else if (animationTriggerType == Type.WHILE) {
				for (var triggerIdentifier : animationTriggerDefinition.getIdentifiers()) {
					PAREventRegistry.EVENT_REGISTRY.get(triggerIdentifier.withSuffixedPath(START_SUFFIX)).register(clientPlayer -> {
						if (!PlayerAnimationUtil.shouldAnimatePlayer(clientPlayer)) {
							return;
						}

						@SuppressWarnings("unchecked") @Nullable var playerAnimationLayer =
								(ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(animationTriggerIdentifier);
						if (playerAnimationLayer == null || playerAnimationLayer.isActive()) {
							return;
						}

						@NotNull var keyframeAnimationPlayer = new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(animationIdentifier), 0, true);
						keyframeAnimationPlayer = disableAnimationBodyPartsBasedOnPriority(animationTriggerIdentifier, animationPriorityDefinition, keyframeAnimationPlayer);

						playerAnimationLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(interpolationLengthInSeconds, interpolationEaseType), keyframeAnimationPlayer, true);
					});
					PAREventRegistry.EVENT_REGISTRY.get(triggerIdentifier.withSuffixedPath(STOP_SUFFIX)).register(clientPlayer -> {
						if (!PlayerAnimationUtil.shouldAnimatePlayer(clientPlayer)) {
							return;
						}

						@SuppressWarnings("unchecked") @Nullable var playerAnimationLayer =
								(ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(animationTriggerIdentifier);
						if (playerAnimationLayer == null || !playerAnimationLayer.isActive()) {
							return;
						}

						playerAnimationLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(interpolationLengthOutSeconds, interpolationEaseType), null, true);
					});
				}
			} else if (animationTriggerType == Type.AFTER) {
				for (var triggerIdentifier : animationTriggerDefinition.getIdentifiers()) {
					PARPlayerEvents.AFTER_ANIMATION.register((clientPlayer, previousAnimation) -> {
						if (!PlayerAnimationUtil.shouldAnimatePlayer(clientPlayer)) {
							return;
						}

						@SuppressWarnings("unchecked") @Nullable var playerAnimationLayer =
								(ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(animationTriggerIdentifier);
						if (playerAnimationLayer == null || !previousAnimation.getIdentifier().equals(triggerIdentifier)) {
							return;
						}

						@NotNull var keyframeAnimationPlayer = new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(animationIdentifier), 0, true);
						keyframeAnimationPlayer = disableAnimationBodyPartsBasedOnPriority(animationTriggerIdentifier, animationPriorityDefinition, keyframeAnimationPlayer);

						playerAnimationLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(interpolationLengthInSeconds, interpolationEaseType), keyframeAnimationPlayer, true);
					});
				}
			}
		}
	}

	private static @NotNull KeyframeAnimationPlayer disableAnimationBodyPartsBasedOnPriority(@NotNull Identifier animationTriggerIdentifier,
			@NotNull AnimationPriorityDefinition animationPriorityDefinition, @NotNull KeyframeAnimationPlayer keyframeAnimationPlayer) {
		for (var otherAnimation : PARAnimationRegistry.ANIMATION_REGISTRY.entrySet()) {
			if (otherAnimation == null || otherAnimation.getKey() == animationTriggerIdentifier) {
				continue;
			}

			@NotNull var otherAnimationPriorityDefinition = otherAnimation.getValue().getAnimationPriorityDefinition();
			if (otherAnimationPriorityDefinition.getHead() < animationPriorityDefinition.getHead()) {
				keyframeAnimationPlayer.getPart("head").part.setEnabled(false);
			}
			if (otherAnimationPriorityDefinition.getTorso() < animationPriorityDefinition.getTorso()) {
				keyframeAnimationPlayer.getPart("torso").part.setEnabled(false);
			}
			if (otherAnimationPriorityDefinition.getArms().getRight() < animationPriorityDefinition.getArms().getRight()) {
				keyframeAnimationPlayer.getPart("rightArm").part.setEnabled(false);
			}
			if (otherAnimationPriorityDefinition.getArms().getLeft() < animationPriorityDefinition.getArms().getLeft()) {
				keyframeAnimationPlayer.getPart("leftArm").part.setEnabled(false);
			}
			if (otherAnimationPriorityDefinition.getLegs().getRight() < animationPriorityDefinition.getLegs().getRight()) {
				keyframeAnimationPlayer.getPart("rightLeg").part.setEnabled(false);
			}
			if (otherAnimationPriorityDefinition.getLegs().getLeft() < animationPriorityDefinition.getLegs().getLeft()) {
				keyframeAnimationPlayer.getPart("leftLeg").part.setEnabled(false);
			}
		}

		return keyframeAnimationPlayer;
	}
}
