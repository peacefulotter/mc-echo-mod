package com.peacefulotter.echomod.gui;

import com.peacefulotter.echomod.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import static com.peacefulotter.echomod.config.ConfigManager.AUTO_FISH_ACTIVE;
import static com.peacefulotter.echomod.config.ConfigManager.AUTO_LIBRARIAN_ACTIVE;

public class EchoMenuScreen extends Screen
{
    private static final Text title = Text.literal( "menu@echo-mod" );
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;

    private final Screen parent;
    private final MinecraftClient client;

    private int anchorY;

    public EchoMenuScreen( Screen parent, MinecraftClient client )
    {
        super( title );
        this.parent = parent;
        this.client = client;
        this.anchorY = 10;
    }

    private void addBtn( String name, ButtonWidget.PressAction onPress )
    {
        addBtn(
                ButtonWidget.builder( Text.of( name ), onPress )
                .size( BUTTON_WIDTH, BUTTON_HEIGHT )
                .position( 10, anchorY )
                .build()
        );
    }

    private void addBtn( Config config )
    {
        addBtn(
                new ConfigButton.Builder( Text.of( config.getName() ), (w) -> config.toggle() )
                        .size( BUTTON_WIDTH, BUTTON_HEIGHT )
                        .position( 10, anchorY )
                        .config( config )
                        .build()
        );
    }

    private void addBtn( ButtonWidget btn )
    {
        this.addDrawableChild( btn );
        anchorY += BUTTON_HEIGHT + 5;
    }

    @Override
    protected void init()
    {
        addBtn( AUTO_FISH_ACTIVE );
        addBtn( AUTO_LIBRARIAN_ACTIVE );
        addBtn( "Return", btn -> close() );
    }

    public void close() {
        this.client.setScreen(this.parent);
    }
}
