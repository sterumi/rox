package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

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

    private Object[] clientObject;

    public MainServerClientConnectingEvent(Object[] clientObject) {
        this.clientObject = clientObject;
    }

    public String getName() {
        return (String) clientObject[0];
    }

    public Socket getSocket() {
        return (Socket) clientObject[1];
    }

    public Thread getClientInputThread() {
        return (Thread) clientObject[2];
    }

    public BufferedReader getBufferedReader() {
        return (BufferedReader) clientObject[3];
    }

    public PrintWriter getPrintWriter() {
        return (PrintWriter) clientObject[4];
    }

    public String getRank() {
        return (String) clientObject[5];
    }

    public UUID getUUID() {
        return (UUID) clientObject[6];
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }


}
