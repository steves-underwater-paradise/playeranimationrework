package com.github.steveplays28.playeranimationrework.animation.check;

import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.SoundCategory;

import java.util.ArrayList;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.SLIDE_SOUND_EVENT;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.*;

public class SprintAnimationCheck implements AnimationCheck {
	private static final String ANIMATION_NAME = "running";
	private static final String STOP_ANIMATION_NAME = "sprint_stop";

	private final ArrayList<ModelPart> disabledModelParts = new ArrayList<>();

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

		if (getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
			disabledModelParts.add(ModelPart.LEFT_ARM);
			disabledModelParts.add(ModelPart.RIGHT_ARM);
		}

		if (getItemsWithThirdPersonRightArmAnimations().contains(player.getEquippedStack(
				EquipmentSlot.MAINHAND).getItem().getClass()) || getItemsWithThirdPersonRightArmAnimations().contains(
				player.getEquippedStack(EquipmentSlot.OFFHAND).getItem().getClass())) {
			disabledModelParts.add(ModelPart.RIGHT_ARM);
		}
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
		return new AnimationData(getAnimation(selectedAnimationName), 1f, fadeTime, Ease.INOUTSINE, disabledModelParts);
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
		disabledModelParts.clear();
	}
}
