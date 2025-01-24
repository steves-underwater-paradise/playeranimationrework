package com.steveplays.playeranimationrework.mixin.client;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
import com.steveplays.playeranimationrework.client.util.PlayerAnimationUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stat.StatHandler;
import net.minecraft.world.GameMode;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Unique private static int playeranimationrework$TICKS_BETWEEN_HAND_SWINGS = 5;

	@Unique private int playeranimationrework$ticksSinceLastHandSwing;

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void playeranimationrework$registerEventHandlers(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook,
			boolean lastSneaking, boolean lastSprinting, CallbackInfo ci) {
		PARPlayerEvents.PUNCH.register(clientPlayer -> playeranimationrework$ticksSinceLastHandSwing = 0);
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void playeranimationrework$increaseTicksSinceLastHandSwing(CallbackInfo ci) {
		playeranimationrework$ticksSinceLastHandSwing++;
	}

	@Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At(value = "TAIL"))
	private void playeranimationrework$invokePunchEvent(CallbackInfo ci) {
		if (playeranimationrework$ticksSinceLastHandSwing < playeranimationrework$TICKS_BETWEEN_HAND_SWINGS) {
			return;
		}

		PARPlayerEvents.PUNCH.invoker().onExecute((ClientPlayerEntity) (Object) this);

	}

	@Inject(method = "onGameModeChanged",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", shift = At.Shift.BEFORE))
	private void playeranimationrework$stopAllAnimationsOnEnteringSpectatorMode(@NotNull GameMode gameMode, CallbackInfo ci) {
		PlayerAnimationUtil.stopAllAnimations((ClientPlayerEntity) (Object) this);
	}
}
