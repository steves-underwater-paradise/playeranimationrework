package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import com.github.steveplays28.playeranimationrework.animation.Animation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonArmAnimations;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonSingleArmAnimation;

public class TurnAnimation extends Animation {
	private static final String TURN_LEFT_ANIMATION_NAME = "turn_left";
	private static final String TURN_RIGHT_ANIMATION_NAME = "turn_right";

	private float previousBodyYaw;

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;
		if (isMoving) {
			return;
		}

		int bodyYawDelta = (int) (player.getBodyYaw() - this.previousBodyYaw);
		if (Math.abs(bodyYawDelta) > 0) {
			super.tick(player);
			this.shouldPlay = true;

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

		this.previousBodyYaw = player.getBodyYaw();
	}

	@Override
	protected @Nullable String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		int bodyYawDelta = (int) (player.getBodyYaw() - this.previousBodyYaw);
		if (Math.abs(bodyYawDelta) > 0) {
			if (bodyYawDelta < 0) {
				return TURN_LEFT_ANIMATION_NAME;
			} else {
				return TURN_RIGHT_ANIMATION_NAME;
			}
		}

		return null;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.TURN;
	}
}
