package com.peacefulotter.echomod.events.list;

import com.peacefulotter.echomod.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public record PlayerInteractEntityEvent(PlayerEntity player, Entity entity, Hand hand) implements Event
{
}
