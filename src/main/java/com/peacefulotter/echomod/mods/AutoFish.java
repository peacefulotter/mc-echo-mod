package com.peacefulotter.echomod.mods;

import net.minecraft.entity.projectile.FishingBobberEntity;

public class AutoFish
{
    private boolean isInOpenWater( FishingBobberEntity bobber )
    {
        return bobber.isInOpenWater();
    }

}
