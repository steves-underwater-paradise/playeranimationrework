package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorkbenchUseAnimation extends Animation {
	private static final String ANIMATION_NAME = "workbench_use";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		super.tick(player);

		// TODO: Sync from server
		shouldPlay = player.currentScreenHandler == null;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.WORKBENCH_USE;
	}

	@Override
	protected @Nullable String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		return ANIMATION_NAME;
	}
}
