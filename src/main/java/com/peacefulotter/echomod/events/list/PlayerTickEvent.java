package com.peacefulotter.echomod.events.list;

import com.peacefulotter.echomod.events.Event;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public record PlayerTickEvent(ClientPlayNetworkHandler networkHandler) implements Event
{
}
