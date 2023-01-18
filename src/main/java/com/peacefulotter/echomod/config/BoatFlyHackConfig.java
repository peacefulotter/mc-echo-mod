package com.peacefulotter.echomod.config;

import com.peacefulotter.echomod.gui.SliderParams;

import java.util.List;

public class BoatFlyHackConfig extends Config
{
    private static final String name = "BoatFlyHack";
    private static final boolean active = true;

    public static double YAW_VELOCITY = 8d;
    public static double FLY_UP = 0.419999f;
    public static int MAX_FLY_TICKS = 39;

    BoatFlyHackConfig()
    {
        super( name, active );
        this.widgetParams.addAll( List.of(
            new SliderParams(
                "yaw_vel", 4d, 16d, YAW_VELOCITY,
                (v) -> BoatFlyHackConfig.YAW_VELOCITY = v
            ),
            new SliderParams(
                "fly_up", 0.2, 0.7, FLY_UP,
                (v) -> BoatFlyHackConfig.FLY_UP = v
            ),
            new SliderParams(
                "max_ticks", 20, 90, MAX_FLY_TICKS,
                (v) -> BoatFlyHackConfig.MAX_FLY_TICKS = v.intValue()
            )
        ) );
    }
}
