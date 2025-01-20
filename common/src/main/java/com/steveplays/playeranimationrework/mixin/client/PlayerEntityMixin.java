package com.steveplays.playeranimationrework.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Unique private boolean playeranimationrework$isIdle = false;
	@Unique private boolean playeranimationrework$isWalking = false;
	@Unique private boolean playeranimationrework$isRunning = false;
	// TODO: Fix fence event stop handlers not being called when going off a fence
	@Unique private boolean playeranimationrework$isFenceIdle = false;
	@Unique private boolean playeranimationrework$isFenceWalking = false;
	@Unique private boolean playeranimationrework$isFenceRunning = false;

	public PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "jump", at = @At(value = "TAIL"))
	private void playeranimationrework$invokeJumpEvent(CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity)) {
			return;
		}

		PARPlayerEvents.JUMP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void playeranimationrework$invokePlayerStopMovementEvents(CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity)) {
			return;
		}

		if (MathHelper.approximatelyEquals(this.getVelocity().horizontalLength(), 0d)) {
			if (playeranimationrework$isRunning && !this.isSprinting()) {
				PARPlayerEvents.RUN_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
				playeranimationrework$isRunning = false;
			}
			if (playeranimationrework$isWalking) {
				PARPlayerEvents.WALK_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
				playeranimationrework$isWalking = false;
			}
			if (playeranimationrework$isFenceRunning && !this.isSprinting()) {
				PARPlayerEvents.FENCE_RUN_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
				playeranimationrework$isFenceRunning = false;
			}
			if (playeranimationrework$isFenceWalking && !this.isSprinting()) {
				PARPlayerEvents.FENCE_WALK_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
				playeranimationrework$isFenceWalking = false;
			}

			if (playeranimationrework$isOnFence()) {
				if (!playeranimationrework$isFenceIdle) {
					PARPlayerEvents.FENCE_IDLE_START.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isFenceIdle = true;
				}
			} else if (!playeranimationrework$isIdle) {
				PARPlayerEvents.IDLE_START.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
				playeranimationrework$isIdle = true;
			}
		}
	}

	@Inject(method = "travel", at = @At(value = "TAIL"))
	private void playeranimationrework$invokePlayerStartMovementEvents(Vec3d movementInput, CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity)) {
			return;
		}

		if (!MathHelper.approximatelyEquals(this.getVelocity().horizontalLength(), 0d)) {
			if (!playeranimationrework$isRunning && this.isSprinting()) {
				if (playeranimationrework$isOnFence()) {
					PARPlayerEvents.FENCE_RUN_START.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isFenceRunning = true;
				} else {
					PARPlayerEvents.RUN_START.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isRunning = true;
				}

				if (playeranimationrework$isWalking) {
					PARPlayerEvents.WALK_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isWalking = false;
				}

				if (playeranimationrework$isOnFence()) {
					if (playeranimationrework$isFenceIdle) {
						PARPlayerEvents.FENCE_IDLE_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
						playeranimationrework$isFenceIdle = false;
					}
				} else if (playeranimationrework$isIdle) {
					PARPlayerEvents.IDLE_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isIdle = false;
				}
			} else if (!playeranimationrework$isWalking) {
				if (playeranimationrework$isOnFence()) {
					PARPlayerEvents.FENCE_WALK_START.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isFenceWalking = true;
				} else {
					PARPlayerEvents.WALK_START.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isWalking = true;
				}

				if (playeranimationrework$isRunning) {
					PARPlayerEvents.RUN_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isRunning = false;
				}

				if (playeranimationrework$isOnFence()) {
					if (playeranimationrework$isFenceIdle) {
						PARPlayerEvents.FENCE_IDLE_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
						playeranimationrework$isFenceIdle = false;
					}
				} else if (playeranimationrework$isIdle) {
					PARPlayerEvents.IDLE_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isIdle = false;
				}
			} else {
				if (playeranimationrework$isOnFence()) {
					if (playeranimationrework$isFenceIdle) {
						PARPlayerEvents.FENCE_IDLE_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
						playeranimationrework$isFenceIdle = false;
					}
				} else if (playeranimationrework$isIdle) {
					PARPlayerEvents.IDLE_STOP.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
					playeranimationrework$isIdle = false;
				}
			}
		}
	}

	@Unique
	private boolean playeranimationrework$isOnFence() {
		return this.getWorld().getBlockState(this.getBlockPos().down()).isIn(BlockTags.FENCES);
	}
}
