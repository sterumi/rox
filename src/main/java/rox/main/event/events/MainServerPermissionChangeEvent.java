package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;
import rox.main.server.permission.Rank;

import java.util.UUID;

public class MainServerPermissionChangeEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private UUID uuid;

    private Rank beforeChange, newRank;

    private String name;

    public MainServerPermissionChangeEvent(UUID uuid, Rank beforeChange, Rank newRank) {
        this.uuid = uuid;
        this.beforeChange = beforeChange;
        this.newRank = newRank;
    }

    public MainServerPermissionChangeEvent(String name, Rank beforeChange, Rank newRank) {
        this.name = name;
        this.beforeChange = beforeChange;
        this.newRank = newRank;
    }

    public String getName() {
        return name;
    }

    public Rank getBeforeChange() {
        return beforeChange;
    }

    public Rank getNewRank() {
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
