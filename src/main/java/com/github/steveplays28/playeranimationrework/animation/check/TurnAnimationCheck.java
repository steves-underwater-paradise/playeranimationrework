package com.github.steveplays28.playeranimationrework.animation.check;

import com.github.steveplays28.playeranimationrework.animation.AnimationData;
import com.github.steveplays28.playeranimationrework.animation.AnimationPriority;
import com.github.steveplays28.playeranimationrework.PlayerAnimationRework;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class TurnAnimationCheck implements AnimationCheck {
    private static final String TURN_LEFT_ANIMATION_NAME = "turn_left";
    private static final String TURN_RIGHT_ANIMATION_NAME = "turn_right";

    private boolean shouldPlay = false;
    private String selectedAnimationName;
    private float lastBodyYaw;

    @Override
    public void tick(AbstractClientPlayerEntity player) {
        boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

        if(isMoving) {
            return;
        }

        int bodyYawDelta = (int) (player.getBodyYaw() - this.lastBodyYaw);

        if(Math.abs(bodyYawDelta) > 0) {
            if(bodyYawDelta < 0) {
                this.selectedAnimationName = TURN_LEFT_ANIMATION_NAME;
            } else {
                this.selectedAnimationName = TURN_RIGHT_ANIMATION_NAME;
            }

            this.shouldPlay = true;
        }

        this.lastBodyYaw = player.getBodyYaw();
    }

    @Override
    public AnimationData getAnimationData() {
        KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(
                new Identifier(PlayerAnimationRework.MOD_ID, this.selectedAnimationName)
        );

        return new AnimationData(animation, 1.0f, 5);
    }

    @Override
    public AnimationPriority getPriority() {
        return AnimationPriority.TURN;
    }

    @Override
    public boolean getShouldPlay() {
        return this.shouldPlay;
    }

    @Override
    public void cleanup() {
        this.shouldPlay = false;
        this.selectedAnimationName = null;
    }
}
