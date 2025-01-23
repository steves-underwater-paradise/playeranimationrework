package com.steveplays.playeranimationrework.tag;

import static com.steveplays.playeranimationrework.PlayerAnimationRework.MOD_ID;
import org.jetbrains.annotations.NotNull;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class PARTags {
	public static final @NotNull TagKey<Item> IS_ON_BACK = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "is_on_back"));
	public static final @NotNull TagKey<Item> USES_VANILLA_ANIMATIONS_MAIN_HAND = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "uses_vanilla_animations_main_hand"));
	public static final @NotNull TagKey<Item> USES_VANILLA_ANIMATIONS_OFF_HAND = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "uses_vanilla_animations_off_hand"));
}
