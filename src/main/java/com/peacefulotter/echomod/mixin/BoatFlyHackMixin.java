package com.peacefulotter.echomod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.peacefulotter.echomod.config.BoatFlyHackConfig.FLY_UP;
import static com.peacefulotter.echomod.config.BoatFlyHackConfig.MAX_FLY_TICKS;
import static com.peacefulotter.echomod.config.ConfigManager.BOAT_FLY_HACK;

@Mixin( ClientPlayerEntity.class )
public abstract class BoatFlyHackMixin extends Entity
{


    @Shadow public Input input;
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Shadow public abstract float getPitch( float tickDelta );

    private Entity mount;
    private boolean mounted = false;
    private int tick = 0;

    public BoatFlyHackMixin( EntityType<?> type, World world )
    {
        super( type, world );
    }

    @Inject(at=@At("RETURN"), method="startRiding" )
    private void onStartRiding( Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir )
    {
        if ( !BOAT_FLY_HACK.getActive() || !cir.getReturnValue() || !(entity instanceof BoatEntity) ) return; // accepted to mount?
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
        if ( !BOAT_FLY_HACK.getActive() || !mounted ) return;

        double y = 0;

        if ( tick >= MAX_FLY_TICKS )
        {
            y = -FLY_UP;
            tick = 0;
        }
        else if ( tick == 1 )
            y = FLY_UP;

        Vec3d vel = mount.getVelocity();
        mount.setVelocity( vel.getX(), y, vel.getZ() );
        tick++;
    }

    private int getPlayerElevation()
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        BlockPos pos = player.getBlockPos();
        int elevation = 0;
        while ( player.world.getBlockState( pos.add( 0, -(elevation++), 0 ) ).isAir() ) {}
        return elevation;
    }

    @Inject(at=@At( "RETURN" ), method="tickMovement")
    private void onTickMovement( CallbackInfo ci )
    {
        if ( !BOAT_FLY_HACK.getActive() || !mounted ) return;

        // Vec3d vec = getPos().add( 0, MOUNT_FLY_SPEED, 0 );
        // PlayerConnectionInvoker conn = (PlayerConnectionInvoker) networkHandler.getConnection();
        // PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.PositionAndOnGround( vec.x, vec.y, vec.z, false );
        // conn.sendImm( packet, null );

        int elevation = getPlayerElevation();

        Vec3d rot = this.getRotationVector();
        double y = Math.min(1, (rot.getY() + (this.input.jumping ? 0.5 : 0))) * FLY_UP;
        if ( elevation == 1 )
            y = Math.max( 0, y );
        if ( !this.input.pressingForward && !this.input.jumping )
            y = 0;
        y += mount.getY();

        double x = mount.getX();
        double z = mount.getZ();
        if ( this.input.pressingForward )
        {
            x += rot.getX();
            z += rot.getZ();
        }
        if ( this.input.pressingLeft )
        {
            Vec3d leftRot = rot.rotateY( (float) Math.PI / 2f );
            x += leftRot.getX();
            z += leftRot.getZ();
        }
        if ( this.input.pressingRight )
        {
            Vec3d rightRot = rot.rotateY( -(float) Math.PI / 2f );
            x += rightRot.getX();
            z += rightRot.getZ();
        }

        mount.updatePosition( x, y, z );
        mount.setYaw( getYaw() % 360.0F );
        this.networkHandler.sendPacket( new VehicleMoveC2SPacket( mount ) );
    }

}
