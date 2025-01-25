package com.steveplays.playeranimationrework.mixin.client.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.steveplays.playeranimationrework.client.util.ItemUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin({PotionItem.class, HoneyBottleItem.class})
public class BottleItemMixin {
	@Inject(method = "use", at = @At(value = "TAIL"))
	private void playeranimationrework$invokeItemUseEvents(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		if (!(user instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		ItemUtil.invokeItemUseStartEvents(clientPlayer, clientPlayer.getActiveItem());
	}

	@Inject(method = "finishUsing", at = @At(value = "HEAD"))
	private void playeranimationrework$invokeItemUseFinishEvents(ItemStack itemStack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		if (!(user instanceof AbstractClientPlayerEntity clientPlayer)) {
			return;
		}

		ItemUtil.invokeItemUseStopEvents(clientPlayer, itemStack);
	}
}
