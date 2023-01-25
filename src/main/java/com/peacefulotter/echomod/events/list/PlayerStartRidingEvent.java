package com.peacefulotter.echomod.events.list;

import com.peacefulotter.echomod.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public record PlayerStartRidingEvent( Entity entity ) implements Event
{
}
