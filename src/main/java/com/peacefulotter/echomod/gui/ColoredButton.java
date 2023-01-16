package com.peacefulotter.echomod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peacefulotter.echomod.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.function.Supplier;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;


public class ColoredButton extends ButtonWidget
{
    private Color textColor, backgroundColor;

    public ColoredButton( Color textColor, Color backgroundColor, int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier )
    {
        super( x, y, width, height, message, onPress, narrationSupplier );
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void renderButton( MatrixStack matrices, int mouseX, int mouseY, float delta )
    {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        RenderSystem.setShader( GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, this.getX(), this.getY(), 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexture(matrices, this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
        int color = textColor.getRGB() | MathHelper.ceil(this.alpha * 255.0F) << 24;
        drawCenteredText(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color );
    }

    public void setTextColor( Color textColor )
    {
        this.textColor = textColor;
    }

    public void setBackgroundColor( Color backgroundColor )
    {
        this.backgroundColor = backgroundColor;
    }

    public static class Builder
    {
        protected static final NarrationSupplier NARRATION_SUPPLIER = Supplier::get;
        protected final Text message;
        protected final PressAction onPress;
        protected int x;
        protected int y;
        protected int width = 150;
        protected int height = 20;
        protected Color textColor, backgroundColor;

        public Builder( String text, PressAction onPress) {
            this.message = Text.of( text );
            this.onPress = onPress;
            this.textColor = Color.WHITE;
            this.backgroundColor = Color.DARK_GRAY;
        }

        public Builder position( int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size( int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public void setTextColor( Color textColor )
        {
            this.textColor = textColor;
        }

        public void setBackgroundColor( Color backgroundColor )
        {
            this.backgroundColor = backgroundColor;
        }

        public ColoredButton build() {
            return new ColoredButton( textColor, backgroundColor, x, y, width, height, message, onPress, NARRATION_SUPPLIER );
        }
    }
}
