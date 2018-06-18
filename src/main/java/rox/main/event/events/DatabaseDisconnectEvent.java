package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

public class DatabaseDisconnectEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;


    public DatabaseDisconnectEvent() {
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    @Override
    public IHandler getHandler() {
        return list;
    }

    public static IHandler getHandlerList() {
        return list;
    }
}
