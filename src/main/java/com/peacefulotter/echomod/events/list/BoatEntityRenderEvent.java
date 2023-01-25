package com.peacefulotter.echomod.events.list;

import com.peacefulotter.echomod.events.Event;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record BoatEntityRenderEvent(BoatEntity entity, CallbackInfo ci ) implements Event
{
}
