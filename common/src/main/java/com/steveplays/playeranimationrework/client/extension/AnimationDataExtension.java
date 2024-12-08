package com.steveplays.playeranimationrework.client.extension;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition;

public interface AnimationDataExtension {
	@NotNull
	List<AnimationDefinition> playeranimationrework$getCurrentAnimations();
}
