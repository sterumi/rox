package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MinecraftServerAcceptEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private String[] input;

    public MinecraftServerAcceptEvent(Socket socket, BufferedReader reader, PrintWriter writer, String[] input) {
        this.socket = socket;
        this.input = input;
        this.reader = reader;
        this.writer = writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public Socket getSocket() {
        return socket;
    }

    public String[] getInput() {
        return input;
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
