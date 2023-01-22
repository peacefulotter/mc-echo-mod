package com.peacefulotter.echomod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoMod implements ModInitializer {
	public static final String MOD_ID = "echomod";
	public static final Logger MOD_LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MOD_LOGGER.info("Hello Fabric world!");
	}
}
