package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import com.github.steveplays28.playeranimationrework.animation.Animation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.RANDOM;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonArmAnimations;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonRightArmAnimations;

public class JumpAnimation extends Animation {
	private static final String[] ANIMATION_NAMES = new String[]{"jump"};

	@Override
	public void jump(@NotNull AbstractClientPlayerEntity player) {
		// TODO: Keep playing jump animation for N ticks after jumping
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
		return ANIMATION_NAMES[RANDOM.nextInt(ANIMATION_NAMES.length)];
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.JUMP;
	}
}
