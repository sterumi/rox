package rox.main.gamesystem;

import rox.main.Main;
import rox.main.util.BaseServer;

import java.net.ServerSocket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameSystem implements BaseServer {

    private ConcurrentHashMap<UUID, DataService> connections = new ConcurrentHashMap<>();

    private ServerSocket serverSocket;

    private final int port, version, maxConnections;

    private boolean active;

    private Thread acceptThread;

    private ConcurrentHashMap<GameType, Integer> versions = new ConcurrentHashMap<>();

    public GameSystem(int port, int maxConnections){
        this.maxConnections = maxConnections;
        this.port = port;
        this.version = 1;
        versions.put(GameType.ARK, 1);
        versions.put(GameType.ARMA3, 1);
        versions.put(GameType.AVORION, 1);
        versions.put(GameType.CSGO, 1);
        versions.put(GameType.FACTORIO, 1);
        versions.put(GameType.CSS, 1);
        versions.put(GameType.MINECRAFT, 1);
    }

    public GameSystem(int port){
        this(port, 20);
    }

    public void start(){
        try{

            if (!Main.getDatabase().isConnected()) {
                Main.getLogger().err("GameSystem", "Could not start GameSystem.");
                return;
            }

            serverSocket = new ServerSocket(port);
            (acceptThread = new Thread(GameServerAcceptThread::new)).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<UUID, DataService> getConnections() {
        return connections;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ConcurrentHashMap<GameType, Integer> getVersions() {
        return versions;
    }

    public int getVersion() {
        return version;
    }

    public int getPort() {
        return port;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setConnections(ConcurrentHashMap<UUID, DataService> connections) {
        this.connections = connections;
    }

    public void setVersions(ConcurrentHashMap<GameType, Integer> versions) {
        this.versions = versions;
    }

    public void setAcceptThread(Thread acceptThread) {
        this.acceptThread = acceptThread;
    }

    public Thread getAcceptThread() {
        return acceptThread;
    }
}
