package com.peacefulotter.echomod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peacefulotter.echomod.EchoMod;
import com.peacefulotter.echomod.config.Config;
import com.peacefulotter.echomod.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;

public class MenuScreen extends Screen
{
    private static final int GRADIENT_START_COLOR = getRGBA( MenuColors.PRUSSIAN_BLUE );
    private static final int GRADIENT_MIDDLE_COLOR = getRGBA( MenuColors.PURPLE );
    private static final int GRADIENT_STOP_COLOR = getRGBA( MenuColors.BLACK_FOGRA );

    private static final Text TITLE = Text.literal( "menu@echo-mod" );
    private static final int BACKGROUND_MARGIN = 5;
    private static final int BUTTON_WIDTH = 100;
    private static final int CONFIG_BUTTON_WIDTH = 20;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING_Y = 10;
    private static final int PADDING_X = 10;
    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 5;

    private final Screen parent;
    private final MinecraftClient client;
    private final Identifier imgLoc;
    private GridWidget configGrid;
    private ColoredButton activeConfigBtn;
    private int time;

    private static int getRGBA(MenuColors mc)
    {
        Color c = mc.getColor();
        var r = c.getRed() & 0xFF;
        var g = c.getGreen() & 0xFF;
        var b = c.getBlue() & 0xFF;
        var a = c.getAlpha() & 0xFF;
        return (a << 24) + (r << 16) + (g << 8) + (b);
    }

    public MenuScreen( Screen parent, MinecraftClient client )
    {
        super( TITLE );
        this.parent = parent;
        this.client = client;
        this.configGrid = new GridWidget();
        this.time = 0;
        addDrawableChild( configGrid );

        this.imgLoc = new Identifier( EchoMod.MOD_ID, "textures/background.jpg" );
        loadBackgroundTexture();
    }

    private void loadBackgroundTexture()
    {
        ResourceManager manager  = MinecraftClient.getInstance().getResourceManager();
        try ( InputStream stream = manager.getResource( imgLoc ).get().getInputStream() )
        {
            NativeImage nativeImage = NativeImage.read(stream);
            NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
            this.client.getTextureManager().registerTexture(imgLoc, nativeImageBackedTexture);
        } catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    private int getPosX( int x )
    {
        return BACKGROUND_MARGIN + PADDING_X + OFFSET_X  * x + BUTTON_WIDTH * (x - 1) +
            (x >= 2 ? CONFIG_BUTTON_WIDTH : BUTTON_WIDTH);
    }

    private int getPosY( int y )
    {
        return BACKGROUND_MARGIN + PADDING_Y + (OFFSET_Y + BUTTON_HEIGHT) * y;
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
            activeConfigBtn.setBackgroundColor( MenuColors.BLACK_FOGRA );
            activeConfigBtn.setTextColor( MenuColors.PRUSSIAN_BLUE );
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
        activeConfigBtn.setBackgroundColor( MenuColors.WIDGET_BACK );
        activeConfigBtn.setTextColor( MenuColors.MIKADO_YELLOW );
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
                .setBackgroundColor( MenuColors.BLACK_FOGRA )
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

        addBtn( "Test", btn -> close(), 3, 0 ).setBackgroundColor( MenuColors.CORNFLOWER_BLUE );
        addBtn( "Test", btn -> close(), 3, 1 ).setBackgroundColor( MenuColors.AMARANTH_PURPLE );
        addBtn( "Test", btn -> close(), 3, 2 ).setBackgroundColor( MenuColors.PEACH );
        addBtn( "Test", btn -> close(), 3, 3 ).setBackgroundColor( MenuColors.GREEN_PANTONE );
        addBtn( "Test", btn -> close(), 3, 4 ).setBackgroundColor( MenuColors.SEA_GREEN_CRAYOLA );
        addBtn( "Test", btn -> close(), 3, 5 ).setBackgroundColor( MenuColors.MOUTAIN_MEADOW );
        addBtn( "Test", btn -> close(), 3, 6 ).setBackgroundColor( MenuColors.DARK_GREEN );
        addBtn( "Test", btn -> close(), 3, 7 ).setBackgroundColor( MenuColors.RED_CRAYOLA );
        addBtn( "Test", btn -> close(), 3, 8 ).setBackgroundColor( MenuColors.ARMY_GREEN );
    }

    @Override
    public void render( MatrixStack matrices, int mouseX, int mouseY, float delta )
    {
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, this.imgLoc);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // fillGradient(matrices, 0, 0, width, height / 2, GRADIENT_START_COLOR, GRADIENT_MIDDLE_COLOR);
        // fillGradient(matrices, 0, height / 2, width, height, GRADIENT_MIDDLE_COLOR, GRADIENT_STOP_COLOR);

        // Background
        Color color = Color.getHSBColor( time / 360f, 1, 1 );
        time = (time + 1) % 360;
        fill(matrices, 0, 0, width, height, color.getRGB());

        // Image
        int margin = BACKGROUND_MARGIN * 2;
        drawTexture( matrices, BACKGROUND_MARGIN, BACKGROUND_MARGIN, 0f, 0f, width - margin, height - margin, width - margin, height - margin );
        super.render( matrices, mouseX, mouseY, delta );
    }

    public void close() {
        this.client.setScreen(this.parent);
    }
}
