package com.github.steveplays28.playeranimationrework.client.animation;

import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import org.jetbrains.annotations.NotNull;

public interface IAnimation {
	IAnimation register();

	int getPriority();

	void start(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer);

	void stop(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer);

	void continueWithSpecificBodyParts();
}
