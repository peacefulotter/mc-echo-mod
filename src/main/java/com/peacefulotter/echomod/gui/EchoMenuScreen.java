package com.peacefulotter.echomod.gui;

import com.peacefulotter.echomod.config.Config;
import com.peacefulotter.echomod.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class EchoMenuScreen extends Screen
{
    private static final int GRADIENT_START_COLOR = getRGBA( 150, 150, 0, 100 );
    private static final int GRADIENT_MIDDLE_COLOR = getRGBA( 0, 150, 150, 100 );
    private static final int GRADIENT_STOP_COLOR = getRGBA( 150, 0, 150, 100 );

    private static final Text TITLE = Text.literal( "menu@echo-mod" );
    private static final int BUTTON_WIDTH = 100;
    private static final int CONFIG_BUTTON_WIDTH = 20;
    private static final int BUTTON_HEIGHT = 20;
    private static final int ANCHOR_Y = 10;
    private static final int ANCHOR_X = 10;
    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 5;

    private final Screen parent;
    private final MinecraftClient client;
    private GridWidget configGrid;
    private ColoredButton activeConfigBtn;

    private static int getRGBA(int red, int green, int blue, int alpha)
    {
        var r = red & 0xFF;
        var g = green & 0xFF;
        var b = blue & 0xFF;
        var a = alpha & 0xFF;
        return (a << 24) + (r << 16) + (g << 8) + (b);
    }

    public EchoMenuScreen( Screen parent, MinecraftClient client )
    {
        super( TITLE );
        this.parent = parent;
        this.client = client;
        this.configGrid = new GridWidget();
        addDrawableChild( configGrid );

    }

    private int getPosX( int x )
    {
        return ANCHOR_X + OFFSET_X  * x + BUTTON_WIDTH * (x - 1) +
            (x >= 2 ? CONFIG_BUTTON_WIDTH : BUTTON_WIDTH);
    }

    private int getPosY( int y )
    {
        return ANCHOR_Y + (OFFSET_Y + BUTTON_HEIGHT) * y;
    }

    private ColoredButton addBtn( String name, ButtonWidget.PressAction onPress, int x, int y )
    {
        ColoredButton widget = new ColoredButton.Builder( name, onPress )
            .size( BUTTON_WIDTH, BUTTON_HEIGHT )
            .position( getPosX( x ), getPosY( y ) )
            .build();
        addDrawableChild( widget );
        return widget;
    }

    private double invMap(double cur, double from, double to)
    {
        return cur * (to - from) + from;
    }

    private double map( double cur, double from, double to )
    {
        return (cur - from) / (to - from);
    }

    private double invMapAndRound( double v, double from, double to )
    {
        BigDecimal bd = BigDecimal.valueOf( invMap(v, from, to) );
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private ClickableWidget getSlider( SliderParams p )
    {
        double from = p.from();
        double to = p.to();
        SimpleOption<Double> option = new SimpleOption<>(p.key(),
            SimpleOption.emptyTooltip(),
            (opt, value) -> Text.of(p.key() + ": " + invMapAndRound(value, from, to) ),
            SimpleOption.DoubleSliderCallbacks.INSTANCE,
            map(p.cur(), from, to), v -> p.onChange().accept(invMap(v, from, to))
        );

        return option.createButton(
            MinecraftClient.getInstance().options,
            0, 0, BUTTON_WIDTH
        );
    }

    private void openConfigMenu( ButtonWidget btn, Config config, int x )
    {
        if ( activeConfigBtn != null )
        {
            activeConfigBtn.setBackgroundColor( Color.GRAY );
            activeConfigBtn.setTextColor( Color.WHITE );
        }

        remove( configGrid );

        if ( activeConfigBtn == btn )
        {
            activeConfigBtn = null;
            return;
        };

        configGrid = new GridWidget();
        configGrid.getMainPositioner().margin( 0, 0, 0, OFFSET_Y );
        configGrid.setPos( getPosX( x ), getPosY( 0 ) );
        activeConfigBtn = (ColoredButton) btn;
        activeConfigBtn.setBackgroundColor( Color.MAGENTA );
        activeConfigBtn.setTextColor( Color.WHITE );
        configGrid.createAdder( 1 );

        int row = 0;
        for ( SliderParams p : config.getWidgetParams() )
        {
            ClickableWidget w = getSlider( p );
            configGrid.add( w, row++, 0 );
        }

        configGrid.recalculateDimensions();
        addDrawableChild( configGrid );
    }

    private void addConfig( Config config, int x, int y )
    {
        addDrawableChild(
            new ConfigButton.Builder( config )
                .size( BUTTON_WIDTH, BUTTON_HEIGHT )
                .position( getPosX( x ), getPosY( y ) )
                .build()
        );

        if ( config.getWidgetParams().size() == 0 ) return;

        addDrawableChild(
            new ColoredButton.Builder( ">", (btn) -> openConfigMenu( btn, config, x + 2 ) )
                .size( CONFIG_BUTTON_WIDTH, BUTTON_HEIGHT )
                .position( getPosX( x + 1 ), getPosY( y ) )
                .setBackgroundColor( Color.GRAY )
                .build()
        );
    }

    @Override
    protected void init()
    {
        int x = 0;
        int y = 0;
        for ( ConfigManager cm : ConfigManager.values() )
            addConfig( cm.getConfig(), x, y++ );

        addBtn( "Return", btn -> close(), x, 8 );
        addBtn( "Test 3", btn -> {}, 3, 0 ).setBackgroundColor( Color.CYAN );
        addBtn( "Test 4", btn -> {}, 3, 1 ).setBackgroundColor( Color.GRAY );
        addBtn( "Test 5", btn -> {}, 3, 2 ).setBackgroundColor( Color.GREEN );
        addBtn( "Test 9", btn -> {}, 3, 3 ).setBackgroundColor( Color.magenta );
        addBtn( "Test 0", btn -> {}, 3, 4 ).setBackgroundColor( Color.RED );
    }

    @Override
    public void render( MatrixStack matrices, int mouseX, int mouseY, float delta )
    {
        this.fillGradient(matrices, 0, 0, this.width, this.height / 2, GRADIENT_START_COLOR, GRADIENT_MIDDLE_COLOR);
        this.fillGradient(matrices, 0, this.height / 2, this.width, this.height, GRADIENT_MIDDLE_COLOR, GRADIENT_STOP_COLOR);
        super.render( matrices, mouseX, mouseY, delta );
    }

    public void close() {
        this.client.setScreen(this.parent);
    }
}
