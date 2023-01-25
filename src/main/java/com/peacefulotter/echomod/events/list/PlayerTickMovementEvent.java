package com.peacefulotter.echomod.events.list;

import com.peacefulotter.echomod.events.Event;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.math.Vec3d;

public record PlayerTickMovementEvent(ClientPlayNetworkHandler networkHandler, Input input, float yaw, Vec3d rot ) implements Event
{
}
