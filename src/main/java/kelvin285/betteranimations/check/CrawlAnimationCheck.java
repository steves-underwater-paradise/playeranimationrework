package kelvin285.betteranimations.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import kelvin285.betteranimations.AnimationData;
import kelvin285.betteranimations.AnimationPriority;
import kelvin285.betteranimations.BetterAnimations;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class CrawlAnimationCheck implements AnimationCheck {
    private static final String ANIMATION_NAME = "crawling";
    private static final String IDLE_ANIMATION_NAME = "crawl_idle";

    private boolean shouldPlay = false;
    private String selectedAnimationName;

    @Override
    public void tick(AbstractClientPlayerEntity player) {
        if(!player.isCrawling()) {
            return;
        }

        this.shouldPlay = true;

        boolean isWalking = Math.abs(player.getX() - player.prevX) > 0 || Math.abs(player.getZ() - player.prevZ) > 0;

        if(isWalking) {
            this.selectedAnimationName = ANIMATION_NAME;
        } else {
            this.selectedAnimationName = IDLE_ANIMATION_NAME;
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
        return AnimationPriority.CRAWLING;
    }

    @Override
    public boolean getShouldPlay() {
        return this.shouldPlay;
    }

    @Override
    public void cleanup() {
        this.selectedAnimationName = null;
        this.shouldPlay = false;
    }
}
