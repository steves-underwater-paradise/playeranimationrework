package com.github.steveplays28.playeranimationrework.mixin;

import com.github.steveplays28.playeranimationrework.client.animation.state.PARStateMachine;
import com.github.steveplays28.playeranimationrework.client.extension.PlayerEntityExtension;
import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: Replace this mixin with Fabric API events
@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity implements PlayerEntityExtension {
	@Unique
	private ModifierLayer<IAnimation> playerAnimationRework$modifierLayer;

	@Unique
	private PARStateMachine playerAnimationRework$stateMachine;

	public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void playerAnimationRework$constructorInject(ClientWorld world, GameProfile profile, CallbackInfo info) {
		playerAnimationRework$modifierLayer = new ModifierLayer<>();
		playerAnimationRework$stateMachine = new PARStateMachine();

		PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(
				0, playerAnimationRework$modifierLayer);
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void playerAnimationRework$tickInject(CallbackInfo ci) {
		var animation = playerAnimationRework$modifierLayer.getAnimation();
		if (animation == null) {
			return;
		}

		if (animation.isActive() && !this.isPartOfGame()) {
			playerAnimationRework$modifierLayer.setAnimation(null);
		}
	}

	@Override
	public ModifierLayer<IAnimation> playerAnimationRework$getModifierLayer() {
		return playerAnimationRework$modifierLayer;
	}

	@Override
	public PARStateMachine playerAnimationRework$getStateMachine() {
		return playerAnimationRework$stateMachine;
	}
}
