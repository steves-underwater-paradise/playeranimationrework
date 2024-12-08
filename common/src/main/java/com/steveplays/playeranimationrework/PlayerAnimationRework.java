package com.steveplays.playeranimationrework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.util.Identifier;

public class PlayerAnimationRework {
	public static final String MOD_ID = "playeranimationrework";
	public static final String MOD_NAME = "Player Animation Rework";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static void initialize() {
		LOGGER.info("Loading {}.", MOD_NAME);

	// 	PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(MOD_ID, "animation"), 42, (player) -> {
	// 		if (player instanceof LocalPlayer) {
	// 			// animationStack.addAnimLayer(42, testAnimation); // Add and save the animation container for later use.
	// 			ModifierLayer<IAnimation> testAnimation = new ModifierLayer<>();

	// 			testAnimation.addModifierBefore(new SpeedModifier(0.5f)); // This will be slow
	// 			testAnimation.addModifierBefore(new MirrorModifier(true)); // Mirror the animation
	// 			return testAnimation;
	// 		}
	// 		return null;
	// 	});
	// }

	new KeyframeAnimationPlayer(new ModifierLayer<IAnimation>());
}
