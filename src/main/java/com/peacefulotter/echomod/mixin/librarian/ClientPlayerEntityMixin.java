package com.peacefulotter.echomod.mixin.librarian;

import com.peacefulotter.echomod.EchoModClient;
import com.peacefulotter.echomod.LibrarianCache;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.SimpleMerchant;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;
import static com.peacefulotter.echomod.config.ConfigManager.AUTO_LIBRARIAN;

@Mixin( ClientPlayerEntity.class )
public class ClientPlayerEntityMixin
{
    private boolean state = false;

    private void placeLectern( ClientPlayerEntity player, ClientPlayerInteractionManager manager )
    {
        int rotX = (int) Math.ceil( player.getRotationVector().getX() );
        int rotZ = (int) Math.ceil( player.getRotationVector().getZ() );
        Vec3d pos = new Vec3d( player.getX() + rotX, player.getY(), player.getZ() + rotZ );
        BlockHitResult hit = new BlockHitResult( pos, Direction.UP, new BlockPos( pos ), false );
        manager.interactBlock( player, Hand.OFF_HAND, hit );
    }

    private void interactWithVillager( ClientPlayerEntity player, ClientPlayerInteractionManager manager )
    {
        manager.interactEntity( player, LibrarianCache.target, Hand.OFF_HAND );
    }

    @Inject( at = @At( "RETURN" ), method = "tick" )
    public void tick( CallbackInfo ci )
    {
        if ( !AUTO_LIBRARIAN.getActive() || LibrarianCache.target == null ) return;

        ClientPlayerEntity player = EchoModClient.getPlayer();
        ClientPlayerInteractionManager manager = EchoModClient.getInteractionManager();
        VillagerProfession profession = LibrarianCache.target.getVillagerData().getProfession();

        if ( profession.equals( VillagerProfession.NONE ) && !state )
        {
            CLIENT_LOGGER.info( "Placing lectern.." );
            placeLectern( player, manager );
            state = true;
        }
        else if ( !profession.equals( VillagerProfession.NONE ) && state )
        {
            CLIENT_LOGGER.info( "Interacting with target.." );
            interactWithVillager( player, manager );
            state = false;
        }
    }
}
