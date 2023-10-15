package kelvin285.betteranimations.check;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import kelvin285.betteranimations.AnimationData;
import kelvin285.betteranimations.AnimationPriority;
import kelvin285.betteranimations.BetterAnimations;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;

public class BoatAnimationCheck implements AnimationCheck {
    private static final String IDLE_ANIMATION_NAME = "boat_idle";
    private static final String FORWARD_ANIMATION_NAME = "boat_forward";
    private static final String LEFT_PADDLE_ANIMATION_NAME = "boat_left_paddle";
    private static final String RIGHT_PADDLE_ANIMATION_NAME = "boat_right_paddle";

    private boolean shouldPlay = false;
    private String selectedAnimationName;

    @Override
    public void tick(AbstractClientPlayerEntity player) {
        Entity vehicle = player.getControllingVehicle();

        if(!(vehicle instanceof BoatEntity)) {
            return;
        }

        this.shouldPlay = true;

        boolean leftPaddleMoving = ((BoatEntity) vehicle).isPaddleMoving(0);
        boolean rightPaddleMoving = ((BoatEntity) vehicle).isPaddleMoving(1);

        if(leftPaddleMoving && rightPaddleMoving) {
            this.selectedAnimationName = FORWARD_ANIMATION_NAME;
        } else if(leftPaddleMoving) {
            this.selectedAnimationName = LEFT_PADDLE_ANIMATION_NAME;
        } else if(rightPaddleMoving) {
            this.selectedAnimationName = RIGHT_PADDLE_ANIMATION_NAME;
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
        return AnimationPriority.BOAT;
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
