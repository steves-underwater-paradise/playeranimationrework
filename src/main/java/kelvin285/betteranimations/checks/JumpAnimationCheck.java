package kelvin285.betteranimations.checks;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import kelvin285.betteranimations.AnimationData;
import kelvin285.betteranimations.AnimationPriority;
import kelvin285.betteranimations.BetterAnimations;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class JumpAnimationCheck implements AnimationCheck {
    private static final String[] ANIMATION_NAMES = new String[]{"jump_first", "jump_second"};

    // TODO: Make correct jumpIndex change
    private boolean isJumpIndexOdd = false;
    private boolean shouldPlay = false;

    @Override
    public void jump(AbstractClientPlayerEntity player) {
        boolean isMoving = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

        this.shouldPlay = !isMoving;
    }

    @Override
    public AnimationData getAnimationData() {
        KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(
                new Identifier(BetterAnimations.MOD_ID, ANIMATION_NAMES[isJumpIndexOdd ? 1 : 0])
        );

        return new AnimationData(animation, 1.0f, 5);
    }

    @Override
    public AnimationPriority getPriority() {
        return AnimationPriority.JUMP;
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
