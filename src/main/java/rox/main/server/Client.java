package rox.main.server;
/*
Created by Bleikind
*/

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class Client {

    private String name, rank;
    private final Socket socket;
    private Thread inputThread;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final UUID uuid;

    public Client(String username, Socket socket, BufferedReader reader, PrintWriter writer, UUID uuid, String rank){
        this.name = username;
        this.rank = rank;
        this.socket = socket;
        this.writer = writer;
        this.uuid = uuid;
        this.reader = reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public String getName() {
        return name;
    }

    public Thread getInputThread() {
        return inputThread;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getRank() {
        return rank;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setInputThread(Thread inputThread) {
        this.inputThread = inputThread;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
