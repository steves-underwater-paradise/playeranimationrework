package com.steveplays.playeranimationrework.mixin.client;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.authlib.GameProfile;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
import com.steveplays.playeranimationrework.client.util.PlayerAnimationUtil;
import com.steveplays.playeranimationrework.tag.PARTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow private ItemStack selectedItem;

	@Unique private boolean playeranimationrework$isIdle = false;
	@Unique private boolean playeranimationrework$isWalking = false;
	@Unique private boolean playeranimationrework$isRunning = false;
	@Unique private boolean playeranimationrework$isFenceIdle = false;
	@Unique private boolean playeranimationrework$isFenceWalking = false;
	@Unique private boolean playeranimationrework$isFenceRunning = false;

	public PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void playeranimationrework$registerEventHandlers(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity)) {
			return;
		}

		// Register movement event handlers
		PARPlayerEvents.IDLE_START.register(clientPlayer -> playeranimationrework$isIdle = true);
		PARPlayerEvents.IDLE_STOP.register(clientPlayer -> playeranimationrework$isIdle = false);
		PARPlayerEvents.WALK_START.register(clientPlayer -> playeranimationrework$isWalking = true);
		PARPlayerEvents.WALK_STOP.register(clientPlayer -> playeranimationrework$isWalking = false);
		PARPlayerEvents.RUN_START.register(clientPlayer -> playeranimationrework$isRunning = true);
		PARPlayerEvents.RUN_STOP.register(clientPlayer -> playeranimationrework$isRunning = false);
		// Register fence movement event handlers
		PARPlayerEvents.FENCE_IDLE_START.register(clientPlayer -> playeranimationrework$isFenceIdle = true);
		PARPlayerEvents.FENCE_IDLE_STOP.register(clientPlayer -> playeranimationrework$isFenceIdle = false);
		PARPlayerEvents.FENCE_WALK_START.register(clientPlayer -> playeranimationrework$isFenceWalking = true);
		PARPlayerEvents.FENCE_WALK_STOP.register(clientPlayer -> playeranimationrework$isFenceWalking = false);
		PARPlayerEvents.FENCE_RUN_START.register(clientPlayer -> playeranimationrework$isFenceRunning = true);
		PARPlayerEvents.FENCE_RUN_STOP.register(clientPlayer -> playeranimationrework$isFenceRunning = false);
	}

	@Inject(method = "jump", at = @At(value = "TAIL"))
	private void playeranimationrework$invokeJumpEvent(CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		PARPlayerEvents.JUMP.invoker().onExecute(clientPlayer);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;", shift = Shift.BEFORE))
	private void playeranimationrework$invokeItemSwitchEvents(CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		// TODO: Add left arm support
		@NotNull var mainHandStack = clientPlayer.getMainHandStack();
		if (mainHandStack.isIn(PARTags.IS_ON_BACK) || this.selectedItem.isIn(PARTags.IS_ON_BACK)) {
			PARPlayerEvents.SWITCH_TO_ITEM_ON_BACK_RIGHT_ARM.invoker().onExecute(clientPlayer);
		} else {
			PARPlayerEvents.SWITCH_TO_ITEM_IN_POCKET_RIGHT_ARM.invoker().onExecute(clientPlayer);
		}
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void playeranimationrework$invokePlayerStopMovementEvents(CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		// Check if the player is idle
		if (MathHelper.approximatelyEquals(this.getVelocity().horizontalLength(), 0d)) {
			// Handle the player going idle
			if (!playeranimationrework$isIdle) {
				PARPlayerEvents.IDLE_START.invoker().onExecute(clientPlayer);
			}
			if (!playeranimationrework$isFenceIdle && playeranimationrework$isOnFence()) {
				PARPlayerEvents.FENCE_IDLE_START.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isWalking) {
				PARPlayerEvents.WALK_STOP.invoker().onExecute(clientPlayer);
			}
			if (playeranimationrework$isRunning) {
				PARPlayerEvents.RUN_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isFenceWalking) {
				PARPlayerEvents.FENCE_WALK_STOP.invoker().onExecute(clientPlayer);
			}
			if (playeranimationrework$isFenceRunning) {
				PARPlayerEvents.FENCE_RUN_STOP.invoker().onExecute(clientPlayer);
			}
		} else {
			// Handle the player unidling
			if (playeranimationrework$isIdle) {
				PARPlayerEvents.IDLE_STOP.invoker().onExecute(clientPlayer);
			}
			if (playeranimationrework$isFenceIdle) {
				PARPlayerEvents.FENCE_IDLE_STOP.invoker().onExecute(clientPlayer);
			}
		}
	}

	@Inject(method = "travel", at = @At(value = "TAIL"))
	private void playeranimationrework$invokePlayerStartMovementEvents(Vec3d movementInput, CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity clientPlayer) || MathHelper.approximatelyEquals(this.getVelocity().horizontalLength(), 0d)) {
			return;
		}

		if (this.isSprinting()) {
			// Handle the player starting sprinting
			if (!playeranimationrework$isRunning) {
				PARPlayerEvents.RUN_START.invoker().onExecute(clientPlayer);
			}

			if (!playeranimationrework$isFenceRunning && playeranimationrework$isOnFence()) {
				PARPlayerEvents.FENCE_RUN_START.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isWalking) {
				PARPlayerEvents.WALK_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isFenceWalking) {
				PARPlayerEvents.FENCE_WALK_STOP.invoker().onExecute(clientPlayer);
			}
		} else {
			// Handle the player stopping sprinting without fully stopping moving
			if (!playeranimationrework$isWalking) {
				PARPlayerEvents.WALK_START.invoker().onExecute(clientPlayer);
			}

			if (!playeranimationrework$isFenceWalking && playeranimationrework$isOnFence()) {
				PARPlayerEvents.FENCE_WALK_START.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isRunning) {
				PARPlayerEvents.RUN_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isFenceRunning && playeranimationrework$isOnFence()) {
				PARPlayerEvents.FENCE_RUN_STOP.invoker().onExecute(clientPlayer);
			}
		}

		// Check if the player has fallen off a fence
		if (!playeranimationrework$isOnFence()) {
			// Handle the player falling off a fence
			if (playeranimationrework$isFenceWalking) {
				PARPlayerEvents.FENCE_WALK_STOP.invoker().onExecute(clientPlayer);
			}
			if (playeranimationrework$isFenceRunning) {
				PARPlayerEvents.FENCE_RUN_STOP.invoker().onExecute(clientPlayer);
			}
			if (playeranimationrework$isFenceIdle) {
				PARPlayerEvents.FENCE_IDLE_STOP.invoker().onExecute(clientPlayer);
			}
		}
	}

	@Inject(method = "onDeath", at = @At(value = "TAIL"))
	private void playeranimationrework$stopAllAnimationsOnDeath(DamageSource damageSource, CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		PlayerAnimationUtil.stopAllAnimations(clientPlayer);
	}

	@Unique
	private boolean playeranimationrework$isOnFence() {
		return this.getWorld().getBlockState(this.getBlockPos().down()).isIn(BlockTags.FENCES);
	}
}
