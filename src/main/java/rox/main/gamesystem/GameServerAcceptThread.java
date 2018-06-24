package rox.main.gamesystem;

import rox.main.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.UUID;

public class GameServerAcceptThread extends Thread {

    // <gametype>§<version>§<uuid>§<password>

    public void run() {
        try {
            Socket socket;
            while ((socket = Main.getGameSystem().getServerSocket().accept()) != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                if(Main.getGameSystem().getConnections().size() <= Main.getGameSystem().getMaxConnections()) {
                    String connectString = reader.readLine();

                    String[] args = connectString.split("§");

                    if (auth(UUID.fromString(args[2]), args[4])) {

                        int version = Integer.parseInt(args[1]);
                        GameType gameType = GameType.valueOf(args[0]);

                        if(Main.getGameSystem().getVersions().get(gameType) == version){
                            DataService dataService = new DataService(getName(UUID.fromString(args[2])), UUID.fromString(args[2]), socket, reader, writer, gameType, version);
                            Main.getGameSystem().getConnections().put(dataService.getUUID(), dataService);
                            dataService.setInputThread(new Thread(() -> new GameServerInputThread(dataService)));
                            writer.println("CONNECTION_ACCEPTED");
                        }else {
                            writer.println("CONNECTION_WRONG_VERSION");
                            socket.close();
                        }
                    } else {
                        writer.println("CONNECTION_REFUSED");
                        socket.close();
                    }
                }else{
                    writer.println("CONNECTION_FULL");
                    socket.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getName(UUID uuid) {
        ResultSet rs = Main.getDatabase().Query("SELECT * FROM gameservers WHERE uuid='" + uuid.toString() + "'");
        try {
            while (rs.next()) return rs.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Default_GameServer";
    }

    private boolean auth(UUID uuid, String password) {
        ResultSet rs = Main.getDatabase().Query("SELECT * FROM gameservers WHERE uuid='" + uuid.toString() + "' AND password='" + password + "'");
        try {
            while (rs.next()) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public ClassLoader getContextClassLoader() {
        return super.getContextClassLoader();
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public State getState() {
        return super.getState();
    }

    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return super.getUncaughtExceptionHandler();
    }

    @Override
    public void setContextClassLoader(ClassLoader cl) {
        super.setContextClassLoader(cl);
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
    }
}
