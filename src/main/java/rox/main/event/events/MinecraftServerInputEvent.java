package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class MinecraftServerInputEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private Object[] objects;

    private String input;


    public MinecraftServerInputEvent(Object[] objects, String input) {
        this.objects = objects;
        this.input = input;
    }

    public Socket getSocket() {
        return (Socket) objects[0];
    }

    public Thread getInputHandlerThread() {
        return (Thread) objects[1];
    }

    public BufferedReader getBufferedReader() {
        return (BufferedReader) objects[2];
    }

    public PrintWriter getPrintWriter() {
        return (PrintWriter) objects[3];
    }

    public UUID getUUID() {
        return (UUID) objects[4];
    }

    public String getServerName() {
        return (String) objects[5];
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
