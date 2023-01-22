package com.peacefulotter.echomod.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class ModMenuBtn
{
    public static ButtonWidget getBtn( Screen parent, MinecraftClient client )
    {
        return new ColoredButton.Builder( "menu@echo-mod", btn -> {
            if ( client == null ) return;
            client.setScreen(new EchoMenuScreen(parent, client));
        } )
            .size( 100, 20 )
            .position( 10, 10 )
            .build();
    }
}
