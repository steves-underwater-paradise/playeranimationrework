package com.github.steveplays28.playeranimationrework.animation;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AnimationData {
	private final float speed;
	private final int fade;
	private final Ease fadeEasing;
	private final ArrayList<ModelPart> disabledModelParts;

	private KeyframeAnimation animation;

	public AnimationData(@NotNull KeyframeAnimation animation, float speed, int fade) {
		this.animation = animation;
		this.speed = speed;
		this.fade = fade;
		this.fadeEasing = Ease.LINEAR;
		this.disabledModelParts = new ArrayList<>();
	}

	public AnimationData(@NotNull KeyframeAnimation animation, float speed, int fade, Ease fadeEasing) {
		this.animation = animation;
		this.speed = speed;
		this.fade = fade;
		this.fadeEasing = fadeEasing;
		this.disabledModelParts = new ArrayList<>();
	}

	public AnimationData(@NotNull KeyframeAnimation animation, float speed, int fade, ArrayList<ModelPart> disabledModelParts) {
		this.animation = animation;
		this.speed = speed;
		this.fade = fade;
		this.fadeEasing = Ease.LINEAR;
		this.disabledModelParts = disabledModelParts;
	}

	public AnimationData(@NotNull KeyframeAnimation animation, float speed, int fade, Ease fadeEasing, ArrayList<ModelPart> disabledModelParts) {
		this.animation = animation;
		this.speed = speed;
		this.fade = fade;
		this.fadeEasing = fadeEasing;
		this.disabledModelParts = disabledModelParts;
	}

	public KeyframeAnimation getKeyframeAnimation() {
		return this.animation;
	}

	public void setKeyframeAnimation(KeyframeAnimation keyframeAnimation) {
		this.animation = keyframeAnimation;
	}

	public void setAnimation(ModifierLayer<IAnimation> animationContainer) {
		// Disable model parts
		disabledModelParts.forEach(this::disableModelPart);

		if (animationContainer.size() > 0) {
			animationContainer.removeModifier(0);
		}

		if (this.speed != 1.0f) {
			animationContainer.addModifier(new SpeedModifier(this.speed), 0);
		}

		IAnimation animationPlayer = new KeyframeAnimationPlayer(this.animation);

		if (this.fade == 0) {
			animationContainer.setAnimation(animationPlayer);
		} else {
			animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fade, fadeEasing), animationPlayer);
		}
	}

	public void disableModelPart(@NotNull ModelPart modelPart) {
		var keyframeAnimationBuilder = getKeyframeAnimation().mutableCopy();
		var keyframeModelPart = keyframeAnimationBuilder.getPart(modelPart.getName());
		if (keyframeModelPart == null) {
			throw new IllegalArgumentException(
					String.format("Model part (disabled by animation %s, made by %s) doesn't exist", animation.extraData.get("name"),
							animation.extraData.get("author")
					));
		}

		keyframeModelPart.setEnabled(false);
		setKeyframeAnimation(keyframeAnimationBuilder.build());
	}
}
