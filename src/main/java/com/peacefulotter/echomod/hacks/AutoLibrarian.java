package com.peacefulotter.echomod.hacks;

import com.peacefulotter.echomod.EchoModClient;
import com.peacefulotter.echomod.LibrarianCache;
import com.peacefulotter.echomod.events.EventManager;
import com.peacefulotter.echomod.events.list.OpenScreenEvent;
import com.peacefulotter.echomod.events.list.PlayerTickEvent;
import com.peacefulotter.echomod.events.list.SetTradeOffersEvent;
import com.peacefulotter.echomod.events.list.PlayerInteractEntityEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerProfession;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;
import static com.peacefulotter.echomod.config.ConfigManager.AUTO_LIBRARIAN;
import static net.minecraft.item.Items.ENCHANTED_BOOK;

public final class AutoLibrarian
{
    private boolean state = false;

    AutoLibrarian()
    {
        new EventManager.Factory( AUTO_LIBRARIAN )
            .addListener( PlayerTickEvent.class, this::onClientPlayerEntityTick )
            .addListener( PlayerInteractEntityEvent.class, this::onPlayerInteractEntity )
            .addListener( SetTradeOffersEvent.class, this::onSetTradeOffers )
            .addListener( OpenScreenEvent.class, this::onOpenScreen );
    }

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

    private void onClientPlayerEntityTick( PlayerTickEvent e )
    {
        if ( LibrarianCache.target == null ) return;

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

    // Setup required to get the target entity
    // Player interacts once with the villager to set it as the target
    private void onPlayerInteractEntity( PlayerInteractEntityEvent event )
    {
        if ( !AUTO_LIBRARIAN.getActive() || !(event.entity() instanceof VillagerEntity ) ) return;
        EchoModClient.sendMessage( "Setting Villager as Librarian target" );
        LibrarianCache.target = (VillagerEntity) event.entity();
    }

    // Handles the change of offers
    private void onSetTradeOffers( SetTradeOffersEvent event )
    {
        TradeOfferList offers = event.packet().getOffers();
        boolean found = false;
        for ( TradeOffer offer : offers )
        {
            ItemStack stack = offer.getSellItem();

            if ( !stack.getItem().equals( ENCHANTED_BOOK ) ) continue;

            NbtList nbt = (NbtList) stack.getOrCreateNbt().get("StoredEnchantments");
            NbtCompound compound = (NbtCompound) nbt.get( 0 );
            String id = compound.getString( "id" ).replace( "minecraft:", "" );
            short lvl = compound.getShort("lvl");

            EchoModClient.sendMessage( "Trading: " + id + " " + lvl );
            found = true;
        }

        if (!found)
            EchoModClient.sendMessage( "Trading: Nothing" );

        event.ci().cancel();
    }

    private void onOpenScreen( OpenScreenEvent event )
    {
        event.ci().cancel();
    }
}
