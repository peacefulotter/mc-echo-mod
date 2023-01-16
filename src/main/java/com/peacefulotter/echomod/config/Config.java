package com.peacefulotter.echomod.config;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Config
{
    public static final Color DEFAULT_TEXT_COLOR = Color.RED;
    public static final Color DEFAULT_BGRD_COLOR = Color.GRAY;
    public static final Color ACTIVE_TEXT_COLOR = Color.GREEN;
    public static final Color ACTIVE_BGRD_COLOR = Color.GRAY;

    private final List<ConfigChangeCallback> listeners;
    private final String name;
    private boolean active;

    Config(String name, boolean active)
    {
        this.name = name;
        this.active = active;
        this.listeners = new ArrayList<>();
    }

    Config(String name) { this(name, false); }

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
}

