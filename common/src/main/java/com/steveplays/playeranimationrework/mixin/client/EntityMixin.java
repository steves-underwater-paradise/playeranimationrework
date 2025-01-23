package com.steveplays.playeranimationrework.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
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
}
