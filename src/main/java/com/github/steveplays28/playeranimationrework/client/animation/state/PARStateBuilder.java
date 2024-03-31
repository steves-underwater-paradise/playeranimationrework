package com.github.steveplays28.playeranimationrework.client.animation.state;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PARStateBuilder {
	private boolean isWalking;
	private boolean isSprinting;
	private boolean isSneaking;
	private ItemStack equippedMainHandItemStack;
	private ItemStack equippedOffHandItemStack;

	public PARStateBuilder() {
		this.isWalking = false;
		this.isSprinting = false;
		this.isSneaking = false;
		this.equippedMainHandItemStack = ItemStack.EMPTY;
		this.equippedOffHandItemStack = ItemStack.EMPTY;
	}

	public PARStateBuilder(@NotNull PARState state) {
		this.isWalking = state.isWalking();
		this.isSprinting = state.isSprinting();
		this.isSneaking = state.isSneaking();
		this.equippedMainHandItemStack = state.getEquippedMainHandItemStack();
		this.equippedOffHandItemStack = state.getEquippedOffHandItemStack();
	}

	public PARStateBuilder setIsWalking(boolean isWalking) {
		this.isWalking = isWalking;
		return this;
	}

	public PARStateBuilder setIsSprinting(boolean isSprinting) {
		this.isSprinting = isSprinting;
		return this;
	}

	public PARStateBuilder setIsSneaking(boolean isSneaking) {
		this.isSneaking = isSneaking;
		return this;
	}

	public PARStateBuilder setEquippedMainHandItemStack(ItemStack equippedMainHandItemStack) {
		this.equippedMainHandItemStack = equippedMainHandItemStack;
		return this;
	}

	public PARStateBuilder setEquippedOffHandItemStack(ItemStack equippedOffHandItemStack) {
		this.equippedOffHandItemStack = equippedOffHandItemStack;
		return this;
	}

	public PARState build() {
		return new PARState(isWalking, isSprinting, isSneaking, equippedMainHandItemStack, equippedOffHandItemStack);
	}
}
