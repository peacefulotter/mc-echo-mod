package com.peacefulotter.echomod;

import com.peacefulotter.echomod.commands.CommandHandler;
import com.peacefulotter.echomod.hacks.HacksList;
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

    public static MinecraftClient MC;

    public static void sendMessage(String msg)
    {
        if ( MC.player == null )
            throw new RuntimeException("MC.player is null");

        MC.player.sendMessage( Text.literal( msg ), true );
    }

    public static void sendRainbowMessage(String msg)
    {
        MC.inGameHud.setOverlayMessage( Text.literal( msg ), true );
    }

    @Override
    public void onInitializeClient()
    {
        MC = MinecraftClient.getInstance();
        HacksList.init();

        CommandHandler.register( "foo", ctx ->
            ctx.getSource().sendFeedback( Text.literal( "foo command" ) )
        );

        // implements HudRenderCallback
        // HudRenderCallback.EVENT.register( new AutoLibrarianHud() );

        CLIENT_LOGGER.info( "EchoModClient live!" );
    }

    public static ClientPlayerEntity getPlayer()
    {
        return MC.player;
    }
    public static ClientPlayerInteractionManager getInteractionManager()
    {
        return MC.interactionManager;
    }
}
