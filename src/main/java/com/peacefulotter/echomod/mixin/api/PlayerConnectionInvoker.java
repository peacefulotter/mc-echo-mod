package com.peacefulotter.echomod.mixin.api;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin( ClientConnection.class)
public interface PlayerConnectionInvoker
{
    @Invoker("sendImmediately")
    void sendImm( Packet<?> packet, @Nullable PacketCallbacks callbacks );
}
