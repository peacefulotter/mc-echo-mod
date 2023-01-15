package com.peacefulotter.echomod.config;

public class Config
{
    private final String name;
    private boolean active = false;

    Config(String name, boolean active)
    {
        this.name = name;
        this.active = active;
    }

    Config(String name) { this(name, false); }

    public void toggle() { active = !active; }
    public boolean get() { return active; }

    public String getName() { return name; }
}

