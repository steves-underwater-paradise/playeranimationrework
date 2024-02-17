package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonArmAnimations;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonRightArmAnimations;

public class SwimAnimation extends Animation {
	private static final String SWIM_ANIMATION_NAME = "swimming";
	private static final String SWIM_IDLE_ANIMATION_NAME = "swim_idle";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		if (player.isInSwimmingPose() || player.isInsideWaterOrBubbleColumn()) {
			shouldPlay = true;
		}

		if (getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
			disableModelParts(ModelPart.LEFT_ARM, ModelPart.RIGHT_ARM);
		}

		if (getItemsWithThirdPersonRightArmAnimations().contains(player.getEquippedStack(
				EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonRightArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
			disableModelParts(ModelPart.RIGHT_ARM);
		}
	}

	@Override
	protected @Nullable String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		if (player.isInSwimmingPose()) {
			return SWIM_ANIMATION_NAME;
		} else if (player.isInsideWaterOrBubbleColumn()) {
			return SWIM_IDLE_ANIMATION_NAME;
		}

		return null;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.SWIM;
	}
}
