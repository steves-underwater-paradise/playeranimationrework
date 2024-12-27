package com.steveplays.playeranimationrework.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.steveplays.playeranimationrework.client.event.PARPlayerEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At(value = "TAIL"))
	private void playeranimationrework$invokePunchEvent(CallbackInfo ci) {
		if (!(((ClientPlayerEntity) (Object) this) instanceof AbstractClientPlayerEntity)) {
			return;
		}

		PARPlayerEvents.PUNCH.invoker().onExecute((AbstractClientPlayerEntity) (Object) this);
	}
}
