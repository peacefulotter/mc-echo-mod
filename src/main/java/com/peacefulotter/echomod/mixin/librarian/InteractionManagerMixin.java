package com.peacefulotter.echomod.mixin.librarian;

import com.peacefulotter.echomod.EchoModClient;
import com.peacefulotter.echomod.LibrarianCache;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.peacefulotter.echomod.config.ConfigManager.AUTO_LIBRARIAN;

@Mixin( ClientPlayerInteractionManager.class )
public class InteractionManagerMixin
{
    // Setup required to get the target entity
    // Player interacts once with the villager to set it as the target

    @Inject( at = @At( "RETURN" ), method = "interactEntity" )
    public void interactEntity( PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir )
    {
        if ( !AUTO_LIBRARIAN.getActive() || !(entity instanceof VillagerEntity) ) return;
        EchoModClient.sendMessage( "Setting Villager as Librarian target" );
        LibrarianCache.target = (VillagerEntity) entity;
    }
}
