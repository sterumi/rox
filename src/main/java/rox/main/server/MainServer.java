package rox.main.server;

import rox.main.Main;
import rox.main.server.command.*;
import rox.main.server.database.MainDatabase;
import rox.main.server.permission.PermissionManager;
import rox.main.server.permission.Rank;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer {

    private ServerSocket serverSocket;

    private int port;

    private boolean waitingConnection = true, isActive = false;

    private ConcurrentHashMap<UUID, Object[]> clients = new ConcurrentHashMap<>();

    private MainDatabase database;

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
    public void start(){
        long startTime = System.currentTimeMillis(); // Load Time
        try {
            database = new MainDatabase("localhost", 3306, "root", "", "rox"); // Connecting to database
            if (!database.isConnected()) {
                Main.getLogger().err("MainServer", "Could not start MainServer."); // If can not connect to database
                return;
            }
            serverSocket = new ServerSocket(port); // Create server socket
            (acceptThread = new ClientAcceptHandler()).start(); // Open new thread for new clients input
            serverCommandLoader = new ServerCommandLoader(); // Command handler if client send a message
            staticManager = new StaticManager(); // Methods where i don't know where to write them.
            permissionManager = new PermissionManager(); // Permissions System for clients, Admins, Mods, Members,..
            loadCommands(); // Loading all commands for the clients
            Main.getLogger().log("MainServer", "Started.");
            isActive = true; // Global boolean to check if server is active
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getLogger().time("MainServerLoad", startTime); // Write to console how long it took to start the server
    }

    /**
     * Closing everything and clear lists.
     */
    public void stop(){
        try {
            serverSocket.close();
            acceptThread.interrupt();
            clients.forEach(((s, objects) -> ((Thread) objects[2]).interrupt()));
            clients.clear();
            Main.getLogger().log("MainServer", "Stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<UUID, Object[]> getClients() {
        return clients;
    }

    void setClients(ConcurrentHashMap<UUID, Object[]> clients) {
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

    private void loadCommands() {
        serverCommandLoader.addCommand("§DISCONNECT", new DisconnectCommand());
        serverCommandLoader.addCommand("§MSG", new MsgCommand());
        serverCommandLoader.addCommand("§INFO", new InfoCommand());
        serverCommandLoader.addCommand("§BAN", new BanCommand());
        serverCommandLoader.addCommand("§RANK", new RankCommand());
    }

    public ServerCommandLoader getServerCommandLoader() {
        return serverCommandLoader;
    }

    public StaticManager getStaticManager() {
        return staticManager;
    }

    public void saveUser(UUID uuid) {
        getDatabase().Update("UPDATE users SET rank='" + getClients().get(uuid)[5].toString().toUpperCase() + "'");
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public Object[] getUser(String name) {
        final Object[][] objects = new Object[1][1];
        Main.getMainServer().getClients().forEach((uuid, obj) -> {
            if (obj[1].equals(name)) {
                objects[0] = obj;
            }
        });
        return objects[0];
    }

    public void banUser(UUID uuid) {
        try {
            this.getDatabase().Update("UPDATE users SET bantime='" + LocalDateTime.now() + "' WHERE uuid ='" + uuid + "'");
            ((PrintWriter) clients.get(uuid)[4]).println("§BANNED");
            ((Thread) clients.get(uuid)[2]).interrupt();
            ((Socket) clients.get(uuid)[1]).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void banOfflineUser(UUID uuid) {
        try {
            this.getDatabase().Update("UPDATE users SET bantime='" + LocalDateTime.now() + "' WHERE uuid ='" + uuid + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isBanned(UUID uuid) {
        try {

            ResultSet rs = Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE uuid='" + uuid + "'");
            while (rs.next()) {
                return rs.getString("bandate") != null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    void createUser(String username, String password) {
        getDatabase().Update("INSERT INTO users(username, uuid, password, points, rank) VALUES ('" + username + "','" + UUID.randomUUID() + "','" + password + "','0','" + Rank.USER + "')");
    }

    public boolean isMaintenance() {
        return (Boolean) Main.getFileConfiguration().getValue("maintenance");
    }

    public void setMaintenance(String key, Object value) {
        Main.getFileConfiguration().saveKey(key, value);
    }

}
