package com.steveplays.playeranimationrework.mixin.client.item;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.llamalad7.mixinextras.sugar.Local;
import com.steveplays.playeranimationrework.client.util.ItemUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(Item.class)
public class ItemMixin {
	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setCurrentHand(Lnet/minecraft/util/Hand;)V"))
	private void playeranimationrework$invokeItemUseStartEvents(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir,
			@Local @NotNull ItemStack itemStack) {
		if (!(user instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		ItemUtil.invokeItemUseStartEvents(clientPlayer, itemStack);
	}

	@Inject(method = "finishUsing", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/LivingEntity;eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/FoodComponent;)Lnet/minecraft/item/ItemStack;"))
	private void playeranimationrework$invokeItemUseFinishEvents(ItemStack itemStack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		if (!(user instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		ItemUtil.invokeItemUseStopEvents(clientPlayer, itemStack);
	}

	@Inject(method = "onStoppedUsing", at = @At(value = "HEAD"))
	private void playeranimationrework$invokeItemUseStopEvents(ItemStack itemStack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		if (!(user instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		ItemUtil.invokeItemUseStopEvents(clientPlayer, itemStack);
	}
}
