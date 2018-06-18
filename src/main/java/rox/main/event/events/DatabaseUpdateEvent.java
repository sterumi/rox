package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

public class DatabaseUpdateEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private String update;

    public DatabaseUpdateEvent(String update) {
        this.update = update;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public String getUpdateString() {
        return update;
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
