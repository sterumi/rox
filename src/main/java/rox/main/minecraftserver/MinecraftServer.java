package rox.main.minecraftserver;

import rox.main.Main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MinecraftServer {

    private ConcurrentHashMap<UUID, Object[]> serverMap;

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
            serverMap = new ConcurrentHashMap<>();
            serverSocket = new ServerSocket(port);
            mci = new MCI();
            (mcAcceptHandlerThread = new MCAcceptHandler()).start();
            active = true;
            Main.getLogger().log("MinecraftSystem", "Started.");
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

    public void addServer(UUID uuid, Object[] objects) {
        serverMap.put(uuid, objects);
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public ConcurrentHashMap<UUID, Object[]> getServerMap() {
        return serverMap;
    }

    public MCI getMCI() {
        return this.mci;
    }

    public void removeServer(UUID uuid) {
        serverMap.remove(uuid);
    }
}
