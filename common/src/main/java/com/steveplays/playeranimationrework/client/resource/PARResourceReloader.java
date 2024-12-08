package com.steveplays.playeranimationrework.client.resource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.steveplays.playeranimationrework.PlayerAnimationRework;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition;
import com.steveplays.playeranimationrework.client.registry.PARAnimationRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import static com.steveplays.playeranimationrework.PlayerAnimationRework.MOD_ID;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class PARResourceReloader extends SinglePreparationResourceReloader<Void> {
	private static final @NotNull Gson GSON = new Gson();
	private static final @NotNull String JSON_FILE_SUFFIX = ".json";
	private static final @NotNull String ANIMATION_DEFINITIONS_FOLDER_NAME = "animations";

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
		// NO-OP
	}

	private void loadAnimations(@NotNull ResourceManager resourceManager) {
		PARAnimationRegistry.ANIMATION_REGISTRY.clear();

		@NotNull var jsonAnimations = resourceManager.findResources(String.format("%s/%s", MOD_ID, ANIMATION_DEFINITIONS_FOLDER_NAME), identifier -> identifier.toString().endsWith(JSON_FILE_SUFFIX));
		for (@NotNull var jsonAnimation : jsonAnimations.entrySet()) {
			@NotNull var jsonAnimationPath = jsonAnimation.getKey();
			@NotNull var jsonAnimationPathSplit = jsonAnimationPath.getPath().replace(JSON_FILE_SUFFIX, "").split("/");
			@NotNull var animationIdentifier = Identifier.of(jsonAnimationPath.getNamespace(), jsonAnimationPathSplit[jsonAnimationPathSplit.length - 1]);

			if (PARAnimationRegistry.ANIMATION_REGISTRY.containsKey(animationIdentifier)) {
				continue;
			}

			try {
				PARAnimationRegistry.ANIMATION_REGISTRY.put(animationIdentifier, AnimationDefinition.CODEC
						.parse(JsonOps.INSTANCE, GSON.fromJson(new String(jsonAnimation.getValue().getInputStream().readAllBytes()), JsonElement.class)).result().orElseThrow());
			} catch (IOException e) {
				PlayerAnimationRework.LOGGER.error("Exception thrown while deserializing an animation definition (identifier: {}): {}", animationIdentifier, e);
			}
		}
	}
}
