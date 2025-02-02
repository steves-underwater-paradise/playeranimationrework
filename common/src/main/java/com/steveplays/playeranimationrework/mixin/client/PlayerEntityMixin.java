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
import com.steveplays.playeranimationrework.client.registry.PARAnimationRegistry;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import static com.steveplays.playeranimationrework.client.util.PlayerAnimationUtil.BodyParts;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow private ItemStack selectedItem;

	// Item use states
	@Unique private boolean playeranimationrework$isEating = false;
	@Unique private boolean playeranimationrework$isDrinkingRightArm = false;
	@Unique private boolean playeranimationrework$isDrinkingLeftArm = false;
	// Movement states
	@Unique private boolean playeranimationrework$isIdle = false;
	@Unique private boolean playeranimationrework$isWalking = false;
	@Unique private boolean playeranimationrework$isRunning = false;
	@Unique private boolean playeranimationrework$isSneakIdle = false;
	@Unique private boolean playeranimationrework$isSneakWalking = false;
	@Unique private boolean playeranimationrework$isSwimmingIdle = false;
	@Unique private boolean playeranimationrework$isSwimmingSlow = false;
	@Unique private boolean playeranimationrework$isSwimmingFast = false;
	@Unique private boolean playeranimationrework$isFenceIdle = false;
	@Unique private boolean playeranimationrework$isFenceWalking = false;
	@Unique private boolean playeranimationrework$isFenceRunning = false;

	public PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void playeranimationrework$registerEventHandlers(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity thisClientPlayer)) {
			return;
		}

		// Register switch to item event handlers
		PARPlayerEvents.SWITCH_TO_ITEM_ON_BACK_RIGHT_ARM.register(this::playeranimationrework$onSwitchItem);
		PARPlayerEvents.SWITCH_TO_ITEM_IN_POCKET_RIGHT_ARM.register(this::playeranimationrework$onSwitchItem);
		// Register item use event handlers
		PARPlayerEvents.EAT_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isEating = true;
		});
		PARPlayerEvents.EAT_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isEating = false;
		});
		PARPlayerEvents.DRINK_RIGHT_ARM_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isDrinkingRightArm = true;
		});
		PARPlayerEvents.DRINK_RIGHT_ARM_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isDrinkingRightArm = false;
		});
		PARPlayerEvents.DRINK_LEFT_ARM_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isDrinkingLeftArm = true;
		});
		PARPlayerEvents.DRINK_LEFT_ARM_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isDrinkingLeftArm = false;
		});
		// Register movement event handlers
		PARPlayerEvents.IDLE_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isIdle = true;
		});
		PARPlayerEvents.IDLE_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isIdle = false;
		});
		PARPlayerEvents.WALK_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isWalking = true;
		});
		PARPlayerEvents.WALK_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isWalking = false;
		});
		PARPlayerEvents.RUN_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isRunning = true;
		});
		PARPlayerEvents.RUN_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isRunning = false;
		});
		PARPlayerEvents.SNEAK_IDLE_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSneakIdle = true;
		});
		PARPlayerEvents.SNEAK_IDLE_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSneakIdle = false;
		});
		PARPlayerEvents.SNEAK_WALK_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSneakWalking = true;
		});
		PARPlayerEvents.SNEAK_WALK_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSneakWalking = false;
		});
		PARPlayerEvents.SWIM_IDLE_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSwimmingIdle = true;
		});
		PARPlayerEvents.SWIM_IDLE_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSwimmingIdle = false;
		});
		PARPlayerEvents.SWIM_SLOW_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSwimmingSlow = true;
		});
		PARPlayerEvents.SWIM_SLOW_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSwimmingSlow = false;
		});
		PARPlayerEvents.SWIM_FAST_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSwimmingFast = true;
		});
		PARPlayerEvents.SWIM_FAST_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isSwimmingFast = false;
		});
		// Register fence movement event handlers
		PARPlayerEvents.FENCE_IDLE_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isFenceIdle = true;
		});
		PARPlayerEvents.FENCE_IDLE_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isFenceIdle = false;
		});
		PARPlayerEvents.FENCE_WALK_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isFenceWalking = true;
		});
		PARPlayerEvents.FENCE_WALK_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isFenceWalking = false;
		});
		PARPlayerEvents.FENCE_RUN_START.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isFenceRunning = true;
		});
		PARPlayerEvents.FENCE_RUN_STOP.register(clientPlayer -> {
			if (clientPlayer != thisClientPlayer) {
				return;
			}

			playeranimationrework$isFenceRunning = false;
		});
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
			if (this.isSneaking()) {
				if (!playeranimationrework$isSneakIdle) {
					PARPlayerEvents.SNEAK_IDLE_START.invoker().onExecute(clientPlayer);
				}
			} else {
				if (playeranimationrework$isSneakIdle) {
					PARPlayerEvents.SNEAK_IDLE_STOP.invoker().onExecute(clientPlayer);
				}
			}
			if (!playeranimationrework$isSwimmingIdle && playeranimationrework$isInFluid()) {
				PARPlayerEvents.SWIM_IDLE_START.invoker().onExecute(clientPlayer);
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
			if (playeranimationrework$isSneakWalking) {
				PARPlayerEvents.SNEAK_WALK_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isSwimmingSlow) {
				PARPlayerEvents.SWIM_SLOW_STOP.invoker().onExecute(clientPlayer);
			}
			if (playeranimationrework$isSwimmingFast) {
				PARPlayerEvents.SWIM_FAST_STOP.invoker().onExecute(clientPlayer);
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
			if (playeranimationrework$isSneakIdle) {
				PARPlayerEvents.SNEAK_IDLE_STOP.invoker().onExecute(clientPlayer);
			}
			if (playeranimationrework$isSwimmingIdle) {
				PARPlayerEvents.SWIM_IDLE_STOP.invoker().onExecute(clientPlayer);
			}
			if (playeranimationrework$isFenceIdle) {
				PARPlayerEvents.FENCE_IDLE_STOP.invoker().onExecute(clientPlayer);
			}

			// Check if the player stopped sneaking
			if (!this.isSneaking()) {
				// Handle the player stopping sneaking
				if (playeranimationrework$isSneakWalking) {
					PARPlayerEvents.SNEAK_WALK_STOP.invoker().onExecute(clientPlayer);
				}
			}
		}
	}

	@Inject(method = "travel", at = @At(value = "TAIL"))
	private void playeranimationrework$invokePlayerStartMovementEvents(Vec3d movementInput, CallbackInfo ci) {
		if (!(((PlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		// Check if velocity is more than zero
		if (!MathHelper.approximatelyEquals(this.getVelocity().length(), 0d)) {
			// Handle velocity being more than zero
			// Check if the player went out of a fluid
			if (!playeranimationrework$isInFluid()) {
				// Handle the player going out of a fluid
				if (playeranimationrework$isSwimmingIdle) {
					PARPlayerEvents.SWIM_IDLE_STOP.invoker().onExecute(clientPlayer);
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

		// Check if horizontal velocity is more than zero
		if (MathHelper.approximatelyEquals(this.getVelocity().horizontalLength(), 0d)) {
			return;
		}

		// Handle horizontal velocity being more than zero
		if (this.isSwimming()) {
			// Handle the player starting swimming fast
			if (!playeranimationrework$isSwimmingFast) {
				PARPlayerEvents.SWIM_FAST_START.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isRunning) {
				PARPlayerEvents.RUN_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isWalking) {
				PARPlayerEvents.WALK_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isSneakWalking) {
				PARPlayerEvents.SNEAK_WALK_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isFenceWalking) {
				PARPlayerEvents.FENCE_WALK_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isFenceRunning) {
				PARPlayerEvents.FENCE_RUN_STOP.invoker().onExecute(clientPlayer);
			}
		} else if (playeranimationrework$isInFluid()) {
			// Handle the player starting swimming slow/wading
			if (!playeranimationrework$isSwimmingSlow) {
				PARPlayerEvents.SWIM_SLOW_START.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isRunning) {
				PARPlayerEvents.RUN_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isWalking) {
				PARPlayerEvents.WALK_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isSneakWalking) {
				PARPlayerEvents.SNEAK_WALK_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isFenceWalking) {
				PARPlayerEvents.FENCE_WALK_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isFenceRunning) {
				PARPlayerEvents.FENCE_RUN_STOP.invoker().onExecute(clientPlayer);
			}
		} else if (this.isSprinting() && !this.isSneaking()) {
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
		} else if (this.isSneaking()) {
			// Handle the player starting sneak walking
			if (!playeranimationrework$isSneakWalking) {
				PARPlayerEvents.SNEAK_WALK_START.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isRunning) {
				PARPlayerEvents.RUN_STOP.invoker().onExecute(clientPlayer);
			}

			if (playeranimationrework$isFenceRunning) {
				PARPlayerEvents.FENCE_RUN_STOP.invoker().onExecute(clientPlayer);
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
		return this.getWorld().getBlockState(this.getBlockPos()).isIn(PARTags.REQUIRES_BALANCING_FULL_BLOCK_HIT_BOX)
				|| this.getWorld().getBlockState(this.getBlockPos().down()).isIn(PARTags.REQUIRES_BALANCING);
	}

	@Unique
	private boolean playeranimationrework$isInFluid() {
		return !this.getWorld().getFluidState(this.getBlockPos()).isEmpty();
	}

	@Unique
	private void playeranimationrework$onSwitchItem(@NotNull AbstractClientPlayerEntity clientPlayer) {
		if (playeranimationrework$isEating) {
			PARPlayerEvents.EAT_STOP.invoker().onExecute(clientPlayer);
		}
		if (playeranimationrework$isDrinkingRightArm) {
			PARPlayerEvents.DRINK_RIGHT_ARM_STOP.invoker().onExecute(clientPlayer);
		}
		if (playeranimationrework$isDrinkingLeftArm) {
			PARPlayerEvents.DRINK_LEFT_ARM_STOP.invoker().onExecute(clientPlayer);
		}

		playeranimationrework$toggleVanillaArmAnimations(clientPlayer);
	}

	@Unique
	private void playeranimationrework$toggleVanillaArmAnimations(@NotNull AbstractClientPlayerEntity clientPlayer) {
		@NotNull var mainHandStack = clientPlayer.getMainHandStack();
		if (mainHandStack.isIn(PARTags.USES_VANILLA_ANIMATIONS_MAIN_HAND)) {
			PlayerAnimationUtil.toggleBodyPartAnimations(clientPlayer, BodyParts.RIGHT_ARM, false);
		} else if (!PARAnimationRegistry.ENABLED_BODY_PARTS.get(BodyParts.RIGHT_ARM)) {
			PlayerAnimationUtil.toggleBodyPartAnimations(clientPlayer, BodyParts.RIGHT_ARM, true);
		}
		if (mainHandStack.isIn(PARTags.USES_VANILLA_ANIMATIONS_OFF_HAND)) {
			PlayerAnimationUtil.toggleBodyPartAnimations(clientPlayer, BodyParts.LEFT_ARM, false);
		} else if (!PARAnimationRegistry.ENABLED_BODY_PARTS.get(BodyParts.LEFT_ARM)) {
			PlayerAnimationUtil.toggleBodyPartAnimations(clientPlayer, BodyParts.LEFT_ARM, true);
		}
	}
}
