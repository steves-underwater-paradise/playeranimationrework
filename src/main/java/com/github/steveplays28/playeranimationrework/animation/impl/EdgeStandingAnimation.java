package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class EdgeStandingAnimation extends Animation {
	private static final String BALANCE_LOSS_ANIMATION_NAME = "edge_idle";

	@Override
	@SuppressWarnings("deprecation")
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		super.tick(player);

		boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;
		BlockState standingBlockState = player.getWorld().getBlockState(player.getBlockPos().down());

		// standingBlockState gets the state of the block under the player by rounding the player's coordinates and
		// finding the block with those coordinates, and it doesn't have to be the block the player is standing on.
		// Therefore, when the player is standing on the edge, it will be an air block, which is not .blocksMovement()
		// (due to rounding up the coordinates)
		this.shouldPlay = !standingBlockState.blocksMovement() && player.isOnGround() && !isMoving;
	}

	@Override
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		return BALANCE_LOSS_ANIMATION_NAME;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.EDGE_STANDING;
	}
}
