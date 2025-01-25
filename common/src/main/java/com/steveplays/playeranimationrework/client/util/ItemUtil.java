package com.steveplays.playeranimationrework.client.util;

import org.jetbrains.annotations.NotNull;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
import com.steveplays.playeranimationrework.tag.PARTags;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;

public class ItemUtil {
	public static void invokeItemUseStartEvents(@NotNull AbstractClientPlayerEntity clientPlayer, @NotNull ItemStack itemStack) {
		if (itemStack.isIn(PARTags.DRINKS)) {
			PARPlayerEvents.DRINK_RIGHT_ARM_START.invoker().onExecute(clientPlayer);
		} else if (itemStack.isIn(PARTags.Common.FOODS)) {
			PARPlayerEvents.EAT_START.invoker().onExecute(clientPlayer);
		}
	}

	public static void invokeItemUseStopEvents(@NotNull AbstractClientPlayerEntity clientPlayer, @NotNull ItemStack itemStack) {
		if (itemStack.isIn(PARTags.DRINKS)) {
			PARPlayerEvents.DRINK_RIGHT_ARM_STOP.invoker().onExecute(clientPlayer);
		} else if (itemStack.isIn(PARTags.Common.FOODS)) {
			PARPlayerEvents.EAT_STOP.invoker().onExecute(clientPlayer);
		}
	}
}
