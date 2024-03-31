package com.github.steveplays28.playeranimationrework.mixin;

import com.github.steveplays28.playeranimationrework.client.animation.state.PARStateBuilder;
import com.github.steveplays28.playeranimationrework.client.extension.PlayerEntityExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot var1);

	@Unique
	private boolean playerAnimationRework$wasInSneakingPoseLastTick = false;
	@Unique
	private ItemStack playerAnimationRework$mainHandItemStackLastTick = ItemStack.EMPTY;
	@Unique
	private ItemStack playerAnimationRework$offHandItemStackLastTick = ItemStack.EMPTY;

	public PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void playerAnimationRework$resetMovementStateIfNeeded(CallbackInfo ci) {
		if (!this.getWorld().isClient()) {
			return;
		}

		if (MathHelper.approximatelyEquals(this.getVelocity().horizontalLength(), 0d)) {
			var stateMachine = ((PlayerEntityExtension) this).playerAnimationRework$getStateMachine();
			var playerAnimationModifierLayer = ((PlayerEntityExtension) this).playerAnimationRework$getModifierLayer();
			stateMachine.setState(
					playerAnimationModifierLayer,
					new PARStateBuilder(stateMachine.getState()).setIsWalking(false).setIsSprinting(this.isSprinting()).build()
			);
		}

		playerAnimationRework$updateMainHandItemStack();
		playerAnimationRework$updateOffHandItemStack();
	}

	@Inject(method = "travel", at = @At(value = "TAIL"))
	private void playerAnimationRework$updateMovementState(Vec3d movementInput, CallbackInfo ci) {
		if (!this.getWorld().isClient() || MathHelper.approximatelyEquals(this.getVelocity().horizontalLength(), 0d)) {
			return;
		}

		var stateMachine = ((PlayerEntityExtension) this).playerAnimationRework$getStateMachine();
		var playerAnimationModifierLayer = ((PlayerEntityExtension) this).playerAnimationRework$getModifierLayer();
		stateMachine.setState(
				playerAnimationModifierLayer,
				new PARStateBuilder(stateMachine.getState()).setIsWalking(true).setIsSprinting(this.isSprinting()).build()
		);
	}

	@Inject(method = "tickMovement", at = @At(value = "TAIL"))
	private void playerAnimationRework$updateSneakState(CallbackInfo ci) {
		if (!this.getWorld().isClient() || playerAnimationRework$wasInSneakingPoseLastTick == isInSneakingPose()) {
			return;
		}

		var stateMachine = ((PlayerEntityExtension) this).playerAnimationRework$getStateMachine();
		var playerAnimationModifierLayer = ((PlayerEntityExtension) this).playerAnimationRework$getModifierLayer();
		stateMachine.setState(
				playerAnimationModifierLayer,
				new PARStateBuilder(stateMachine.getState()).setIsSneaking(this.isInSneakingPose()).build()
		);

		playerAnimationRework$wasInSneakingPoseLastTick = isInSneakingPose();
	}

	@Unique
	private void playerAnimationRework$updateMainHandItemStack() {
		var mainHandItemStack = this.getMainHandStack();
		if (mainHandItemStack.equals(playerAnimationRework$mainHandItemStackLastTick)) {
			return;
		}

		var stateMachine = ((PlayerEntityExtension) this).playerAnimationRework$getStateMachine();
		var playerAnimationModifierLayer = ((PlayerEntityExtension) this).playerAnimationRework$getModifierLayer();
		stateMachine.setState(
				playerAnimationModifierLayer,
				new PARStateBuilder(stateMachine.getState()).setEquippedMainHandItemStack(mainHandItemStack).build()
		);
		if (mainHandItemStack.isEmpty()) {
			stateMachine.invokePlayerUnequipMainHandItemStack(playerAnimationModifierLayer);
		} else {
			stateMachine.invokePlayerEquipMainHandItemStack(playerAnimationModifierLayer);
		}

		playerAnimationRework$mainHandItemStackLastTick = mainHandItemStack;
	}

	@Unique
	private void playerAnimationRework$updateOffHandItemStack() {
		var offHandItemStack = this.getOffHandStack();
		if (offHandItemStack.equals(playerAnimationRework$offHandItemStackLastTick)) {
			return;
		}

		var stateMachine = ((PlayerEntityExtension) this).playerAnimationRework$getStateMachine();
		var playerAnimationModifierLayer = ((PlayerEntityExtension) this).playerAnimationRework$getModifierLayer();
		stateMachine.setState(
				playerAnimationModifierLayer,
				new PARStateBuilder(stateMachine.getState()).setEquippedOffHandItemStack(offHandItemStack).build()
		);
		if (offHandItemStack.isEmpty()) {
			stateMachine.invokePlayerUnequipOffHandItemStack(playerAnimationModifierLayer);
		} else {
			stateMachine.invokePlayerEquipOffHandItemStack(playerAnimationModifierLayer);
		}

		playerAnimationRework$offHandItemStackLastTick = offHandItemStack;
	}
}
