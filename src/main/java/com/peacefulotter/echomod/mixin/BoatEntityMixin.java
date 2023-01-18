package com.peacefulotter.echomod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.peacefulotter.echomod.config.BoatFlyHackConfig.YAW_VELOCITY;


@Mixin( BoatEntity.class )
public abstract class BoatEntityMixin extends Entity
{
    @Shadow private float velocityDecay;
    @Shadow private float yawVelocity;

    @Shadow private boolean pressingLeft;

    @Shadow private boolean pressingRight;

    public BoatEntityMixin( EntityType<?> type, World world )
    {
        super( type, world );
    }

    @Inject(
            method="updateVelocity",
            at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void updateVelocity( CallbackInfo ci, double d, double e )
    {
        this.velocityDecay = 0.95f;
        this.setVelocity(this.getVelocity().subtract( 0, e, 0 ));
    }

    @Inject( at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;setYaw(F)V"), method="updatePaddles" )
    private void updatePaddles( CallbackInfo ci )
    {
        this.yawVelocity = 0f;
        if ( this.pressingLeft ) this.yawVelocity -= YAW_VELOCITY;
        if ( this.pressingRight ) this.yawVelocity += YAW_VELOCITY;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void fall( double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition )
    {
        // CLIENT_LOGGER.info( heightDifference + " " + onGround + " " + state + " " + landedPosition );
    }


}
