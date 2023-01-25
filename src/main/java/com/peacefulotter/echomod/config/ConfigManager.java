package com.peacefulotter.echomod.config;

import com.peacefulotter.echomod.events.Event;
import com.peacefulotter.echomod.events.Listener;

import java.util.HashMap;
import java.util.Map;

public enum ConfigManager
{
    BOAT_FLY_HACK( new BoatFlyHackConfig() ),
    X_RAY( new XRayConfig() ),
    AUTO_FISH( new Config( "AutoFish", true ) ),
    AUTO_LIBRARIAN( new Config( "AutoLibrarian", true ) ),
    ENTITY_NAMETAG( new Config( "EntityNametag", true ) );

    private static final Map<Listener<?>, ConfigManager> listenerConfigDependencies = new HashMap<>();
    private final Config config;

    ConfigManager( Config config )
    {
        this.config = config;
    }

    public boolean getActive() { return config.get(); }
    public Config getConfig() { return config; }

    public <E extends Event> void addListenerConfigDependency( Listener<E> listener )
    {
        listenerConfigDependencies.put( listener, this );
    }

    public static <E extends Event> boolean isEventEnabled( Listener<E> listener )
    {
        ConfigManager cm = listenerConfigDependencies.get( listener );
        return cm == null || cm.getActive();
    }
}
