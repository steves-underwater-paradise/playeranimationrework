package kelvin285.betteranimations.checks;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import kelvin285.betteranimations.AnimationData;
import kelvin285.betteranimations.AnimationPriority;
import kelvin285.betteranimations.BetterAnimations;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.util.Identifier;

public class FlintAndSteelAnimationCheck implements AnimationCheck {
    private static final String ANIMATION_NAME = "flint_and_steel";
    private static final String SNEAK_ANIMATION_NAME = "flint_and_steel_sneak";

    private boolean shouldPlay = false;
    private String selectedAnimationName;

    @Override
    public void tick(AbstractClientPlayerEntity player) {
        if(!player.isUsingItem() || !(player.getActiveItem().getItem() instanceof FlintAndSteelItem)) {
            return;
        }

        this.shouldPlay = true;

        if(player.isSneaking()) {
            this.selectedAnimationName = SNEAK_ANIMATION_NAME;
        } else {
            this.selectedAnimationName = ANIMATION_NAME;
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
        return AnimationPriority.FLINT_AND_STEEL;
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
