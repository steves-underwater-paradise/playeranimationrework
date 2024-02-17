package com.github.steveplays28.playeranimationrework.animation;

import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Animation {
	protected boolean shouldPlay = false;

	private final List<ModelPart> disabledModelParts = new ArrayList<>();

	private String selectedAnimationName;

	public abstract AnimationPriority getPriority();

	public void tick(@NotNull AbstractClientPlayerEntity player) {
		selectedAnimationName = getNewSelectedAnimationName(player);
	}

	public void jump(@NotNull AbstractClientPlayerEntity player) {}

	public void swingHand(@NotNull AbstractClientPlayerEntity player, @NotNull Hand hand) {}

	public void fall(@NotNull AbstractClientPlayerEntity player, double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {}

	public void cleanup() {
		shouldPlay = false;
		selectedAnimationName = null;
		disabledModelParts.clear();
	}

	public void onPlay(@NotNull AbstractClientPlayerEntity player, String selectedAnimationName) {}

	public boolean getShouldPlay() {
		return shouldPlay;
	}

	public String getSelectedAnimationName() {
		return selectedAnimationName;
	}

	public float getSpeed() {
		return 1f;
	}

	public int getFadeDurationTicks() {
		return 5;
	}

	public Ease getFadeEasing() {
		return Ease.INOUTEXPO;
	}

	public List<ModelPart> getDisabledModelParts() {
		return disabledModelParts;
	}

	public void disableModelParts(ModelPart... modelParts) {
		disabledModelParts.addAll(Arrays.asList(modelParts));
	}

	public AnimationData getAnimationData() {
		return new AnimationData(this);
	}

	protected abstract @Nullable String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player);
}
