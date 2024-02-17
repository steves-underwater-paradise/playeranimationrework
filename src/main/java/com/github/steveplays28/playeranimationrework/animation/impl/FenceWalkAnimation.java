package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonArmAnimations;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonRightArmAnimations;

public class FenceWalkAnimation extends Animation {
	private static final String IDLE_ANIMATION_NAME = "fence_idle";
	private static final String WALK_ANIMATION_NAME = "fence_walk";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		super.tick(player);

		Block standingBlock = player.getWorld().getBlockState(player.getBlockPos().down()).getBlock();
		boolean onThinBlock = (standingBlock instanceof FenceBlock || standingBlock instanceof WallBlock || standingBlock instanceof PaneBlock) && player.isOnGround();
		if (!onThinBlock) {
			return;
		}

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
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

		if (isMoving) {
			return WALK_ANIMATION_NAME;
		} else {
			return IDLE_ANIMATION_NAME;
		}
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.FENCE_WALK;
	}
}
