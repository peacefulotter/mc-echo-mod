package com.peacefulotter.echomod.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public interface CommandCallback
{
    void exec( CommandContext<FabricClientCommandSource> context );
}
