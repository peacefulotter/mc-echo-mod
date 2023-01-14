package com.peacefulotter.echomod.config;

public class Config
{
    private boolean active = false;

    public Config(boolean active)
    {
        this.active = active;
    }

    public Config() {}

    public void toggle() { active = !active; }
    public boolean get() { return active; }
}

