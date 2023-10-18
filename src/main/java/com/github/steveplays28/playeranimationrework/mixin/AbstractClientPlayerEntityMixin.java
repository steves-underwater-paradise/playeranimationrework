package com.github.steveplays28.playeranimationrework.mixin;

import com.github.steveplays28.playeranimationrework.animation.check.*;
import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import com.github.steveplays28.playeranimationrework.animation.AnimationCheckRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
	@Unique
	private final ModifierLayer<IAnimation> modAnimationContainer = new ModifierLayer<>();
	@Unique
	private final AnimationCheckRegistry checkRegistry = new AnimationCheckRegistry();

	public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void init(ClientWorld world, GameProfile profile, CallbackInfo info) {
		PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(0, modAnimationContainer);

		checkRegistry.registerAnimationCheck(new BoatAnimationCheck());
		checkRegistry.registerAnimationCheck(new ClimbingAnimationCheck());
		checkRegistry.registerAnimationCheck(new CrawlAnimationCheck());
		checkRegistry.registerAnimationCheck(new EatingAnimationCheck());
		checkRegistry.registerAnimationCheck(new EdgeStandingAnimationCheck());
		checkRegistry.registerAnimationCheck(new ElytraAnimationCheck());
		checkRegistry.registerAnimationCheck(new FallAnimationCheck());
		checkRegistry.registerAnimationCheck(new FenceWalkAnimationCheck());
		checkRegistry.registerAnimationCheck(new FlintAndSteelAnimationCheck());
		checkRegistry.registerAnimationCheck(new JumpAnimationCheck());
		checkRegistry.registerAnimationCheck(new PunchAnimationCheck());
		checkRegistry.registerAnimationCheck(new SneakAnimationCheck());
		checkRegistry.registerAnimationCheck(new SprintAnimationCheck());
		checkRegistry.registerAnimationCheck(new SwimAnimationCheck());
		checkRegistry.registerAnimationCheck(new TurnAnimationCheck());
		checkRegistry.registerAnimationCheck(new WalkAnimationCheck());
	}

	@Override
	public void tick() {
		super.tick();
		checkRegistry.invokeTick((AbstractClientPlayerEntity) (Object) this);

		AnimationCheck animationCheck = checkRegistry.getMostSuitableAnimation();
		if (animationCheck != null) {
			var animationData = animationCheck.getAnimationData();

			if (animationData == null && !checkRegistry.animationSameAsPreviousOne()) {
				modAnimationContainer.setAnimation(null);
			} else if (animationData != null) {
				// TODO: Disable arm animations when using an item with its own third-person animation
				animationData.setAnimation(modAnimationContainer);
				animationCheck.onPlay((AbstractClientPlayerEntity) (Object) this, animationCheck.getSelectedAnimationName());
			}
		}

		checkRegistry.invokeCleanup();
	}

	@Override
	public void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
		super.fall(heightDifference, onGround, state, landedPosition);
		checkRegistry.invokeFall((AbstractClientPlayerEntity) (Object) this, heightDifference, onGround, state, landedPosition);
	}

	@Override
	public void jump() {
		super.jump();
		checkRegistry.invokeJump((AbstractClientPlayerEntity) (Object) this);
	}

	@Override
	public void swingHand(Hand hand) {
		super.swingHand(hand);
		checkRegistry.invokeSwingHand((AbstractClientPlayerEntity) (Object) this, hand);
	}
}
