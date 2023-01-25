


import com.peacefulotter.echomod.config.ConfigManager;
import com.peacefulotter.echomod.events.Event;
import com.peacefulotter.echomod.events.EventManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class EventManagerTest
{
    record SimpleEvent(String name, int age, LocalDateTime date) implements Event
    {
    }

    @Test
    public void eventsListenerTriggered()
    {
        EventManager.addListener( ConfigManager.X_RAY, SimpleEvent.class, (event) -> {
            System.out.println( "X_RAY: " + event );
        } );
        EventManager.addListener( ConfigManager.AUTO_FISH, SimpleEvent.class, (event) -> {
            System.out.println( "AUTO_FISH: " + event );
        }  );

        ConfigManager.X_RAY.getConfig().toggle();

        EventManager.fire( new SimpleEvent( "BipBoop", 42, LocalDateTime.now() ) );
    }
}
