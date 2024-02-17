package com.github.steveplays28.playeranimationrework.animation.impl;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.RANDOM;

public class PunchAnimation extends Animation {
	private static final String[] ANIMATION_NAMES = new String[]{"punch_left", "punch_right"};
	private static final String[] SWORD_SWING_RIGHT_ANIMATION_NAMES = new String[]{"sword_swing_right", "sword_swing_right_2"};
	private static final String PICKAXE_MINE_RIGHT_ANIMATION_NAME = "mine_right";
	private static final String SNEAK_ANIMATION_SUFFIX = "_sneak";

	private String selectedAnimationName;

	@Override
	public void tick(@NotNull AbstractClientPlayerEntity player) {
		// NOOP
		// Stops the getNewSelectedAnimationName() method from running every tick when it's unnecessary to do so
		// Instead it's manually invoked in swingHand()
	}

	@Override
	public void swingHand(@NotNull AbstractClientPlayerEntity player, @NotNull Hand hand) {
		if (player.handSwinging && player.handSwingTicks < this.getHandSwingDuration(player) / 2 && player.handSwingTicks > 0) {
			return;
		}

		selectedAnimationName = getNewSelectedAnimationName(player, hand);
		shouldPlay = true;
	}

	@Override
	public @Nullable String getSelectedAnimationName() {
		return selectedAnimationName;
	}

	@Override
	public int getFadeDurationTicks() {
		return 1;
	}

	@Override
	protected @Nullable String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player) {
		// NOOP
		return null;
	}

	@Override
	public AnimationPriority getPriority() {
		return AnimationPriority.PUNCH;
	}

	protected @NotNull String getNewSelectedAnimationName(@NotNull AbstractClientPlayerEntity player, @NotNull Hand hand) {
		var equippedItem = player.getEquippedStack(EquipmentSlot.MAINHAND).getItem();

		if (equippedItem instanceof SwordItem) {
			return SWORD_SWING_RIGHT_ANIMATION_NAMES[RANDOM.nextInt(SWORD_SWING_RIGHT_ANIMATION_NAMES.length)];
		} else if (equippedItem instanceof PickaxeItem || equippedItem instanceof AxeItem) {
			return PICKAXE_MINE_RIGHT_ANIMATION_NAME;
		} else {
			String selectedAnimationName = ANIMATION_NAMES[hand == Hand.MAIN_HAND ? 1 : 0];
			if (player.isSneaking()) {
				selectedAnimationName += SNEAK_ANIMATION_SUFFIX;
			}

			return selectedAnimationName;
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
}
