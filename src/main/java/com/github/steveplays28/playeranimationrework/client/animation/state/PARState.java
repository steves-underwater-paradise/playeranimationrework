package com.github.steveplays28.playeranimationrework.client.animation.state;

public class PARState {
	private final boolean isWalking;
	private final boolean isSprinting;

	PARState(boolean isWalking, boolean isSprinting) {
		this.isWalking = isWalking;
		this.isSprinting = isSprinting;
	}

	public boolean isWalking() {
		return isWalking;
	}

	public boolean isSprinting() {
		return isSprinting;
	}
}
