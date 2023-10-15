package kelvin285.betteranimations.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import kelvin285.betteranimations.AnimationData;
import kelvin285.betteranimations.AnimationPriority;
import kelvin285.betteranimations.BetterAnimations;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class SneakAnimationCheck implements AnimationCheck {

    private static final String IDLE_ANIMATION_NAME = "sneak_idle";
    private static final String WALK_ANIMATION_NAME = "sneak_walk";

    private boolean shouldPlay = false;
    private String selectedAnimationName;
    private float lastBodyYaw;

    @Override
    public void tick(AbstractClientPlayerEntity player) {
        if(!player.isInSneakingPose()) {
            return;
        }

        this.shouldPlay = true;

        boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;
        float bodyYawDelta = player.getBodyYaw() - this.lastBodyYaw;

        if(isMoving || Math.abs(bodyYawDelta) > 3) {
            selectedAnimationName = WALK_ANIMATION_NAME;
        } else {
            selectedAnimationName = IDLE_ANIMATION_NAME;
        }

        this.lastBodyYaw = player.getBodyYaw();
    }

    @Override
    public AnimationData getAnimationData() {
        KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(
                new Identifier(BetterAnimations.MOD_ID, this.selectedAnimationName)
        );

        return new AnimationData(animation, 1.0f, 5);
    }

    @Override
    public AnimationPriority getPriority() {
        return AnimationPriority.SNEAK;
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
