package com.peacefulotter.echomod;

import com.peacefulotter.echomod.commands.CommandHandler;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoModClient implements ClientModInitializer
{
    private static final String MOD_ID = "echo-mod-client";
    public static final Logger CLIENT_LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient()
    {
        CLIENT_LOGGER.info( "EchoModClient live!" );

        CommandHandler.register( "foo", ctx ->
            ctx.getSource().sendFeedback( Text.literal( "foo command" ) )
        );
    }
}
