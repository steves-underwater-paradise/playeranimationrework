package com.github.steveplays28.playeranimationrework.animation.check;

import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;

import java.util.ArrayList;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.*;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonRightArmAnimations;

public class WalkAnimationCheck implements AnimationCheck {
	private static final String IDLE_ANIMATION_NAME = "idle";
	private static final String WALK_ANIMATION_NAME = "walking";

	private final ArrayList<ModelPart> disabledModelParts = new ArrayList<>();

	private boolean shouldPlay = false;
	private String selectedAnimationName;

	@Override
	public void tick(AbstractClientPlayerEntity player) {
		if (!player.isOnGround()) {
			return;
		}

		this.shouldPlay = true;

		// TODO: Sync feet movement to floor
		boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

		if (isMoving) {
			this.selectedAnimationName = WALK_ANIMATION_NAME;
		} else {
			this.selectedAnimationName = IDLE_ANIMATION_NAME;
		}

		if (getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
			disabledModelParts.add(ModelPart.LEFT_ARM);
			disabledModelParts.add(ModelPart.RIGHT_ARM);
		}

		if (getItemsWithThirdPersonRightArmAnimations().contains(player.getEquippedStack(
				EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonRightArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
			disabledModelParts.add(ModelPart.RIGHT_ARM);
		}
	}

	@Override
	public AnimationData getAnimationData() {
		return new AnimationData(getAnimation(selectedAnimationName), 1f, 5, disabledModelParts);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.WALK;
	}

	@Override
	public boolean getShouldPlay() {
		return this.shouldPlay;
	}

	@Override
	public void cleanup() {
		this.shouldPlay = false;
		this.selectedAnimationName = null;
		disabledModelParts.clear();
	}
}
