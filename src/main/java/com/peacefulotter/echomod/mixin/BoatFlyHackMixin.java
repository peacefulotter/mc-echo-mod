package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.EchoModClient;
import com.peacefulotter.echomod.config.BoatFlyHackConfig;
import com.peacefulotter.echomod.config.Config;
import com.peacefulotter.echomod.config.ConfigManager;
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

import static com.peacefulotter.echomod.config.ConfigManager.BOAT_FLY_HACK;

@Mixin( ClientPlayerEntity.class )
public abstract class BoatFlyHackMixin extends Entity
{
    private static final double ANTI_KICK_Y = -0.03125D;

    @Shadow public Input input;
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Shadow public abstract float getPitch( float tickDelta );

    private final BoatFlyHackConfig config;
    private Entity mount;
    private boolean mounted = false;
    private int tick = 0;

    public BoatFlyHackMixin( EntityType<?> type, World world )
    {
        super( type, world );
        this.config = (BoatFlyHackConfig) BOAT_FLY_HACK.getConfig();
    }

    @Inject(at=@At("RETURN"), method="startRiding" )
    private void onStartRiding( Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir )
    {
        if ( !BOAT_FLY_HACK.getActive() || !cir.getReturnValue() || !(entity instanceof BoatEntity) ) return; // accepted to mount?
        mounted = true;
        mount = entity;
        tick = 0;
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

        // double y = 0;

        if ( tick >= config.getMaxFlyTicks() )
        {
            // y = -FLY_UP;
            tick = 0;
            mount.setPosition( mount.getPos().subtract( 0, ANTI_KICK_Y, 0 ) );
            this.networkHandler.sendPacket( new VehicleMoveC2SPacket( mount ) );
//            PlayerConnectionInvoker handler = (PlayerConnectionInvoker) EchoModClient.getPlayer().networkHandler.getConnection();
//            VehicleMoveC2SPacket packet = new VehicleMoveC2SPacket( mount );
//            handler.sendImm( packet, null );
        }
        else if ( tick == 1 )
        {
            mount.setPosition( mount.getPos().add( 0, ANTI_KICK_Y, 0 ) );
            this.networkHandler.sendPacket( new VehicleMoveC2SPacket( mount ) );
        }
//            y = FLY_UP;
//
//        Vec3d vel = mount.getVelocity();
//        mount.setVelocity( vel.getX(), y, vel.getZ() );
        tick++;
    }

    private boolean preventGroundMove()
    {
        ClientPlayerEntity player = EchoModClient.getPlayer();
        BlockPos pos = player.getBlockPos();
        int elevation = 0;
        while ( player.world.getBlockState( pos.add( 0, -(elevation++), 0 ) ).isAir() ) {}

        double y = pos.getY();
        double decimalsY = (y - (int)y);

        double GROUND_Y_THRESHOLD = 0.1;
        return elevation <= 1 && decimalsY < GROUND_Y_THRESHOLD;
    }

    @Inject(at=@At( "RETURN" ), method="tickMovement")
    private void onTickMovement( CallbackInfo ci )
    {
        if ( !BOAT_FLY_HACK.getActive() || !mounted ) return;

        // Vec3d vec = getPos().add( 0, MOUNT_FLY_SPEED, 0 );
        // PlayerConnectionInvoker conn = (PlayerConnectionInvoker) networkHandler.getConnection();
        // PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.PositionAndOnGround( vec.x, vec.y, vec.z, false );
        // conn.sendImm( packet, null );

        Vec3d rot = this.getRotationVector();
        double y = Math.min(1, (rot.getY() + (this.input.jumping ? 0.5 : 0))) * config.getFlyUp();
        if ( preventGroundMove() )
            y = Math.max( 0, y );
        if ( !this.input.pressingForward && !this.input.jumping )
            y = 0;

        double x = 0;
        double z = 0;
        mount.setVelocity( 0, 0, 0 );

        if ( this.input.pressingForward )
        {
            x += rot.getX();
            z += rot.getZ();
        }
        if ( this.input.pressingBack )
        {
            x -= rot.getX();
            z -= rot.getZ();
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

        double len = new Vec3d( x, y, z ).length();
        if ( len == 0 ) return;

        Vec3d finalVel = new Vec3d( x, y, z ).multiply( config.getMaxVel() / len );
        mount.setVelocity( finalVel );
        mount.setYaw( getYaw() );
        this.networkHandler.sendPacket( new VehicleMoveC2SPacket( mount ) );
    }

}
