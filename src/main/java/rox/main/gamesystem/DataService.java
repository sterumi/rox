package rox.main.gamesystem;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataService {

    private String name;
    private final UUID uuid;
    private final Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private GameType gameType;
    private int version;
    private Thread inputThread;
    private ConcurrentHashMap<String, Object> information = new ConcurrentHashMap<>();

    public DataService(String name, UUID uuid, Socket socket, GameType gameType, int version){
        this.name = name;
        this.uuid = uuid;
        this.socket = socket;
        this.gameType = gameType;
        this.version = version;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataService(String name, UUID uuid, Socket socket, BufferedReader reader, PrintWriter writer, GameType gameType, int version){
        this.name = name;
        this.uuid = uuid;
        this.socket = socket;
        this.gameType = gameType;
        this.version = version;
        this.reader = reader;
        this.writer = writer;

    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public UUID getUUID() {
        return uuid;
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getVersion() {
        return version;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public ConcurrentHashMap<String, Object> getInformation() {
        return information;
    }

    public void setInformation(ConcurrentHashMap<String, Object> information) {
        this.information = information;
    }

    public void setInputThread(Thread inputThread) {
        this.inputThread = inputThread;
        inputThread.start();
    }

    public Thread getInputThread() {
        return inputThread;
    }

    public String toJSONString(){
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("uuid", uuid);
        object.put("gameType", gameType);
        object.put("version", version);

        JSONObject informationObject = new JSONObject();
        information.forEach(informationObject::put);
        object.put("information", informationObject);

        return object.toJSONString();
    }
}
