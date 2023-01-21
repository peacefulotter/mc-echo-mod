package com.peacefulotter.echomod.config;

import com.peacefulotter.echomod.gui.SliderParams;

import java.util.List;

public class LibrarianConfig extends Config
{
    private static final String name = "AutoLibrarian";
    private static final boolean active = false;

    private int maxFlyTicks = 39;

    LibrarianConfig()
    {
        super( name, active );
    }

    @Override
    public List<SliderParams> getWidgetParams()
    {
        return List.of(
            new SliderParams(
                "max_ticks", 40, 81, maxFlyTicks,
                (v) -> maxFlyTicks = v.intValue()
            )
        );
    }

    public int getMaxFlyTicks()
    {
        return maxFlyTicks;
    }
}
