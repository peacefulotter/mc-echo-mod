package com.peacefulotter.echomod.mixin.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peacefulotter.echomod.gui.MenuColors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin( SliderWidget.class )
public class SliderWidgetMixin
{
    @Inject( at=@At( value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", shift = At.Shift.AFTER), method = "renderBackground")
    protected void renderBackground( MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY, CallbackInfo ci ) {
        Color c = MenuColors.WIDGET_BACK.getColor();
        RenderSystem.setShaderColor(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);
    }
}
