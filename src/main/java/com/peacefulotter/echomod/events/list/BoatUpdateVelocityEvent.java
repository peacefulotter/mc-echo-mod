package com.peacefulotter.echomod.events.list;

import com.peacefulotter.echomod.events.Event;
import com.peacefulotter.echomod.mixin.boat.BoatEntityMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record BoatUpdateVelocityEvent(BoatEntity boat, double e) implements Event
{
}
