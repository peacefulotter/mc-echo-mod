package com.peacefulotter.echomod.mixin.librarian;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;
import static com.peacefulotter.echomod.config.ConfigManager.AUTO_LIBRARIAN;

@Mixin( Merchant.class )
public interface MerchantMixin
{
    // Cancels the open menu screen when interacting with the villager

    @Inject( at=@At( "HEAD" ), method="sendOffers" )
    default void sendOffers( PlayerEntity player, Text test, int levelProgress, CallbackInfo ci )
    {
        if ( !AUTO_LIBRARIAN.getActive() ) return;
        CLIENT_LOGGER.info("MerchantMixin + sendOffers");
//        TradeOfferList tradeOfferList = this.getOffers();
//        if (!tradeOfferList.isEmpty()) {
//            player.sendTradeOffers(optionalInt.getAsInt(), tradeOfferList, levelProgress, this.getExperience(), this.isLeveledMerchant(), this.canRefreshTrades());
//        }
    }
}
