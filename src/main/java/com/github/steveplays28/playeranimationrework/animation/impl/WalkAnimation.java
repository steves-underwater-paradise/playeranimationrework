package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import com.github.steveplays28.playeranimationrework.animation.Animation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonArmAnimations;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonRightArmAnimations;

public class WalkAnimation extends Animation {
	private static final String IDLE_ANIMATION_NAME = "idle";
	private static final String WALK_ANIMATION_NAME = "walking";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		super.tick(player);
		this.shouldPlay = true;

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
		// TODO: Sync feet movement to floor
		boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

		if (isMoving && player.isOnGround()) {
			return WALK_ANIMATION_NAME;
		} else {
			return IDLE_ANIMATION_NAME;
		}
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.WALK;
	}
}
