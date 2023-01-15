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

import java.util.function.Supplier;


public class ConfigButton extends ButtonWidget
{
    private static final int RED = 16711680;
    private static final int GREEN = 65280;

    private final Config config;

    public ConfigButton( Config config, int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier )
    {
        super( x, y, width, height, message, onPress, narrationSupplier );
        this.config = config;
    }

    private int getColor()
    {
        return config.get() ? GREEN : RED;
    }

    @Override
    public void renderButton( MatrixStack matrices, int mouseX, int mouseY, float delta )
    {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        RenderSystem.setShader( GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 0.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, this.getX(), this.getY(), 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexture(matrices, this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
        int color = getColor() | MathHelper.ceil(this.alpha * 255.0F) << 24;
        drawCenteredText(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color );
    }

    public static class Builder
    {
        private static final ButtonWidget.NarrationSupplier NARRATION_SUPPLIER = Supplier::get;
        private final Text message;
        private final ButtonWidget.PressAction onPress;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private Config config;

        public Builder(Text message, ButtonWidget.PressAction onPress) {
            this.message = message;
            this.onPress = onPress;
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

        public Builder config( Config config )
        {
            this.config = config;
            return this;
        }

        public ConfigButton build() {
            if ( config == null ) throw new RuntimeException("Config is null");
            return new ConfigButton( config, x, y, width, height, message, onPress, NARRATION_SUPPLIER );
        }
    }
}
