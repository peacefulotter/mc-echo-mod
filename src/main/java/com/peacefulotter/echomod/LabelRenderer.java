package com.peacefulotter.echomod;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

public class LabelRenderer
{
    private static final double MAX_RENDER_DISTANCE = 4096d;

    public static <T extends Entity> void render( EntityRenderDispatcher dispatcher, TextRenderer textRenderer,
                                                  T entity, Text text, MatrixStack matrices,
                                                  VertexConsumerProvider vertexConsumer, int light )
    {
        if (dispatcher.getSquaredDistanceToCamera(entity) > MAX_RENDER_DISTANCE)
            return;

        boolean bl = !entity.isSneaky();
        float f = entity.getHeight() + 0.5F;

        matrices.push();
        matrices.translate(0.0D, f, 0.0D);
        matrices.multiply(dispatcher.getRotation());

        float scale = 0.025F;
        double distance = EchoModClient.getPlayer().distanceTo(entity);

        if (distance > 10)
            scale *= distance / 10;

        matrices.scale(-scale, -scale, scale);

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float g = EchoModClient.MC.options.getTextBackgroundOpacity(0.25F);
        int k = (int)(g * 255.0F) << 24;

        float h = -textRenderer.getWidth(text) / 2f;

        textRenderer.draw(
            text.asOrderedText(), h, 0, 553648127, false,
            matrix4f, vertexConsumer, bl, k, light
        );

        if (bl)
            textRenderer.draw(
                text.asOrderedText(), h, 0, -1, false, matrix4f,
                vertexConsumer, false, 0, light
            );

        matrices.pop();
    }
}
