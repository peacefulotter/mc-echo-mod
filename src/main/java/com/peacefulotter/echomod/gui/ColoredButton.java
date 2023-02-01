package com.peacefulotter.echomod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.function.Supplier;


public class ColoredButton extends ButtonWidget
{
    private Color textColor, backgroundColor;

    public ColoredButton( MenuColors textColor, MenuColors backgroundColor, int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier )
    {
        super( x, y, width, height, message, onPress, narrationSupplier );
        this.textColor = textColor.getColor();
        this.backgroundColor = backgroundColor.getColor();
    }

    @Override
    public void renderButton( MatrixStack matrices, int mouseX, int mouseY, float delta )
    {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        RenderSystem.setShader( GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(backgroundColor.getRed() / 255f, backgroundColor.getGreen() / 255f, backgroundColor.getBlue() / 255f, this.alpha);
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

    public void setTextColor( MenuColors textColor )
    {
        this.textColor = textColor.getColor();
    }

    public void setBackgroundColor( MenuColors backgroundColor )
    {
        this.backgroundColor = backgroundColor.getColor();
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
        protected MenuColors textColor, backgroundColor;

        public Builder( String text, PressAction onPress) {
            this.message = Text.of( text );
            this.onPress = onPress;
            this.textColor = MenuColors.WIDGET_TEXT;
            this.backgroundColor = MenuColors.WIDGET_BACK;
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

        public Builder setTextColor( MenuColors textColor )
        {
            this.textColor = textColor;
            return this;
        }

        public Builder setBackgroundColor( MenuColors backgroundColor )
        {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public ColoredButton build() {
            return new ColoredButton( textColor, backgroundColor, x, y, width, height, message, onPress, NARRATION_SUPPLIER );
        }
    }
}
