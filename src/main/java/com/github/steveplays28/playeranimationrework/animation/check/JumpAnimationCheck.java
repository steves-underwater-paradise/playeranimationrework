package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import static com.github.steveplays28.playeranimationrework.util.AnimationUtil.getAnimation;

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
		KeyframeAnimation animation = getAnimation(ANIMATION_NAMES[isJumpIndexOdd ? 1 : 0]);
		return new AnimationData(animation, 1.0f, 150, Ease.INOUTCUBIC);
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
