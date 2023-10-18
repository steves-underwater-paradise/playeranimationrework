package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.sound.SoundCategory;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.SLIDE_SOUND_EVENT;
import static com.github.steveplays28.playeranimationrework.util.AnimationUtil.getAnimation;

public class SprintAnimationCheck implements AnimationCheck {
	private static final String ANIMATION_NAME = "running";
	private static final String STOP_ANIMATION_NAME = "sprint_stop";

	private boolean shouldPlay = false;
	private boolean wasPlayerSprintingLastTick = false;
	private String selectedAnimationName;
	private int fadeTime = 5;

	@Override
	public String getSelectedAnimationName() {
		return selectedAnimationName;
	}

	@Override
	public void tick(AbstractClientPlayerEntity player) {
		if (player.isSprinting()) {
			this.selectedAnimationName = ANIMATION_NAME;
			this.shouldPlay = true;
		} else if (wasPlayerSprintingLastTick) {
			this.selectedAnimationName = STOP_ANIMATION_NAME;
			this.fadeTime = 2;
			this.shouldPlay = true;
		}

		this.wasPlayerSprintingLastTick = player.isSprinting();
	}

	@Override
	public void onPlay(AbstractClientPlayerEntity player, String selectedAnimationName) {
		if (!selectedAnimationName.equals(STOP_ANIMATION_NAME)) {
			return;
		}

		// Play slide sound
		var world = MinecraftClient.getInstance().world;
		if (world != null) {
			world.playSound(player, player.getBlockPos(), SLIDE_SOUND_EVENT, SoundCategory.PLAYERS);
		}
	}

	@Override
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = getAnimation(selectedAnimationName);
		return new AnimationData(animation, 1f, fadeTime, Ease.INOUTSINE);
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.SPRINT;
	}

	@Override
	public boolean getShouldPlay() {
		return this.shouldPlay;
	}

	@Override
	public void cleanup() {
		this.shouldPlay = false;
		this.selectedAnimationName = null;
		this.fadeTime = 5;
	}
}
