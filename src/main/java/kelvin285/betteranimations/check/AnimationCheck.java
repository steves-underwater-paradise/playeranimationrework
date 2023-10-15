package kelvin285.betteranimations.check;

import kelvin285.betteranimations.AnimationData;
import kelvin285.betteranimations.AnimationPriority;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public interface AnimationCheck {
    AnimationData getAnimationData();
    AnimationPriority getPriority();
    boolean getShouldPlay();
    default void tick(AbstractClientPlayerEntity player) {}
    default void jump(AbstractClientPlayerEntity player) {}
    default void swingHand(AbstractClientPlayerEntity player, Hand hand) {}
    default void fall(AbstractClientPlayerEntity player, double heightDifference, boolean onGround, BlockState state,
                      BlockPos landedPosition) {}
    default void cleanup() {}
}
