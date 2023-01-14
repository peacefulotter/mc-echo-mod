package com.peacefulotter.echomod.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class CommandHandler
{
    public static void register( String name, CommandCallback cb )
    {
        ClientCommandRegistrationCallback.EVENT.register( ( dispatcher, registryAccess ) ->
        {
            dispatcher.register( ClientCommandManager.literal( name ).executes( context ->
            {
                cb.exec( context );
                return 0;
            } ) );
        } );
    }
}
