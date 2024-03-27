package com.github.steveplays28.playeranimationrework.animation;

public enum AnimationPriority {
	WALK(1),
	TURN(2),
	FENCE_WALK(3),
	EDGE_STANDING(4),
	SPRINT(5),
	SNEAK(6),
	FALL(7),
	JUMP(8),
	SWIM(9),
	CRAWLING(10),
	CLIMBING(11),
	BOAT(12),
	ELYTRA(13),
	PUNCH(14),
	EATING(15),
	FLINT_AND_STEEL(16),
	WORKBENCH_USE(17);

	private final int value;

	AnimationPriority(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
