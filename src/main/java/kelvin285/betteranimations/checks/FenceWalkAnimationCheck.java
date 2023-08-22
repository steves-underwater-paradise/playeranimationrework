package kelvin285.betteranimations.checks;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import kelvin285.betteranimations.AnimationData;
import kelvin285.betteranimations.AnimationPriority;
import kelvin285.betteranimations.BetterAnimations;
import net.minecraft.block.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class FenceWalkAnimationCheck implements AnimationCheck {

    private static final String IDLE_ANIMATION_NAME = "fence_idle";
    private static final String WALK_ANIMATION_NAME = "fence_walk";

    private boolean shouldPlay = false;
    private String selectedAnimationName;

    @Override
    public void tick(AbstractClientPlayerEntity player) {
        Block standingBlock = player.getWorld().getBlockState(player.getBlockPos().down()).getBlock();
        boolean onThinBlock = (standingBlock instanceof FenceBlock || standingBlock instanceof WallBlock ||
                standingBlock instanceof PaneBlock) && player.isOnGround();

        if(!onThinBlock) {
            return;
        }

        this.shouldPlay = true;

        boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

        if(isMoving) {
            selectedAnimationName = WALK_ANIMATION_NAME;
        } else {
            selectedAnimationName = IDLE_ANIMATION_NAME;
        }
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
        return AnimationPriority.FENCE_WALK;
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
