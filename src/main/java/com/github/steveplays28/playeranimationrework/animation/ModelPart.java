package com.github.steveplays28.playeranimationrework.animation;

public enum ModelPart {
	LEFT_ARM("leftArm"), RIGHT_ARM("rightArm");

	private final String name;

	ModelPart(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
