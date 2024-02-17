package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonArmAnimations;

public class CrawlAnimation extends Animation {
	private static final String ANIMATION_NAME = "crawling";
	private static final String IDLE_ANIMATION_NAME = "crawl_idle";

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		super.tick(player);

		if (!player.isCrawling()) {
			return;
		}

		this.shouldPlay = true;

		if (getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
			disableModelParts(ModelPart.LEFT_ARM, ModelPart.RIGHT_ARM);
		}
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.CRAWLING;
	}

	@Override
	protected String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

		if (isMoving) {
			return ANIMATION_NAME;
		} else {
			return IDLE_ANIMATION_NAME;
		}
	}
}
