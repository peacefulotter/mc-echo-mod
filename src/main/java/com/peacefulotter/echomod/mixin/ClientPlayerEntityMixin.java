package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.events.list.*;
import com.peacefulotter.echomod.events.EventManager;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( ClientPlayerEntity.class )
public abstract class ClientPlayerEntityMixin extends Entity
{
    @Shadow
    public Input input;
    @Shadow @Final
    public ClientPlayNetworkHandler networkHandler;

    public ClientPlayerEntityMixin( EntityType<?> type, World world )
    {
        super( type, world );
    }

    @Inject(at=@At("RETURN"), method="startRiding" )
    private void onStartRiding( Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir )
    {
        if ( !cir.getReturnValue() ) return;
        EventManager.fire( new PlayerStartRidingEvent( entity ) );
    }

    @Inject(at=@At( "RETURN" ), method="dismountVehicle")
    private void onDismountVehicle( CallbackInfo ci )
    {
        EventManager.fire( new PlayerStopRidingEvent() );
    }

    @Inject(at=@At( "RETURN" ), method="tick")
    private void onTick( CallbackInfo ci )
    {
        EventManager.fire( new PlayerTickEvent(networkHandler) );
    }

    @Inject(at=@At( "RETURN" ), method="tickMovement")
    private void onTickMovement( CallbackInfo ci )
    {
        EventManager.fire( new PlayerTickMovementEvent( networkHandler, input, getYaw(), getRotationVector() ) );
    }
}
