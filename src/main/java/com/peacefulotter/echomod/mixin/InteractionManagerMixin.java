package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.events.EventManager;
import com.peacefulotter.echomod.events.list.PlayerInteractEntityEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( ClientPlayerInteractionManager.class )
public class InteractionManagerMixin
{
    @Inject( at = @At( "RETURN" ), method = "interactEntity" )
    public void interactEntity( PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir )
    {
        EventManager.fire( new PlayerInteractEntityEvent(player, entity, hand) );
    }
}
