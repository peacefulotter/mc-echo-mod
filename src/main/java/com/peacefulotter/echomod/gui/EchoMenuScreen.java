package com.peacefulotter.echomod.gui;

import com.peacefulotter.echomod.config.Config;
import com.peacefulotter.echomod.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class EchoMenuScreen extends Screen
{
    private static final Text title = Text.literal( "menu@echo-mod" );
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int OFFSET_ANCHOR_Y = 5;
    private static int OFFSET_ANCHOR_X = 10;

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

    private ColoredButton addBtn( String name, ButtonWidget.PressAction onPress )
    {
        ColoredButton widget = new ColoredButton.Builder( name, onPress )
            .size( BUTTON_WIDTH, BUTTON_HEIGHT )
            .position( OFFSET_ANCHOR_X, anchorY )
            .build();

        addDrawableChild( widget );

        anchorY += BUTTON_HEIGHT + OFFSET_ANCHOR_Y;
        return widget;
    }

    private double map(double cur, double from, double to)
    {
        return cur * (to - from) + from;
    }

    private double mapAndRound( double v, double from, double to )
    {
        BigDecimal bd = BigDecimal.valueOf( map(v, from, to) );
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void addSlider( SliderParams p )
    {
        double from = p.from();
        double to = p.to();
        SimpleOption<Double> option = new SimpleOption<>(p.key(),
            SimpleOption.emptyTooltip(),
            (opt, value) -> Text.of(p.key() + ": " + mapAndRound(value, from, to) ),
            SimpleOption.DoubleSliderCallbacks.INSTANCE,
            p.cur(), v -> p.onChange().accept(map(v, from, to))
        );

        addDrawableChild( option.createButton(
            MinecraftClient.getInstance().options,
            OFFSET_ANCHOR_X * 2 + BUTTON_WIDTH, anchorY, BUTTON_WIDTH
        ) );
    }

    private void addConfig( Config config )
    {
        addDrawableChild(
            new ConfigButton.Builder( config )
                .size( BUTTON_WIDTH, BUTTON_HEIGHT )
                .position( OFFSET_ANCHOR_X, anchorY )
                .build()
        );

        config.getWidgetParams().forEach( p -> {
            addSlider(p);
            anchorY += BUTTON_HEIGHT + OFFSET_ANCHOR_Y;
        } );

        anchorY += BUTTON_HEIGHT + OFFSET_ANCHOR_Y;
    }

    @Override
    protected void init()
    {
        OFFSET_ANCHOR_X = 5;
        for ( ConfigManager cm : ConfigManager.values() )
            addConfig( cm.getConfig() );
        addBtn( "Return", btn -> close() );
        addBtn( "Test 1", btn -> {} ).setBackgroundColor( Color.BLUE );
        addBtn( "Test 2", btn -> {} ).setBackgroundColor( Color.MAGENTA );
        addBtn( "Test 3", btn -> {} ).setBackgroundColor( Color.CYAN );
        addBtn( "Test 4", btn -> {} ).setBackgroundColor( Color.GRAY );
        addBtn( "Test 5", btn -> {} ).setBackgroundColor( Color.GREEN );
        OFFSET_ANCHOR_X = 300;
        anchorY = OFFSET_ANCHOR_Y;
        addBtn( "Test 6", btn -> {} ).setBackgroundColor( Color.LIGHT_GRAY );
        addBtn( "Test 7", btn -> {} ).setBackgroundColor( Color.ORANGE );
        addBtn( "Test 8", btn -> {} ).setBackgroundColor( Color.PINK );
        addBtn( "Test 9", btn -> {} ).setBackgroundColor( Color.magenta );
        addBtn( "Test 11", btn -> {} ).setBackgroundColor( Color.RED );
    }

    public void close() {
        this.client.setScreen(this.parent);
    }
}
