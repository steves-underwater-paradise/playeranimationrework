package com.github.steveplays28.playeranimationrework.client.animation.state;

import org.jetbrains.annotations.NotNull;

public class PARStateBuilder {
	private boolean isWalking;
	private boolean isSprinting;

	public PARStateBuilder() {
		// NO-OP
	}

	public PARStateBuilder(@NotNull PARState state) {
		this.isWalking = state.isWalking();
		this.isSprinting = state.isSprinting();
	}

	public PARStateBuilder setIsWalking(boolean isWalking) {
		this.isWalking = isWalking;
		return this;
	}

	public PARStateBuilder setIsSprinting(boolean isSprinting) {
		this.isSprinting = isSprinting;
		return this;
	}

	public PARState build() {
		return new PARState(isWalking, isSprinting);
	}
}
