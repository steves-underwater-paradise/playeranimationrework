package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import com.github.steveplays28.playeranimationrework.animation.Animation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonArmAnimations;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonSingleArmAnimation;

public class ClimbingAnimation extends Animation {
	private static final String IDLE_ANIMATION_NAME = "climbing_idle";
	private static final String WALK_ANIMATION_NAME = "climbing";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		if (!player.isClimbing()) {
			return;
		}

		super.tick(player);
		this.shouldPlay = true;

		if (getItemsWithThirdPersonSingleArmAnimation().contains(player.getEquippedStack(
				EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonSingleArmAnimation().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass()) || getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
			disableModelParts(ModelPart.RIGHT_ARM);
		}
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.CLIMBING;
	}

	@Override
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		if (Math.abs(player.getY() - player.prevY) > 0) {
			return WALK_ANIMATION_NAME;
		} else {
			return IDLE_ANIMATION_NAME;
		}
	}
}
