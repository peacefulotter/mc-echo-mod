package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.item.Items.ENCHANTED_BOOK;

@Mixin( VillagerEntity.class)
public abstract class AutoLibrarian extends MerchantEntity
{
    public AutoLibrarian( EntityType<? extends MerchantEntity> entityType, World world )
    {
        super( entityType, world );
    }

    private void sendMessage(String msg)
    {
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(
            Text.literal( msg ),
            true
        );
    }

    @Inject( at = @At( "RETURN" ), method = "fillRecipes" )
    public void onFillRecipes( CallbackInfo ci )
    {
        if (
            !ConfigManager.AUTO_LIBRARIAN.getActive() ||
            offers == null || offers.size() == 0
        ) return;

        for ( TradeOffer offer : offers )
        {
            ItemStack stack = offer.getSellItem();

            if ( !stack.getItem().equals( ENCHANTED_BOOK ) ) continue;

            NbtList nbt = (NbtList) stack.getOrCreateNbt().get("StoredEnchantments");
            NbtCompound compound = (NbtCompound) nbt.get( 0 );
            String id = compound.getString( "id" ).replace( "minecraft:", "" );
            short lvl = compound.getShort("lvl");

            sendMessage( "Found: " + id + " (" + lvl + ")" );
            return;
        }

        sendMessage( "Not Found" );
    }
}
