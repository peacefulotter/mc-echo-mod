package com.peacefulotter.echomod.config;

public enum ConfigManager
{
    BOAT_FLY_HACK( new BoatFlyHackConfig() ),
    AUTO_FISH( new Config( "AutoFish", true ) ),
    AUTO_LIBRARIAN( new Config( "AutoLibrarian", true ) );

    private final Config config;

    ConfigManager( Config config )
    {
        this.config = config;
    }

    public boolean getActive() { return config.get(); }
    public Config getConfig() { return config; }
}
