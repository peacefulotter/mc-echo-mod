package com.peacefulotter.echomod.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;

import static com.peacefulotter.echomod.config.ConfigManager.X_RAY;
import static com.peacefulotter.echomod.config.XRayConfig.BANNED_ITEMS;
import static com.peacefulotter.echomod.config.XRayConfig.INTERESTED_ITEMS;

@Mixin( AbstractBlock.AbstractBlockState.class )
public abstract class XRayHack
{
    @Shadow public abstract Block getBlock();

    private void callback( CallbackInfoReturnable<Boolean> cir )
    {
        callback( cir, (b, i) -> b );
    }

    private <T> void callback( CallbackInfoReturnable<T> cir, BiFunction<Boolean, Boolean, T> cb )
    {
        if ( !X_RAY.getActive() ) return;
        Item item = getBlock().asItem();
        boolean banned = BANNED_ITEMS.contains( item );
        boolean interested = INTERESTED_ITEMS.contains( item );
        T res = cb.apply( banned, interested );
        if ( res != null )
            cir.setReturnValue( res );
    }

    @Inject( at=@At( "HEAD" ), method = "isSideInvisible", cancellable = true )
    public void isSideInvisible( BlockState state, Direction direction, CallbackInfoReturnable<Boolean> cir )
    {
        callback( cir );
    }

    @Inject( at=@At( "HEAD" ), method = "isTranslucent", cancellable = true )
    public void isTranslucent( BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir )
    {
        callback( cir );
    }

    @Inject( at=@At( "HEAD" ), method = "getOpacity", cancellable = true )
    public void getOpacity( BlockView world, BlockPos pos, CallbackInfoReturnable<Integer> cir )
    {
        callback( cir, (b, i) -> b ? 0 : 1 );
    }

    @Inject( at=@At( "HEAD" ), method = "isOpaque", cancellable = true )
    public void isOpaque( CallbackInfoReturnable<Boolean> cir )
    {
        callback( cir, (b, i) -> !b );
    }

    @Inject( at=@At( "HEAD" ), method = "isOpaqueFullCube", cancellable = true )
    public void isOpaqueFullCube( CallbackInfoReturnable<Boolean> cir )
    {
        callback( cir, (b, i) -> !b );
    }

    @Inject( at=@At( "HEAD" ), method = "hasSidedTransparency", cancellable = true )
    public void hasSidedTransparency( CallbackInfoReturnable<Boolean> cir )
    {
        callback( cir, (b, i) -> b );
    }

    @Inject( at=@At( "HEAD" ), method = "getLuminance", cancellable = true )
    public void getLuminance( CallbackInfoReturnable<Integer> cir )
    {
        callback( cir, (b, i) -> b ? Integer.valueOf( 0 ) : (i ? 12 : null) );
    }

    @Inject( at=@At( "HEAD" ), method = "getCullingFace", cancellable = true )
    public void getCullingFace( BlockView world, BlockPos pos, Direction direction, CallbackInfoReturnable<VoxelShape> cir ) {
        callback( cir, (b, i) -> b ? VoxelShapes.empty() : (i ? VoxelShapes.fullCube() : null) );
    }

    @Inject( at=@At( "HEAD" ), method = "getAmbientOcclusionLightLevel", cancellable = true )
    public void getAmbientOcclusionLightLevel( BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir ) {
        callback( cir, (b, i) -> 1f );
    }
}