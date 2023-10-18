package com.github.steveplays28.playeranimationrework.animation.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;

import java.util.Objects;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.RANDOM;
import static com.github.steveplays28.playeranimationrework.client.util.AnimationUtil.getAnimation;

public class PunchAnimationCheck implements AnimationCheck {
	private static final String[] ANIMATION_NAMES = new String[]{"punch_left", "punch_right"};
	private static final String[] SWORD_SWING_RIGHT_ANIMATION_NAMES = new String[]{"sword_swing_right", "sword_swing_right_2"};
	private static final String PICKAXE_MINE_RIGHT_ANIMATION_NAME = "mine_right";
	private static final String SNEAK_ANIMATION_SUFFIX = "_sneak";

	private boolean shouldPlay = false;
	private String selectedAnimationName;

	@Override
	public void swingHand(AbstractClientPlayerEntity player, Hand hand) {
		if (player.handSwinging && player.handSwingTicks < this.getHandSwingDuration(player) / 2 && player.handSwingTicks > 0) {
			return;
		}

		var equippedItem = player.getEquippedStack(EquipmentSlot.MAINHAND).getItem();

		if (equippedItem instanceof SwordItem) {
			selectedAnimationName = SWORD_SWING_RIGHT_ANIMATION_NAMES[RANDOM.nextInt(SWORD_SWING_RIGHT_ANIMATION_NAMES.length)];
		} else if (equippedItem instanceof PickaxeItem || equippedItem instanceof AxeItem) {
			selectedAnimationName = PICKAXE_MINE_RIGHT_ANIMATION_NAME;
		} else {
			selectedAnimationName = ANIMATION_NAMES[hand == Hand.MAIN_HAND ? 1 : 0];
			if (player.isSneaking()) {
				selectedAnimationName += SNEAK_ANIMATION_SUFFIX;
			}
		}

		shouldPlay = true;
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
		return new AnimationData(animation, 1f, 1);
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
