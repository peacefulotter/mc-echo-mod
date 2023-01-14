package com.peacefulotter.echomod.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

public class Bot
{
    public static void useHandItem()
    {
        MinecraftClient client = MinecraftClient.getInstance();
        if ( client.interactionManager == null ) return;
        client.interactionManager.interactItem( client.player, Hand.MAIN_HAND );
    }
}
