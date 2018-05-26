package rox.main.server;

import rox.main.server.database.MainDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer {

    private ServerSocket serverSocket;

    private int port;

    private boolean waitingConnection = true, isActive = false;

    private ConcurrentHashMap<String, Object[]> clients = new ConcurrentHashMap<>();

    private MainDatabase database;

    private Thread acceptThread;

    public MainServer(int port){
        this.port = port;
    }

    public void start(){
        try {
            database = new MainDatabase("localhost", 3306, "root", "", "rox");
            serverSocket = new ServerSocket(port);
            isActive = true;
            acceptThread = new ClientAcceptHandler();
            acceptThread.start();
            System.out.println("Main Server started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            serverSocket.close();
            acceptThread.interrupt();
            clients.forEach(((s, objects) -> ((Thread) objects[2]).interrupt()));
            clients.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ConcurrentHashMap<String, Object[]> getClients() {
        return clients;
    }

    void setClients(ConcurrentHashMap<String, Object[]> clients) {
        if (this.clients != null) this.clients.clear();
        this.clients = clients;
    }

    public MainDatabase getDatabase() {
        return database;
    }

    void setDatabase(MainDatabase database) {
        if (this.database != null) this.database.disconnect();
        this.database = database;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public boolean isActive() {
        return isActive;
    }

    void setActive(boolean bool) {
        isActive = bool;
    }

    void setWaitingConnection(boolean bool) {
        waitingConnection = bool;
    }

    public boolean isWaitingConnection(){
        return waitingConnection;
    }

}
