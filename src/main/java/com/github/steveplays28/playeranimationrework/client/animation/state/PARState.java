package com.github.steveplays28.playeranimationrework.client.animation.state;

public class PARState {
	private final boolean isWalking;
	private final boolean isSprinting;
	private final boolean isSneaking;

	PARState(boolean isWalking, boolean isSprinting, boolean isSneaking) {
		this.isWalking = isWalking;
		this.isSprinting = isSprinting;
		this.isSneaking = isSneaking;
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
}
