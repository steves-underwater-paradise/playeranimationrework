package com.steveplays.playeranimationrework.neoforge;

import com.steveplays.playeranimationrework.PlayerAnimationRework;
import com.steveplays.playeranimationrework.client.PlayerAnimationReworkClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(PlayerAnimationRework.MOD_ID)
public class PlayerAnimationReworkNeoForge {
	public PlayerAnimationReworkNeoForge() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			PlayerAnimationReworkClient.initialize();
		}

		PlayerAnimationRework.initialize();
	}
}
