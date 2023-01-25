package com.peacefulotter.echomod.events;

import com.peacefulotter.echomod.config.ConfigManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;

public final class EventManager
{
    private static final Map<Class<?>, List<Listener<?>>> manager = new HashMap<>();

    public static <E extends Event> void addListener( ConfigManager config, Class<E> eventClass, Listener<E> listener )
    {
        config.addListenerConfigDependency( listener );
        List<Listener<?>> listeners = manager.getOrDefault( eventClass, new ArrayList<>() );
        listeners.add( listener );
        manager.put( eventClass, listeners );
    }

    @SuppressWarnings("unchecked")
    public static <E extends Event> void fire( E event )
    {
        manager.getOrDefault( event.getClass(), new ArrayList<>() ).forEach( l -> {
            Listener<E> listener = (Listener<E>) l;
            if ( !ConfigManager.isEventEnabled( listener ) ) return;
            listener.call( event );
        } );
    }

    public static class Factory
    {
        private final ConfigManager config;

        public Factory( ConfigManager config )
        {
            this.config = config;
        }

        public <E extends Event> Factory addListener( Class<E> eventClass, Listener<E> listener )
        {
            EventManager.addListener( config, eventClass, listener );
            return this;
        }
    }
}
