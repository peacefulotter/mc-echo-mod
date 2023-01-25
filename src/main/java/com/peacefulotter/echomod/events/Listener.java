package com.peacefulotter.echomod.events;


public interface Listener<E extends Event>
{
    void call( E event );
}
