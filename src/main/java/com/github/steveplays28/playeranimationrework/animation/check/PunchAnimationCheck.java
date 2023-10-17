package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Hand;

import java.util.Objects;

import static com.github.steveplays28.playeranimationrework.util.AnimationUtil.getAnimation;

public class PunchAnimationCheck implements AnimationCheck {
	private static final String[] ANIMATION_NAMES = new String[]{"punch_left", "punch_right"};
	private static final String SNEAK_ANIMATION_SUFFIX = "_sneak";

	private boolean shouldPlay = false;
	private String selectedAnimationName;

	@Override
	public void swingHand(AbstractClientPlayerEntity player, Hand hand) {
		if (player.handSwinging && player.handSwingTicks < this.getHandSwingDuration(player) / 2 && player.handSwingTicks > 0) {
			return;
		}

		this.shouldPlay = true;
		this.selectedAnimationName = ANIMATION_NAMES[hand == Hand.MAIN_HAND ? 1 : 0];

		if (player.isSneaking()) {
			this.selectedAnimationName += SNEAK_ANIMATION_SUFFIX;
		}
	}

	private int getHandSwingDuration(AbstractClientPlayerEntity player) {
		if (StatusEffectUtil.hasHaste(player)) {
			return 6 - (1 + StatusEffectUtil.getHasteAmplifier(player));
		} else {
			return player.hasStatusEffect(StatusEffects.MINING_FATIGUE) ? 6 + (1 + Objects.requireNonNull(
					player.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier()) * 2 : 6;
		}
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = getAnimation(selectedAnimationName);
		return new AnimationData(animation, 0.2f, 0);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.PUNCH;
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
