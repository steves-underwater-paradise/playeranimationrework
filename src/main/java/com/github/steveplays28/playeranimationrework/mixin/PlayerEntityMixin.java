package com.github.steveplays28.playeranimationrework.mixin;

import com.github.steveplays28.playeranimationrework.client.animation.state.PARStateBuilder;
import com.github.steveplays28.playeranimationrework.client.extension.PlayerEntityExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
	public PlayerEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void playerAnimationRework$updateState(CallbackInfo ci) {
		if (!this.getWorld().isClient()) {
			return;
		}

		if (MathHelper.approximatelyEquals(this.getVelocity().horizontalLength(), 0d)) {
			var stateMachine = ((PlayerEntityExtension) this).playerAnimationRework$getStateMachine();
			var previousState = stateMachine.getState();
			stateMachine.setState(((PlayerEntity) (Object) this), previousState, new PARStateBuilder(previousState).setIsWalking(false).build());
		}
	}

	@Inject(method = "travel", at = @At(value = "TAIL"))
	private void playerAnimationRework$updateWalkingState(Vec3d movementInput, CallbackInfo ci) {
		if (!this.getWorld().isClient() || MathHelper.approximatelyEquals(this.getVelocity().horizontalLength(), 0d)) {
			return;
		}

		var stateMachine = ((PlayerEntityExtension) this).playerAnimationRework$getStateMachine();
		var previousState = stateMachine.getState();
		stateMachine.setState(((PlayerEntity) (Object) this), previousState, new PARStateBuilder(previousState).setIsWalking(true).build());
	}
}
