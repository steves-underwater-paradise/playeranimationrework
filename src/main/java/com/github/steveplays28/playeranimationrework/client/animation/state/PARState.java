package com.github.steveplays28.playeranimationrework.client.animation.state;

import net.minecraft.item.ItemStack;

public class PARState {
	private final boolean isWalking;
	private final boolean isSprinting;
	private final boolean isSneaking;
	private final ItemStack equippedMainHandItemStack;
	private final ItemStack equippedOffHandItemStack;

	PARState(boolean isWalking, boolean isSprinting, boolean isSneaking, ItemStack equippedMainHandItemStack, ItemStack equippedOffHandItemStack) {
		this.isWalking = isWalking;
		this.isSprinting = isSprinting;
		this.isSneaking = isSneaking;
		this.equippedMainHandItemStack = equippedMainHandItemStack;
		this.equippedOffHandItemStack = equippedOffHandItemStack;
	}

	public boolean isWalking() {
		return isWalking;
	}

	public boolean isSprinting() {
		return isSprinting;
	}

	public boolean isSneaking() {
		return isSneaking;
	}

	public ItemStack getEquippedMainHandItemStack() {
		return equippedMainHandItemStack;
	}

	public ItemStack getEquippedOffHandItemStack() {
		return equippedOffHandItemStack;
	}
}
