package rox.main.gamesystem;

import rox.main.Main;
import rox.main.util.BaseServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
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
            new Thread(this::inputThread).start();
            // <gametype>§<version>§<uuid>§<password>
            setActive(true);
            Main.getLogger().log("GameServer", "Started.");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            connections.keySet().forEach(this::disconnect);
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inputThread(){
        try {
            Socket socket;
            while ((socket = Main.getGameSystem().getServerSocket().accept()) != null) {
                Main.getLogger().log("GameServer", "Client connecting...");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                if (Main.getGameSystem().getConnections().size() <= Main.getGameSystem().getMaxConnections()) {
                    String connectString = reader.readLine();

                    String[] args = connectString.split("§");

                    if(connections.containsKey(UUID.fromString(args[2]))){
                        writer.println("CONNECTION_REFUSED");
                        socket.close();
                        return;
                    }

                    if (auth(UUID.fromString(args[2]), args[3])) {

                        int version = Integer.parseInt(args[1]);
                        GameType gameType = GameType.valueOf(args[0]);

                        if (Main.getGameSystem().getVersions().get(gameType) == version) {
                            DataService dataService = new DataService(getName(UUID.fromString(args[2])), UUID.fromString(args[2]), socket, reader, writer, gameType, version);
                            Main.getGameSystem().getConnections().put(dataService.getUUID(), dataService);
                            dataService.setInputThread(new GameServerInputThread(dataService));
                            writer.println("CONNECTION_ACCEPTED");
                            Main.getLogger().log("GameServer", dataService.getName() + " connected!");
                        } else {
                            writer.println("CONNECTION_WRONG_VERSION");
                            socket.close();
                        }
                    } else {
                        writer.println("CONNECTION_REFUSED");
                        socket.close();
                    }
                } else {
                    writer.println("CONNECTION_FULL");
                    socket.close();
                }

            }
        } catch (Exception e) {
            if(e instanceof SocketException){
                Main.getLogger().warn("MainServer", "Interrupted the accepting for clients.");
                return;
            }
            e.printStackTrace();
        }
    }

    public void disconnect(UUID uuid){
        DataService dataService = connections.get(uuid);
        dataService.getWriter().println("DISCONNECT");
        dataService.getInputThread().interrupt();
        if(!dataService.getSocket().isClosed()) try { dataService.getSocket().close(); } catch (Exception e) { e.printStackTrace(); }
        connections.remove(uuid);
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

    private String getName(UUID uuid) {
        ResultSet rs = Main.getDatabase().Query("SELECT * FROM gameserver WHERE uuid='" + uuid.toString() + "'");
        try {
            while (rs.next()) return rs.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Default_GameServer";
    }

    private boolean auth(UUID uuid, String password) {
        ResultSet rs = Main.getDatabase().Query("SELECT * FROM gameserver WHERE uuid='" + uuid.toString() + "' AND password='" + password + "'");
        try {
            while (rs.next()) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void register(String name, UUID uuid, String password, GameType type){
        Main.getDatabase().Update("INSERT INTO gameserver(name, uuid, gametype, password) VALUES ('" + name + "','" + uuid + "','" + type.toString() + "','" + Main.getMathUtil().computeSHA256(password) + "')");
    }
}
