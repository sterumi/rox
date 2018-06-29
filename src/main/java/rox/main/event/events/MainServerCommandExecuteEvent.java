package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class MainServerCommandExecuteEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private Object[] objects, args;

    private String command;


    public MainServerCommandExecuteEvent(Object[] objects, String command, String[] args) {
    }

    public String getCommand() {
        return command;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getUsername() {
        return (String) objects[0];
    }

    public Socket getSocket() {
        return (Socket) objects[1];
    }

    public Thread getClientInputThread() {
        return (Thread) objects[2];
    }

    public BufferedReader getBufferedReader() {
        return (BufferedReader) objects[3];
    }

    public PrintWriter getPrintWriter() {
        return (PrintWriter) objects[4];
    }

    public String getRank() {
        return (String) objects[5];
    }

    public UUID getUUID() {
        return (UUID) objects[6];
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
