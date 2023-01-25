package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.events.EventManager;
import com.peacefulotter.echomod.events.list.OpenScreenEvent;
import com.peacefulotter.echomod.events.list.SetTradeOffersEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ClientPlayNetworkHandler.class)
public class NetworkHandlerMixin
{
    @Inject(at=@At ( "HEAD" ), method="onOpenScreen", cancellable = true )
    public void onOpenScreen( OpenScreenS2CPacket packet, CallbackInfo ci )
    {
        EventManager.fire( new OpenScreenEvent( packet, ci ) );
    }

    @Inject( at = @At( "RETURN" ), method = "onSetTradeOffers", cancellable = true )
    public void onSetTradeOffers( SetTradeOffersS2CPacket packet, CallbackInfo ci )
    {
        EventManager.fire( new SetTradeOffersEvent( packet, ci ) );
    }
}
