package com.peacefulotter.echomod.events.list;

import com.peacefulotter.echomod.events.Event;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


public record RenderLabelEvent<T extends Entity>(EntityRenderDispatcher dispatcher, TextRenderer textRenderer, T entity,
                                                 MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                                 int light, CallbackInfo ci )
    implements Event
{
}
