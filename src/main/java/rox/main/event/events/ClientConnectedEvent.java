package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;
import rox.main.server.permission.Rank;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ClientConnectedEvent extends Event {

    private static IHandler list = new IHandler();

    @Override
    public IHandler getHandler() {
        return list;
    }

    public static IHandler getHandlerList() {
        return list;
    }

    private Object[] clientObject;

    public ClientConnectedEvent(Object[] clientObject) {
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

    public Rank getRank() {
        return (Rank) clientObject[5];
    }

    public UUID getUUID() {
        return (UUID) clientObject[6];
    }


}
