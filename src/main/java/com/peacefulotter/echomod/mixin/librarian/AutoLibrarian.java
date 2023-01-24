package com.peacefulotter.echomod.mixin.librarian;

import com.peacefulotter.echomod.EchoModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
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
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.peacefulotter.echomod.config.ConfigManager.AUTO_LIBRARIAN;
import static net.minecraft.item.Items.ENCHANTED_BOOK;

/**
 * WORKS ONLY IN SINGLEPLAYER
 */

@Mixin( VillagerEntity.class)
public abstract class AutoLibrarian extends MerchantEntity
{
    public AutoLibrarian( EntityType<? extends MerchantEntity> entityType, World world )
    {
        super( entityType, world );
    }

    @Inject( at = @At( "RETURN" ), method = "fillRecipes" )
    public void onFillRecipes( CallbackInfo ci )
    {
        if (
            !AUTO_LIBRARIAN.getActive() ||
            offers == null || offers.size() == 0
        )
        {
            EchoModClient.sendMessage( "-" );
            return;
        }

        for ( TradeOffer offer : offers )
        {
            ItemStack stack = offer.getSellItem();

            if ( !stack.getItem().equals( ENCHANTED_BOOK ) ) continue;

            NbtList nbt = (NbtList) stack.getOrCreateNbt().get("StoredEnchantments");
            NbtCompound compound = (NbtCompound) nbt.get( 0 );
            String id = compound.getString( "id" ).replace( "minecraft:", "" );
            short lvl = compound.getShort("lvl");

            EchoModClient.sendMessage( "Found: " + id + " (" + lvl + ")" );
            return;
        }

        EchoModClient.sendMessage( "Not Found" );
    }

    @Inject( at = @At( "RETURN" ), method = "setVillagerData" )
    public void setVillagerData( VillagerData villagerData, CallbackInfo ci )
    {
        if ( villagerData.getProfession().equals( VillagerProfession.NONE ) )
        {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            int rotX = (int) Math.ceil( player.getRotationVector().getX() );
            int rotZ = (int) Math.ceil( player.getRotationVector().getZ() );
            Vec3d pos = new Vec3d( player.getX() + rotX, player.getY(), player.getZ() + rotZ );
            BlockHitResult hit = new BlockHitResult( pos, Direction.UP, new BlockPos( pos ), false );
            MinecraftClient.getInstance().interactionManager.interactBlock( player, Hand.OFF_HAND, hit );
        }
    }
}
