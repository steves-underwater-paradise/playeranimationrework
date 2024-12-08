package com.steveplays.playeranimationrework.mixin.client;

import static com.steveplays.playeranimationrework.PlayerAnimationRework.MOD_ID;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition;
import com.steveplays.playeranimationrework.client.extension.AnimationDataExtension;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin implements AnimationDataExtension {
	@Unique private final @NotNull List<AnimationDefinition> playeranimationrework$currentAnimations = List.of();

	@Override
	public @NotNull List<AnimationDefinition> playeranimationrework$getCurrentAnimations() {
		return playeranimationrework$currentAnimations;
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void playeranimationrework$tickAnimationStateMachine() {
		@NotNull var animationIdentifier = new Identifier(MOD_ID, "idle");
		@SuppressWarnings("unchecked") var playerAnimationLayer =
				(ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayerEntity) (Object) this).get(animationIdentifier);
		playerAnimationLayer.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(animationIdentifier)));
	}
}
