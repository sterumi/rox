package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

import java.util.UUID;

public class MainServerPermissionChangeEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private UUID uuid;

    private String beforeChange, newRank;

    private String name;

    public MainServerPermissionChangeEvent(UUID uuid, String beforeChange, String newRank) {
        this.uuid = uuid;
        this.beforeChange = beforeChange;
        this.newRank = newRank;
    }

    public MainServerPermissionChangeEvent(String name, String beforeChange, String newRank) {
        this.name = name;
        this.beforeChange = beforeChange;
        this.newRank = newRank;
    }

    public String getName() {
        return name;
    }

    public String getBeforeChange() {
        return beforeChange;
    }

    public String getNewRank() {
        return newRank;
    }

    public UUID getUUID() {
        return uuid;
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
