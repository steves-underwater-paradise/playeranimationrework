package com.steveplays.playeranimationrework.forge;

import com.steveplays.playeranimationrework.PlayerAnimationRework;
import net.minecraftforge.fml.common.Mod;

@Mod(PlayerAnimationRework.MOD_ID)
public class PlayerAnimationReworkForge {
	public PlayerAnimationReworkForge() {
		PlayerAnimationRework.initialize();
	}
}
