package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.RANDOM;
import static com.github.steveplays28.playeranimationrework.util.AnimationUtil.getAnimation;

public class JumpAnimationCheck implements AnimationCheck {
	private static final String[] ANIMATION_NAMES = new String[]{"jump"};

	private boolean shouldPlay = false;

	@Override
	public void jump(AbstractClientPlayerEntity player) {
		this.shouldPlay = true;
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = getAnimation(ANIMATION_NAMES[RANDOM.nextInt(ANIMATION_NAMES.length)]);
		return new AnimationData(animation, 1.0f, 5, Ease.INOUTSINE);
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
