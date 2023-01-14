package com.peacefulotter.echomod.utils;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler
{
    public interface Task
    {
        void apply();
    }

    private static final Timer timer = new Timer("scheduler");

    public static void schedule( Task task, int delay )
    {
        timer.schedule( new TimerTask()
        {
            @Override
            public void run()
            {
                task.apply();
            }
        }, delay );
    }
}
