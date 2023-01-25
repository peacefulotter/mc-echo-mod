package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.events.EventManager;
import com.peacefulotter.echomod.events.list.RenderLabelEvent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity>
{
    @Shadow @Final protected EntityRenderDispatcher dispatcher;
    @Shadow @Final private TextRenderer textRenderer;

    @Inject(
        at = @At("HEAD"),
        method = "render",
        cancellable = true
    )
    private void render( T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci )
    {
        EventManager.fire( new RenderLabelEvent<>( dispatcher, textRenderer, entity, matrices, vertexConsumers, light, ci ) );
    }
}
