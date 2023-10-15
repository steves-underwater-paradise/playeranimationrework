package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.PlayerAnimationRework;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class JumpAnimationCheck implements AnimationCheck {
	private static final String[] ANIMATION_NAMES = new String[]{"jump_first", "jump_second"};

	// TODO: Make correct jumpIndex change
	private boolean isJumpIndexOdd = false;
	private boolean shouldPlay = false;

	@Override
	public void jump(AbstractClientPlayerEntity player) {
		boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

		this.shouldPlay = !isMoving;
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(
				new Identifier(PlayerAnimationRework.MOD_ID, ANIMATION_NAMES[isJumpIndexOdd ? 1 : 0])
		);

		return new AnimationData(animation, 1.0f, 5);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.JUMP;
	}

	@Override
	public boolean getShouldPlay() {
		return this.shouldPlay;
	}

	@Override
	public void cleanup() {
		this.shouldPlay = false;
	}
}
