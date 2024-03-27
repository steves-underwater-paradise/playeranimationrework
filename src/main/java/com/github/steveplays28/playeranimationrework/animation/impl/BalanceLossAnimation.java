package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.Animation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class BalanceLossAnimation extends Animation {
	private static final String BALANCE_LOSS_ANIMATION_NAME = "edge_idle";

	@Override
	@SuppressWarnings("deprecation")
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		// TODO
//		boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;
//		var world = player.getWorld();
//		var blockPosBelowPlayer = player.getBlockPos().down();
//		var blockStateBelowPlayer = world.getBlockState(blockPosBelowPlayer);
//
//		// blockStateBelowPlayer is the state of the block under the player, gotten by rounding the player's coordinates and finding the block with those coordinates
//		// It doesn't have to be the block the player is standing on
//		// Therefore, when the player is standing on the edge, it will be an air block, which is not .blocksMovement() (due to rounding up the coordinates)
//		if (!blockStateBelowPlayer.blocksMovement() && player.isOnGround() && !isMoving && !blockStateBelowPlayer.isSolidBlock(
//				world, blockPosBelowPlayer)) {
//			super.tick(player);
//			this.shouldPlay = true;
//		}
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
