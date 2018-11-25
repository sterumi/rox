package rox.main.server;

import rox.main.Main;
import rox.main.event.events.MainServerStartingEvent;
import rox.main.event.events.MainServerStoppingEvent;
import rox.main.server.command.*;
import rox.main.server.dummy.TestClient;
import rox.main.server.permission.PermissionManager;
import rox.main.util.BaseServer;

import java.io.*;
import java.net.ServerSocket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer implements BaseServer {

    private ServerSocket serverSocket;

    private final int port;

    private boolean waitingConnection = true, isActive = false;

    private ConcurrentHashMap<UUID, Client> clients = new ConcurrentHashMap<>();


    private Thread acceptThread;

    private ServerCommandLoader serverCommandLoader;

    private StaticManager staticManager;

    private PermissionManager permissionManager;


    /**
     * Sets port for clients listening.
     *
     * @param port The port for the server
     */

    public MainServer(int port){
        this.port = port;
    }


    /**
     * Connect to Database and create server socket. Open a new thread for listening new clients are connecting.
     */
    public boolean start(){

        MainServerStartingEvent event = new MainServerStartingEvent();
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return false;

        long startTime = System.currentTimeMillis(); // Load Time
        try {
            if (!Main.getDatabase().isConnected()) {
                Main.getLogger().err("MainServer", "Could not start MainServer."); // If can not connect to database
                return false;
            }
            serverSocket = new ServerSocket(port); // Create server socket
            (acceptThread = new ClientAcceptHandler()).start(); // Open new thread for new clients input
            serverCommandLoader = new ServerCommandLoader(); // Command handler if client send a message
            staticManager = new StaticManager(); // Methods where i don't know where to write them.
            permissionManager = new PermissionManager(); // Permissions System for clients, Admins, Mods, Members,..
            loadCommands(); // Loading all commands for the clients
            Main.getLogger().log("MainServer", "Started.");
            isActive = true; // Global boolean to check if server is active

            if((Boolean)Main.getFileConfiguration().getDummyValues().get("enable")) new TestClient().connect();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getLogger().time("MainServerLoad", startTime); // Write to console how long it took to start the server
        return false;
    }

    /**
     * Closing everything and clear lists.
     */
    public boolean stop(){
        try {

            MainServerStoppingEvent event = new MainServerStoppingEvent();
            Main.getEventManager().callEvent(event);
            if (event.isCancelled()) return false;

            serverSocket.close();
            acceptThread.interrupt();
            clients.forEach(((s, client) -> (client.getInputThread()).interrupt()));
            clients.clear();
            permissionManager.updatePermission();
            Main.getLogger().log("MainServer", "Stopped.");

            isActive = false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void clear(){
        clients.clear();
    }

    public ConcurrentHashMap<UUID, Client> getClients() {
        return clients;
    }

    public boolean isConnected() {
        return serverSocket.isClosed();
    }

    public void setClients(ConcurrentHashMap<UUID, Client> clients) {
        if (this.clients != null) this.clients.clear();
        this.clients = clients;
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

    private void loadCommands() {
        serverCommandLoader.addCommand("DISCONNECT", new DisconnectCommand());
        serverCommandLoader.addCommand("MSG", new MsgCommand());
        serverCommandLoader.addCommand("INFO", new InfoCommand());
        serverCommandLoader.addCommand("BAN", new BanCommand());
        serverCommandLoader.addCommand("RANK", new RankCommand());
    }

    public ServerCommandLoader getServerCommandLoader() {
        return serverCommandLoader;
    }

    public StaticManager getStaticManager() {
        return staticManager;
    }

    public void saveUser(UUID uuid) {
        Main.getDatabase().Update("UPDATE users SET rank='" + getClients().get(uuid).getRank() + "'");
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }


    void createUser(String username, String password) {
        Main.getDatabase().Update("INSERT INTO users(username, uuid, password, points, rank) VALUES ('" + username + "','" + UUID.randomUUID() + "','" + password + "','0','" + permissionManager.getDefault() + "')");
    }

    public boolean isMaintenance() {
        return (Boolean) Main.getFileConfiguration().getValue("maintenance");
    }

    public void setMaintenance(String key, Object value) {
        Main.getFileConfiguration().saveKey(key, value);
    }

    public void setAcceptThread(Thread acceptThread) {
        this.acceptThread = acceptThread;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setServerCommandLoader(ServerCommandLoader serverCommandLoader) {
        this.serverCommandLoader = serverCommandLoader;
    }

    public Thread getAcceptThread() {
        return acceptThread;
    }

    public int getPort() {
        return port;
    }
}
