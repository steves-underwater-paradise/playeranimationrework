package com.github.steveplays28.playeranimationrework.client.animation.state;

import com.github.steveplays28.playeranimationrework.client.event.animation.state.PARPlayerStateChangeEvents;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PARStateMachine {
	private PARState state;
	private PARState previousState;

	public PARStateMachine() {
		this.state = new PARStateBuilder().build();
		this.previousState = new PARStateBuilder().build();
	}

	public PARState getState() {
		return state;
	}

	public PARState getPreviousState() {
		return previousState;
	}

	public void setState(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, PARState newState) {
		this.previousState = getState();
		this.state = newState;

		PARPlayerStateChangeEvents.PLAYER_STATE_CHANGED.invoker().onPlayerStateChanged(
				playerAnimationModifierLayer, getPreviousState(), newState);

		if (getPreviousState().isSneaking() != newState.isSneaking()) {
			PARPlayerStateChangeEvents.PLAYER_SNEAK_STATE_CHANGED.invoker().onPlayerSneakStateChanged(
					playerAnimationModifierLayer, getPreviousState(), newState);

			if (!getPreviousState().isSneaking() && newState.isSneaking()) {
				PARPlayerStateChangeEvents.PLAYER_SNEAK.invoker().onPlayerSneak(playerAnimationModifierLayer, getPreviousState(), newState);
			}
			if (getPreviousState().isSneaking() && !newState.isSneaking()) {
				PARPlayerStateChangeEvents.PLAYER_UNSNEAK.invoker().onPlayerUnsneak(
						playerAnimationModifierLayer, getPreviousState(), newState);
			}
		}
	}

	/**
	 * Allows a mixin injection to invoke {@link PARPlayerStateChangeEvents#PLAYER_EQUIP_MAIN_HAND_ITEM_STACK},
	 * which is slightly faster than letting {@link PARStateMachine#setState} do the comparison.
	 *
	 * @param playerAnimationModifierLayer The {@link PlayerEntity}'s animation modifier layer.
	 */
	public void invokePlayerEquipMainHandItemStack(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		PARPlayerStateChangeEvents.PLAYER_EQUIP_MAIN_HAND_ITEM_STACK.invoker().onPlayerEquippedMainHandItemStack(
				playerAnimationModifierLayer, getPreviousState(), getState()
		);
	}

	/**
	 * Allows a mixin injection to invoke {@link PARPlayerStateChangeEvents#PLAYER_EQUIP_OFF_HAND_ITEM_STACK},
	 * which is slightly faster than letting {@link PARStateMachine#setState} do the comparison.
	 *
	 * @param playerAnimationModifierLayer The {@link PlayerEntity}'s animation modifier layer.
	 */
	public void invokePlayerEquipOffHandItemStack(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		PARPlayerStateChangeEvents.PLAYER_EQUIP_OFF_HAND_ITEM_STACK.invoker().onPlayerEquippedOffHandItemStack(
				playerAnimationModifierLayer, getPreviousState(), getState()
		);
	}

	/**
	 * Allows a mixin injection to invoke {@link PARPlayerStateChangeEvents#PLAYER_EQUIP_MAIN_HAND_ITEM_STACK},
	 * which is slightly faster than letting {@link PARStateMachine#setState} do the comparison.
	 *
	 * @param playerAnimationModifierLayer The {@link PlayerEntity}'s animation modifier layer.
	 */
	public void invokePlayerUnequipMainHandItemStack(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		PARPlayerStateChangeEvents.PLAYER_UNEQUIP_MAIN_HAND_ITEM_STACK.invoker().onPlayerUnequippedMainHandItemStack(
				playerAnimationModifierLayer, getPreviousState(), getState()
		);
	}

	/**
	 * Allows a mixin injection to invoke {@link PARPlayerStateChangeEvents#PLAYER_EQUIP_MAIN_HAND_ITEM_STACK},
	 * which is slightly faster than letting {@link PARStateMachine#setState} do the comparison.
	 *
	 * @param playerAnimationModifierLayer The {@link PlayerEntity}'s animation modifier layer.
	 */
	public void invokePlayerUnequipOffHandItemStack(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer) {
		PARPlayerStateChangeEvents.PLAYER_UNEQUIP_OFF_HAND_ITEM_STACK.invoker().onPlayerUnequippedOffHandItemStack(
				playerAnimationModifierLayer, getPreviousState(), getState()
		);
	}
}
