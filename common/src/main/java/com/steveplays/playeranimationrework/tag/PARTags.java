package com.steveplays.playeranimationrework.tag;

import static com.steveplays.playeranimationrework.PlayerAnimationRework.MOD_ID;
import org.jetbrains.annotations.NotNull;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class PARTags {
	public static final @NotNull TagKey<Block> REQUIRES_BALANCING = TagKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, "requires_balancing"));
	public static final @NotNull TagKey<Block> REQUIRES_BALANCING_FULL_BLOCK_HIT_BOX = TagKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, "requires_balancing_full_block_hit_box"));
	public static final @NotNull TagKey<Item> IS_ON_BACK = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "is_on_back"));
	public static final @NotNull TagKey<Item> USES_VANILLA_ANIMATIONS_MAIN_HAND = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "uses_vanilla_animations_main_hand"));
	public static final @NotNull TagKey<Item> USES_VANILLA_ANIMATIONS_OFF_HAND = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "uses_vanilla_animations_off_hand"));
	public static final @NotNull TagKey<Item> DRINKS = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "drinks"));

	public static class Common {
		private static final @NotNull String COMMON_NAMESPACE = "c";

		public static final @NotNull TagKey<Item> IGNITER_TOOLS = TagKey.of(RegistryKeys.ITEM, Identifier.of(COMMON_NAMESPACE, "tools/igniter"));
		public static final @NotNull TagKey<Item> FOODS = TagKey.of(RegistryKeys.ITEM, Identifier.of(COMMON_NAMESPACE, "foods"));
	}
}
