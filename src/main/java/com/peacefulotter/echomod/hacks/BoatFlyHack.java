package com.peacefulotter.echomod.hacks;

import com.peacefulotter.echomod.EchoModClient;
import com.peacefulotter.echomod.config.BoatFlyHackConfig;
import com.peacefulotter.echomod.events.EventManager;
import com.peacefulotter.echomod.events.list.*;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;
import static com.peacefulotter.echomod.config.ConfigManager.BOAT_FLY_HACK;

public final class BoatFlyHack
{
    private static final double ANTI_KICK_Y = -0.031251D;

    private final BoatFlyHackConfig config;
    private static Entity mount;
    private static boolean mounted = false;
    private static int tick = 0;

    BoatFlyHack()
    {
        this.config = (BoatFlyHackConfig) BOAT_FLY_HACK.getConfig();
        new EventManager.Factory( BOAT_FLY_HACK )
            .addListener( BoatUpdateVelocityEvent.class, this::onBoatUpdateVelocity )
            .addListener( PlayerStartRidingEvent.class, this::onStartRiding )
            .addListener( PlayerStopRidingEvent.class, this::onStopRiding )
            .addListener( PlayerTickEvent.class, this::onPlayerTick )
            .addListener( BoatEntityRenderEvent.class, this::onEntityShouldRender )
            .addListener( PlayerTickMovementEvent.class, this::onPlayerTickMovement );
    }

    private void sendMovePacket( ClientPlayNetworkHandler networkHandler, int sign )
    {
        mount.setPosition( mount.getPos().add( 0, ANTI_KICK_Y * sign, 0 ) );
        networkHandler.sendPacket( new VehicleMoveC2SPacket( mount ) );
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

    private void onBoatUpdateVelocity( BoatUpdateVelocityEvent e )
    {
    }

    private void onStartRiding( PlayerStartRidingEvent e )
    {
        if ( !(e.entity() instanceof BoatEntity ) ) return;
        mounted = true;
        mount = e.entity();
        tick = 0;
    }

    private void onStopRiding( PlayerStopRidingEvent e )
    {
        mounted = false;
        mount = null;
    }

    private void onPlayerTick( PlayerTickEvent e )
    {
        if ( !mounted ) return;

        if ( tick >= config.getMaxFlyTicks() )
        {
            tick = 0;
            sendMovePacket( e.networkHandler(), -1 );
        }
        else if ( tick == 1 )
        {
            sendMovePacket( e.networkHandler(), 1 );
        }

        tick++;
    }

    private void onPlayerTickMovement( PlayerTickMovementEvent e )
    {
        if ( !mounted ) return;

        // Vec3d vec = getPos().add( 0, MOUNT_FLY_SPEED, 0 );
        // PlayerConnectionInvoker conn = (PlayerConnectionInvoker) networkHandler.getConnection();
        // PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.PositionAndOnGround( vec.x, vec.y, vec.z, false );
        // conn.sendImm( packet, null );

        Vec3d rot = e.rot();
        double y = Math.min(1, (rot.getY() + (e.input().jumping ? 0.5 : 0))) * config.getFlyUp();
        if ( preventGroundMove() )
            y = Math.max( 0, y );
        if ( !e.input().pressingForward && !e.input().jumping )
            y = 0;

        double x = 0;
        double z = 0;
        mount.setVelocity( 0, 0, 0 );

        if ( e.input().pressingForward )
        {
            x += rot.getX();
            z += rot.getZ();
        }
        if ( e.input().pressingBack )
        {
            x -= rot.getX();
            z -= rot.getZ();
        }
        if ( e.input().pressingLeft )
        {
            Vec3d leftRot = rot.rotateY( (float) Math.PI / 2f );
            x += leftRot.getX();
            z += leftRot.getZ();
        }
        if ( e.input().pressingRight )
        {
            Vec3d rightRot = rot.rotateY( -(float) Math.PI / 2f );
            x += rightRot.getX();
            z += rightRot.getZ();
        }

        double len = new Vec3d( x, y, z ).length();
        if ( len == 0 ) return;

        Vec3d finalVel = new Vec3d( x, y, z ).multiply( config.getMaxVel() / len );
        mount.setVelocity( finalVel );
        mount.setYaw( e.yaw() );
        e.networkHandler().sendPacket( new VehicleMoveC2SPacket( mount ) );
    }

    private void onEntityShouldRender( BoatEntityRenderEvent e )
    {
        if ( !mounted ) return;
        CLIENT_LOGGER.info("onEntityShouldRender " + mount + " " + e.entity() );
        e.ci().cancel();
    }
}
