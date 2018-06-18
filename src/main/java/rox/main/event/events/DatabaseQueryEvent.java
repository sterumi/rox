package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

public class DatabaseQueryEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private String query;

    public DatabaseQueryEvent(String query) {
        this.query = query;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public String getQueryString() {
        return query;
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
