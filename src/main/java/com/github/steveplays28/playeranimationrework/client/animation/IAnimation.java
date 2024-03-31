package com.github.steveplays28.playeranimationrework.client.animation;

import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IAnimation {
	IAnimation register();

	int getPriority();

	void start(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer);

	void stop(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer);

	void continueWithSpecificBodyPartsEnabled(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull KeyframeAnimation animation, @NotNull List<ModelPart> unanimatedBodyParts);

	void continueWithSpecificBodyPartsDisabled(@NotNull ModifierLayer<dev.kosmx.playerAnim.api.layered.IAnimation> playerAnimationModifierLayer, @NotNull KeyframeAnimation animation, @NotNull List<ModelPart> unanimatedBodyParts);
}
