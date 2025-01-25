package com.steveplays.playeranimationrework.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
import com.steveplays.playeranimationrework.client.util.PlayerAnimationUtil;
import com.steveplays.playeranimationrework.client.util.PlayerAnimationUtil.BodyParts;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public class EntityMixin {
	@Shadow public float fallDistance;

	@Inject(method = "fall", at = @At(value = "HEAD"))
	private void playeranimationrework$invokeLandEvent(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci) {
		if (!(((Entity) (Object) this) instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		if (!onGround || fallDistance < 0.75f) {
			return;
		}


		if (fallDistance < 5f) {
			PARPlayerEvents.LAND_SHORT_DISTANCE.invoker().onExecute(clientPlayer);
		} else {
			PARPlayerEvents.LAND_LONG_DISTANCE.invoker().onExecute(clientPlayer);
		}
	}

	@Inject(method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z", at = @At(value = "HEAD"))
	private void playeranimationrework$disableLegAnimationsWhenStartingToRide(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
		if (!(((Entity) (Object) this) instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		PlayerAnimationUtil.toggleBodyPartAnimations(clientPlayer, BodyParts.RIGHT_ARM, false);
		PlayerAnimationUtil.toggleBodyPartAnimations(clientPlayer, BodyParts.LEFT_ARM, false);
		PlayerAnimationUtil.toggleBodyPartAnimations(clientPlayer, BodyParts.RIGHT_LEG, false);
		PlayerAnimationUtil.toggleBodyPartAnimations(clientPlayer, BodyParts.LEFT_LEG, false);
	}
}
