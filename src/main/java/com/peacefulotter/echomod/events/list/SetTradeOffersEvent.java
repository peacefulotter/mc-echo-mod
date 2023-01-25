package com.peacefulotter.echomod.events.list;

import com.peacefulotter.echomod.events.Event;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record SetTradeOffersEvent(SetTradeOffersS2CPacket packet, CallbackInfo ci) implements Event
{
}
