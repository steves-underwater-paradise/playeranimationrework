package com.github.steveplays28.playeranimationrework.client.extension;

import com.github.steveplays28.playeranimationrework.client.animation.state.PARStateMachine;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;

public interface PlayerEntityExtension {
	ModifierLayer<IAnimation> playerAnimationRework$getModifierLayer();

	PARStateMachine playerAnimationRework$getStateMachine();
}
