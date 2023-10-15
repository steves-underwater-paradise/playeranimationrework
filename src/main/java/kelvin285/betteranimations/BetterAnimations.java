package kelvin285.betteranimations;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterAnimations implements ClientModInitializer {
	public static final String MOD_ID = "betteranimations";
	public static final String MOD_NAME = "Player Animation Rework";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
	    LOGGER.info("Loading {}.", MOD_NAME);
    }
}
