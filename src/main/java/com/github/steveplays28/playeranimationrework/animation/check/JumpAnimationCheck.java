package com.github.steveplays28.playeranimationrework.animation.check;

import com.github.steveplays28.playeranimationrework.animation.ModelPart;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;

import java.util.ArrayList;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.RANDOM;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.*;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getItemsWithThirdPersonRightArmAnimations;

public class JumpAnimationCheck implements AnimationCheck {
	private static final String[] ANIMATION_NAMES = new String[]{"jump"};

	private final ArrayList<ModelPart> disabledModelParts = new ArrayList<>();

	private boolean shouldPlay = false;

	@Override
	public void jump(AbstractClientPlayerEntity player) {
		this.shouldPlay = true;

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
	public AnimationData getAnimationData() {
		KeyframeAnimation animation = getAnimation(ANIMATION_NAMES[RANDOM.nextInt(ANIMATION_NAMES.length)]);
		return new AnimationData(animation, 1.0f, 5, Ease.INOUTSINE, disabledModelParts);
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
