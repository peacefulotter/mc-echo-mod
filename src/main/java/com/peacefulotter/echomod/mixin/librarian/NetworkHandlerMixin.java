package com.peacefulotter.echomod.mixin.librarian;

import com.peacefulotter.echomod.EchoModClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;
import static com.peacefulotter.echomod.config.ConfigManager.AUTO_LIBRARIAN;
import static net.minecraft.item.Items.ENCHANTED_BOOK;

@Mixin( ClientPlayNetworkHandler.class)
public class NetworkHandlerMixin
{
    @Inject(at=@At ( "HEAD" ), method="onOpenScreen", cancellable = true )
    public void onOpenScreen( OpenScreenS2CPacket packet, CallbackInfo ci )
    {
        if ( !AUTO_LIBRARIAN.getActive() ) return;

        CLIENT_LOGGER.info( "ClientPlayNetworkHandler + onOpenScreen" );
        ci.cancel();
    }

    private static void handleTradeOffer( TradeOfferList offers)
    {
        for ( TradeOffer offer : offers )
        {
            ItemStack stack = offer.getSellItem();

            if ( !stack.getItem().equals( ENCHANTED_BOOK ) ) continue;

            NbtList nbt = (NbtList) stack.getOrCreateNbt().get("StoredEnchantments");
            NbtCompound compound = (NbtCompound) nbt.get( 0 );
            String id = compound.getString( "id" ).replace( "minecraft:", "" );
            short lvl = compound.getShort("lvl");

            EchoModClient.sendMessage( "Trading: " + id + " " + lvl );
            return;
        }

        EchoModClient.sendMessage( "Trading: Nothing" );
    }


    // Handles the change of offers

    @Inject( at = @At( "RETURN" ), method = "onSetTradeOffers", cancellable = true )
    public void onSetTradeOffers( SetTradeOffersS2CPacket packet, CallbackInfo ci )
    {
        if ( !AUTO_LIBRARIAN.getActive() ) return;

        CLIENT_LOGGER.info( "ClientPlayNetworkHandler + onSetTradeOffers: " + packet.getOffers() );
        handleTradeOffer(  packet.getOffers() );
        ci.cancel();
    }
}
