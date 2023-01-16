package com.peacefulotter.echomod.gui;

import com.peacefulotter.echomod.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.function.Consumer;

import static com.peacefulotter.echomod.config.ConfigManager.AUTO_FISH_ACTIVE;
import static com.peacefulotter.echomod.config.ConfigManager.AUTO_LIBRARIAN_ACTIVE;

public class EchoMenuScreen extends Screen
{
    private static final Text title = Text.literal( "menu@echo-mod" );
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int OFFSET_ANCHOR_Y = 5;
    private static final int OFFSET_X = 10;

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
        addDrawableChild(
                new ColoredButton.Builder( name, onPress )
                    .size( BUTTON_WIDTH, BUTTON_HEIGHT )
                    .position( OFFSET_X, anchorY )
                    .build()
        );

        anchorY += BUTTON_HEIGHT + OFFSET_ANCHOR_Y;
    }

    private ClickableWidget getSlider( String key, int from, int to, int cur, Consumer<Integer> onChange)
    {
        /*
        new SimpleOption.ValidatingIntSliderCallbacks(from, to)
                        .withModifier(
                                (value) -> value * 10,
                                (value) -> value / 10
                        ),
                Codec.intRange(10, 500)
         */
        SimpleOption<Integer> option = new SimpleOption<>(key,
            SimpleOption.emptyTooltip(),
            (opt, value) -> Text.of(key + ": " + value ),
            new SimpleOption.ValidatingIntSliderCallbacks(from, to),
            cur, onChange
        );

        return option.createButton(
            MinecraftClient.getInstance().options,
            OFFSET_X * 2 + BUTTON_WIDTH, anchorY, BUTTON_WIDTH
        );
    }

    private void addConfig( Config config )
    {
        addDrawableChild(
            new ConfigButton.Builder( config )
                .size( BUTTON_WIDTH, BUTTON_HEIGHT )
                .position( OFFSET_X, anchorY )
                .build()
        );

        addDrawableChild( getSlider( config.getName(), 0, 10, 4, (v) -> {} ) );

        anchorY += BUTTON_HEIGHT + OFFSET_ANCHOR_Y;
    }

    @Override
    protected void init()
    {
        addConfig( AUTO_FISH_ACTIVE );
        addConfig( AUTO_LIBRARIAN_ACTIVE );
        addBtn( "Return", btn -> close() );
    }

    public void close() {
        this.client.setScreen(this.parent);
    }
}
