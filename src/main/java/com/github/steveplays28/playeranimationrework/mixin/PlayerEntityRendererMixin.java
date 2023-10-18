package com.github.steveplays28.playeranimationrework.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.steveplays28.playeranimationrework.client.PlayerAnimationReworkClient.REAL_CAMERA_MOD_ID;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	// TODO: Use better way to get tick delta
	@Unique
	private final float tickDelta = 0.05f;
	@Unique
	private float leanAmount = 0;
	@Unique
	private float leanMultiplier = 1;
	@Unique
	private float realLeanMultiplier = 1;
	@Unique
	private float turnDelta = 0;

	@Shadow
	protected abstract void setModelPose(AbstractClientPlayerEntity player);

	public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", cancellable = true)
	public void render(AbstractClientPlayerEntity player, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (!MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() && player == MinecraftClient.getInstance().player || FabricLoader.getInstance().isModLoaded(
				REAL_CAMERA_MOD_ID)) {
			return;
		}

		matrixStack.push();

		float leanX = (float) player.getVelocity().z;
		float leanZ = -(float) player.getVelocity().x;
		float bodyYawDelta = player.getYaw() - player.prevYaw;
		boolean isWalking = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

		if (turnDelta != 0) {
			leanAmount = MathHelper.lerp(tickDelta * 4, leanAmount, bodyYawDelta);
		} else {
			leanAmount = MathHelper.lerp(tickDelta * 4, leanAmount, 0);
		}
		leanMultiplier = MathHelper.lerp(tickDelta * 8, leanMultiplier, realLeanMultiplier);

		if (realLeanMultiplier < 1) {
			realLeanMultiplier += 0.1f;
		} else {
			realLeanMultiplier = 1;
		}

		if (Math.abs(bodyYawDelta) > 3 && !isWalking) {
			turnDelta = Math.signum(bodyYawDelta);
		} else {
			turnDelta = MathHelper.lerp(tickDelta * 16, turnDelta, 0);
			if (Math.abs(turnDelta) < 0.01f) {
				turnDelta = 0;
			}
		}

		float yaw = (float) Math.toRadians(player.bodyYaw + 90);
		leanX += Math.cos(yaw) * leanAmount * 0.01f;
		leanZ += Math.sin(yaw) * leanAmount * 0.01f;

		leanX *= leanMultiplier;
		leanZ *= leanMultiplier;

		if (!player.isFallFlying()) {
			Quaternionf quat = new Quaternionf();
			quat = new Matrix4f().rotate(leanX, new Vector3f(1, 0, 0)).rotate(leanZ, new Vector3f(0, 0, 1)).getNormalizedRotation(quat);

			matrixStack.multiply(quat);
			this.setModelPose(player);
		}

		super.render(player, f, g, matrixStack, vertexConsumerProvider, i);
		matrixStack.pop();

		ci.cancel();
	}
}
