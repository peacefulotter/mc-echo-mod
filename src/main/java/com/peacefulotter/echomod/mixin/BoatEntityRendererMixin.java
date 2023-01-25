package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.events.EventManager;
import com.peacefulotter.echomod.events.list.BoatEntityRenderEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( BoatEntityRenderer.class )
public class BoatEntityRendererMixin
{
    @Inject( method = "render*", at=@At ( "HEAD" ), cancellable = true )
    private void render( BoatEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci )
    {
        EventManager.fire( new BoatEntityRenderEvent( entity, ci ) );
    }
}
