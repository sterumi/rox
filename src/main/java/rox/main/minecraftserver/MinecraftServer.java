package rox.main.minecraftserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class MinecraftServer {

    private ConcurrentHashMap<String, Object[]> serverMap = new ConcurrentHashMap<>();

    private ServerSocket serverSocket;

    private MCI mci;

    private int port;

    private boolean active = false;

    private Thread mcAcceptHandlerThread;

    public MinecraftServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            mci = new MCI();
            (mcAcceptHandlerThread = new MCAcceptHandler()).start();
            active = true;
            System.out.println("Minecraft System started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverMap.forEach((name, objects) -> {
                try {
                    ((Thread) objects[1]).interrupt();
                    ((Socket) objects[0]).close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            serverSocket.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void clear() {
        this.getServerMap().clear();
    }

    public Thread getMcAcceptHandlerThread() {
        return this.mcAcceptHandlerThread;
    }

    public void addServer(String name, Object[] objects) {
        this.getServerMap().put(name, objects);
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public ConcurrentHashMap<String, Object[]> getServerMap() {
        return this.serverMap;
    }

    public MCI getMCI() {
        return this.mci;
    }
}
