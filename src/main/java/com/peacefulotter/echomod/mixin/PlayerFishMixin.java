package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.utils.Bot;
import com.peacefulotter.echomod.utils.Scheduler;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;
import static com.peacefulotter.echomod.config.ConfigManager.AUTO_FISH;


@Mixin( FishingBobberEntity.class)
public class PlayerFishMixin
{
    @Shadow private boolean caughtFish;

    @Inject(at=@At("RETURN"), method="onTrackedDataSet")
    public void onTrackedDataSet( TrackedData<?> data, CallbackInfo ci )
    {
        CLIENT_LOGGER.info( "AutoFish active: " + AUTO_FISH.getActive() + ", caught?: " + this.caughtFish + ", data: " + data );
        if ( !AUTO_FISH.getActive() ) return;

        if ( this.caughtFish )
        {
            Bot.useHandItem();
            Scheduler.schedule( Bot::useHandItem, 100 );
        }
    }
}
