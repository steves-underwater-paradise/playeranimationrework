package com.steveplays.playeranimationrework.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateLimbs(Z)V"))
	private void playeranimationrework$preventLimbMovementAnimations(LivingEntity instance, boolean flutter, Operation<Boolean> original) {
		if (!(((LivingEntity) (Object) this) instanceof AbstractClientPlayerEntity)) {
			original.call(instance, flutter);
		}

		original.call(instance, false);
	}
}
