package com.peacefulotter.echomod;

import com.peacefulotter.echomod.commands.CommandHandler;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoModClient implements ClientModInitializer
{
    private static final String MOD_ID = "echo-mod-client";
    public static final Logger CLIENT_LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void sendMessage(String msg)
    {
        MinecraftClient.getInstance().player.sendMessage(
            Text.literal( msg ),
            true
        );
    }

    public static void sendRainbowMessage(String msg)
    {
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(
            Text.literal( msg ),
            true
        );
    }

    @Override
    public void onInitializeClient()
    {
        CLIENT_LOGGER.info( "EchoModClient live!" );

        CommandHandler.register( "foo", ctx ->
            ctx.getSource().sendFeedback( Text.literal( "foo command" ) )
        );

        // implements HudRenderCallback
        // HudRenderCallback.EVENT.register( new AutoLibrarianHud() );
    }

    public static ClientPlayerEntity getPlayer()
    {
        return MinecraftClient.getInstance().player;
    }
    public static ClientPlayerInteractionManager getInteractionManager()
    {
        return MinecraftClient.getInstance().interactionManager;
    }
}
