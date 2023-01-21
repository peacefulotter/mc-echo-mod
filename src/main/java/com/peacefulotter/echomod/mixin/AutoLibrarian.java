package com.peacefulotter.echomod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;
import static net.minecraft.item.Items.ENCHANTED_BOOK;

@Mixin( VillagerEntity.class)
public class AutoLibrarian
{
    @Inject( at = @At( "RETURN" ), method = "setVillagerData" )
    public void setVillagerData( VillagerData villagerData, CallbackInfo ci )
    {
        CLIENT_LOGGER.info( "setVillagerData: " + villagerData );
    }

    @Inject( at = @At( "RETURN" ), method = "setOffers" )
    public void setOffers( TradeOfferList offers, CallbackInfo ci )
    {
        CLIENT_LOGGER.info( "setOffers: " + offers );

        for ( TradeOffer offer : offers )
        {
            CLIENT_LOGGER.info( "offer: " + offer );
            CLIENT_LOGGER.info( "item: " + offer.getSellItem() );
            CLIENT_LOGGER.info( "first: " + offer.getOriginalFirstBuyItem() );
            CLIENT_LOGGER.info( "adj first: " + offer.getAdjustedFirstBuyItem() );
            CLIENT_LOGGER.info( "second: " + offer.getSecondBuyItem() );
            ItemStack stack = offer.getSellItem();
            Item item = stack.getItem();
            if (
                item.equals( ENCHANTED_BOOK )
            )
            {
                CLIENT_LOGGER.info( "Enchants: " + stack.getEnchantments() );
                for ( NbtElement enchantment : stack.getEnchantments() )
                {
                    // TODO: check target enchant + level
                    CLIENT_LOGGER.info( "enchantment: " + enchantment );
                }
            }
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        BlockHitResult result = new BlockHitResult(player.getPos(), player.getMovementDirection(), player.getBlockPos(), false);
        player.getActiveItem().useOnBlock( new ItemUsageContext( player, Hand.MAIN_HAND, result ) );
    }
}
