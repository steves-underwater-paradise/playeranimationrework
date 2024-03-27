package com.github.steveplays28.playeranimationrework.mixin;

import com.github.steveplays28.playeranimationrework.animation.Animation;
import com.github.steveplays28.playeranimationrework.animation.AnimationRegistry;
import com.github.steveplays28.playeranimationrework.animation.impl.*;
import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
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
	private final AnimationRegistry animationRegistry = new AnimationRegistry();

	public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void init(ClientWorld world, GameProfile profile, CallbackInfo info) {
		PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(0, modAnimationContainer);

		// TODO: Refactor into an API
		animationRegistry.registerAnimations(new BoatAnimation(), new ClimbingAnimation(), new CrawlAnimation(), new EatingAnimation(),
				new BalanceLossAnimation(), new ElytraAnimation(), new FallAnimation(), new FenceWalkAnimation(),
				new FlintAndSteelAnimation(), new JumpAnimation(), new PunchAnimation(), new SneakAnimation(), new SprintAnimation(),
				new SwimAnimation(), new TurnAnimation(), new WalkAnimation(), new WorkbenchUseAnimation()
		);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.isPartOfGame()) {
			return;
		}

		animationRegistry.invokeTick((AbstractClientPlayerEntity) (Object) this);

		Animation animation = animationRegistry.getMostSuitableAnimation();
		if (animation != null) {
			var animationData = animation.getAnimationData();

			if (animationData == null && !animationRegistry.isAnimationEqualToPreviousAnimation()) {
				modAnimationContainer.setAnimation(null);
			} else if (animationData != null) {
				animationData.setAnimation(modAnimationContainer);
				animation.onPlay((AbstractClientPlayerEntity) (Object) this, animation.getSelectedAnimationName());
			}
		}

		animationRegistry.invokeCleanup();
	}

	@Override
	public void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
		super.fall(heightDifference, onGround, state, landedPosition);
		if (!this.isPartOfGame()) {
			return;
		}

		animationRegistry.invokeFall((AbstractClientPlayerEntity) (Object) this, heightDifference, onGround, state, landedPosition);
	}

	@Override
	public void jump() {
		super.jump();
		if (!this.isPartOfGame()) {
			return;
		}

		animationRegistry.invokeJump((AbstractClientPlayerEntity) (Object) this);
	}

	@Override
	public void swingHand(Hand hand) {
		super.swingHand(hand);
		if (!this.isPartOfGame()) {
			return;
		}

		animationRegistry.invokeSwingHand((AbstractClientPlayerEntity) (Object) this, hand);
	}
}
