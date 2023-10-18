package com.github.steveplays28.playeranimationrework.animation.check;

import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;

import java.util.ArrayList;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.*;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonArmAnimations;

public class ClimbingAnimationCheck implements AnimationCheck {
	private static final String IDLE_ANIMATION_NAME = "climbing_idle";
	private static final String WALK_ANIMATION_NAME = "climbing";

	private final ArrayList<ModelPart> disabledModelParts = new ArrayList<>();

	private boolean shouldPlay = false;
	private String selectedAnimationName;

	@Override
	public void tick(AbstractClientPlayerEntity player) {
		if (!player.isClimbing()) {
			return;
		}

		this.shouldPlay = true;

		if (Math.abs(player.getY() - player.prevY) > 0) {
			this.selectedAnimationName = WALK_ANIMATION_NAME;
		} else {
			this.selectedAnimationName = IDLE_ANIMATION_NAME;
		}

		if (getItemsWithThirdPersonRightArmAnimations().contains(player.getEquippedStack(
				EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonRightArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass()) || getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
			disabledModelParts.add(ModelPart.RIGHT_ARM);
		}
	}

	@Override
	public AnimationData getAnimationData() {
		return new AnimationData(getAnimation(selectedAnimationName), 1.0f, 5, disabledModelParts);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.CLIMBING;
	}

	@Override
	public boolean getShouldPlay() {
		return this.shouldPlay;
	}

	@Override
	public void cleanup() {
		this.shouldPlay = false;
		this.selectedAnimationName = null;
	}
}
