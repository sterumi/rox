package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;
import rox.main.server.Client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class MainServerClientConnectingEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    @Override
    public IHandler getHandler() {
        return list;
    }

    public static IHandler getHandlerList() {
        return list;
    }

    private Client client;

    public MainServerClientConnectingEvent(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }


}
