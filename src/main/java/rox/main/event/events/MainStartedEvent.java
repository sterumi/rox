package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

public class MainStartedEvent extends Event {

    private static IHandler list = new IHandler();

    @Override
    public IHandler getHandler() {
        return list;
    }

    public static IHandler getHandlerList() {
        return list;
    }
}
