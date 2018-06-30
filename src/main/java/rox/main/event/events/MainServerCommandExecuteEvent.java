package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;
import rox.main.server.Client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class MainServerCommandExecuteEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private Client client;
    private String[] args;

    private String command;


    public MainServerCommandExecuteEvent(Client client, String command, String[] args) {
        this.client = client;
        this.command = command;
        this.args = args;
    }

    public Client getClient() {
        return client;
    }

    public String getCommand() {
        return command;
    }

    public Object[] getArgs() {
        return args;
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
