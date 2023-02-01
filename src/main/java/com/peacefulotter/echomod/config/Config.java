package com.peacefulotter.echomod.config;

import com.peacefulotter.echomod.gui.MenuColors;
import com.peacefulotter.echomod.gui.SliderParams;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config
{
    public static final MenuColors DEFAULT_TEXT_COLOR = MenuColors.VENETIAN_RED;
    public static final MenuColors ACTIVE_TEXT_COLOR = MenuColors.MALACHITE;

    private final List<ConfigChangeCallback> listeners;
    private final String name;
    private boolean active;

    Config(String name, boolean active)
    {
        this.name = name;
        this.active = active;
        this.listeners = new ArrayList<>();
    }

    public void onChange( ConfigChangeCallback cb )
    {
        this.listeners.add( cb );
    }

    public void toggle() {
        active = !active;
        this.listeners.forEach( l -> l.apply( active ) );
    }

    public boolean get() { return active; }
    public String getName() { return name; }
    public List<SliderParams> getWidgetParams() { return Collections.emptyList(); }
}

