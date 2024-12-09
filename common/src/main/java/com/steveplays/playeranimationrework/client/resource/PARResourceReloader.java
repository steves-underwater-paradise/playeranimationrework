package com.steveplays.playeranimationrework.client.resource;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.steveplays.playeranimationrework.PlayerAnimationRework;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition;
import com.steveplays.playeranimationrework.client.registry.PARAnimationRegistry;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import static com.steveplays.playeranimationrework.PlayerAnimationRework.TICKS_PER_SECOND;
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
						AnimationDefinition.CODEC.parse(JsonOps.INSTANCE, GSON.fromJson(new String(jsonAnimation.getValue().getInputStream().readAllBytes()), JsonElement.class)).get();
				animationDefinitionOrError.ifLeft(animationDefinition -> PARAnimationRegistry.ANIMATION_REGISTRY.put(animationIdentifier, animationDefinition));
				animationDefinitionOrError.ifRight(partialAnimationDefinition -> PlayerAnimationRework.LOGGER.error("Exception thrown while deserializing an animation definition (identifier: {}): {}",
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
			PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(animation.getKey(), 100, clientPlayer -> {
				@NotNull var playerAnimationLayer = new ModifierLayer<>();
				@NotNull var animationDefinition = animation.getValue();
				@NotNull var interpolationDefinitionOptional = animationDefinition.getAnimationInterpolationDefinition();
				if (interpolationDefinitionOptional.isEmpty()) {
					return playerAnimationLayer;
				}

				@NotNull var interpolationDefinition = interpolationDefinitionOptional.get();
				@NotNull var lengthInOptional = interpolationDefinition.getLengthIn();
				@NotNull var easeTypeOptional = interpolationDefinition.getConvertedType();
				if (lengthInOptional.isEmpty() || easeTypeOptional.isEmpty()) {
					return playerAnimationLayer;
				}

				@NotNull var lengthIn = lengthInOptional.get();
				@NotNull var easeType = easeTypeOptional.get();
				playerAnimationLayer.addModifierLast(new AbstractFadeModifier(Math.round(lengthIn / TICKS_PER_SECOND)) {
					@Override
					protected float getAlpha(String modelName, TransformType type, float progress) {
						return easeType.invoke(progress);
					}
				});
				return playerAnimationLayer;
			});
		}
	}
}
