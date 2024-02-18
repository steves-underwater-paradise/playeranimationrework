package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import com.github.steveplays28.playeranimationrework.animation.Animation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.SLIDE_SOUND_EVENT;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonArmAnimations;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonSingleArmAnimation;

public class SprintAnimation extends Animation {
	private static final String ANIMATION_NAME = "running";
	private static final String STOP_ANIMATION_NAME = "sprint_stop";

	private boolean wasPlayerSprintingLastTick = false;
	private int fadeDurationTicks;

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		if (player.isSprinting() || wasPlayerSprintingLastTick) {
			super.tick(player);
			shouldPlay = true;

			if (getItemsWithThirdPersonArmAnimations().contains(player.getEquippedStack(
					EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonArmAnimations().contains(
					player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
				disableModelParts(ModelPart.LEFT_ARM, ModelPart.RIGHT_ARM);
			}

			if (getItemsWithThirdPersonSingleArmAnimation().contains(
					player.getEquippedStack(EquipmentSlot.MAINHAND).getItem().getClass())) {
				disableModelParts(ModelPart.RIGHT_ARM);
			} else if (getItemsWithThirdPersonSingleArmAnimation().contains(
					player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
				disableModelParts(ModelPart.LEFT_ARM);
			}
		}

		if (wasPlayerSprintingLastTick) {
			fadeDurationTicks = 2;
		}

		this.wasPlayerSprintingLastTick = player.isSprinting();
	}

	@Override
	public void onPlay(@NotNull AbstractClientPlayerEntity player, String selectedAnimationName) {
		if (!selectedAnimationName.equals(STOP_ANIMATION_NAME)) {
			return;
		}

		// Play slide sound
		var world = MinecraftClient.getInstance().world;
		if (world != null) {
			world.playSound(player, player.getBlockPos(), SLIDE_SOUND_EVENT, SoundCategory.PLAYERS);
		}
	}

	@Override
	public int getFadeDurationTicks() {
		return fadeDurationTicks;
	}

	@Override
	protected @Nullable String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		if (player.isSprinting()) {
			return ANIMATION_NAME;
		} else if (wasPlayerSprintingLastTick) {
			return STOP_ANIMATION_NAME;
		}

		return null;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.SPRINT;
	}

	@Override
	public void cleanup() {
		super.cleanup();
		fadeDurationTicks = super.getFadeDurationTicks();
	}
}
