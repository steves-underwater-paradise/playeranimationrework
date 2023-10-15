package kelvin285.betteranimations.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import kelvin285.betteranimations.AnimationData;
import kelvin285.betteranimations.AnimationPriority;
import kelvin285.betteranimations.BetterAnimations;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class EdgeStandingAnimationCheck implements AnimationCheck {
    private static final String BALANCE_LOSS_ANIMATION_NAME = "edge_idle";

    private boolean shouldPlay = false;

    @Override
    public void tick(AbstractClientPlayerEntity player) {
        boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;
        BlockState standingBlockState = player.getWorld().getBlockState(player.getBlockPos().down());

        // standingBlockState gets the state of the block under the player by rounding the player's coordinates and
        // finding the block with those coordinates, and it doesn't have to be the block the player is standing on.
        // Therefore, when the player is standing on the edge, it will be an air block, which is not .blocksMovement()
        // (due to rounding up the coordinates)
        this.shouldPlay = !standingBlockState.blocksMovement() && player.isOnGround() && !isMoving;
    }

    @Override
    public AnimationData getAnimationData() {
        KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(
                new Identifier(BetterAnimations.MOD_ID, BALANCE_LOSS_ANIMATION_NAME)
        );

        return new AnimationData(animation, 1.0f, 5);
    }

    @Override
    public AnimationPriority getPriority() {
        return AnimationPriority.EDGE_STANDING;
    }

    @Override
    public boolean getShouldPlay() {
        return this.shouldPlay;
    }

    @Override
    public void cleanup() {
        this.shouldPlay = false;
    }
}
