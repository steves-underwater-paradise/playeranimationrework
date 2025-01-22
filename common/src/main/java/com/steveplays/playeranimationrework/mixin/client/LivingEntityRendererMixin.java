package com.steveplays.playeranimationrework.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
	@ModifyExpressionValue(
			method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isAlive()Z")
	)
	private boolean playeranimationrework$preventLimbAnimations(boolean original, @Local(argsOnly = true) @NotNull LivingEntity livingEntity) {
		if (livingEntity.isPlayer()) {
			return false;
		}

		return original;
	}
}
