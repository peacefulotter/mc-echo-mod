package com.peacefulotter.echomod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;

@Mixin( ClientPlayerEntity.class )
public abstract class FlyHackMixin extends Entity
{
    private static final float FLY_UP = 0.419999f;
    private static final int MAX_FLY_TICKS = 39;

    @Shadow public Input input;
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    private Entity mount;
    private boolean mounted = false;
    private int tick = 0;

    public FlyHackMixin( EntityType<?> type, World world )
    {
        super( type, world );
    }

    @Inject(at=@At("RETURN"), method="startRiding" )
    private void onStartRiding( Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir )
    {
        if ( !cir.getReturnValue() ) return; // accepted to mount?
        mounted = true;
        mount = entity;
    }

    @Inject(at=@At( "RETURN" ), method="dismountVehicle")
    private void onDismountVehicle( CallbackInfo ci )
    {
        mounted = false;
    }

    @Inject(at=@At( "RETURN" ), method="tick")
    private void onTick( CallbackInfo ci )
    {
        if ( mounted && tick >= MAX_FLY_TICKS )
        {
            Vec3d vel = mount.getVelocity();
            mount.setVelocity( vel.getX(), -FLY_UP, vel.getZ() );
            tick = 0;
        }
        else
            tick++;
    }

    @Inject(at=@At( "RETURN" ), method="tickMovement")
    private void onTickMovement( CallbackInfo ci )
    {
        if (!mounted || !this.input.jumping) return;

        // Vec3d vec = getPos().add( 0, MOUNT_FLY_SPEED, 0 );
        // PlayerConnectionInvoker conn = (PlayerConnectionInvoker) networkHandler.getConnection();
        // PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.PositionAndOnGround( vec.x, vec.y, vec.z, false );
        // conn.sendImm( packet, null );

        CLIENT_LOGGER.info("tick: " + tick);
        mount.updatePosition( mount.getX(), mount.getY() + FLY_UP, mount.getZ() );
        this.networkHandler.sendPacket( new VehicleMoveC2SPacket( mount ) );
    }

}
