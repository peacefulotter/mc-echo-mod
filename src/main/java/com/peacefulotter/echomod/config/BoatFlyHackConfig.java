package com.peacefulotter.echomod.config;

import com.peacefulotter.echomod.gui.SliderParams;

import java.util.List;

public class BoatFlyHackConfig extends Config
{
    private static final String name = "BoatFlyHack";
    private static final boolean active = true;

    private double maxVel = 1;
    private double flyUp = 0.419999f;
    private int maxFlyTicks = 39;

    BoatFlyHackConfig()
    {
        super( name, active );
    }

    @Override
    public List<SliderParams> getWidgetParams()
    {
        return List.of(
            new SliderParams(
                "max_vel", 0.4, 2, maxVel,
                (v) -> maxVel = v
            ),
            new SliderParams(
                "fly_up", 0.1, 1, flyUp,
                (v) -> flyUp = v
            ),
            new SliderParams(
                "max_ticks", 40, 81, maxFlyTicks,
                (v) -> maxFlyTicks = v.intValue()
            )
        );
    }

    public double getMaxVel()
    {
        return maxVel;
    }

    public double getFlyUp()
    {
        return flyUp;
    }

    public int getMaxFlyTicks()
    {
        return maxFlyTicks;
    }
}
