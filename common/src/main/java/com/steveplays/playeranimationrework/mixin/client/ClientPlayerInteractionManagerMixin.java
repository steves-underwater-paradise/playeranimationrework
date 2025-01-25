package com.steveplays.playeranimationrework.mixin.client;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.llamalad7.mixinextras.sugar.Local;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
import com.steveplays.playeranimationrework.tag.PARTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
	@Inject(method = "interactBlockInternal",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"))
	private void playeranimationrework$invokeItemUseOnBlockEvents(@NotNull ClientPlayerEntity clientPlayer, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir,
			@Local @NotNull ItemStack itemStack) {
		if (itemStack.isIn(PARTags.Common.IGNITER_TOOLS)) {
			PARPlayerEvents.USE_IGNITER.invoker().onExecute(clientPlayer);
		}
	}
}
