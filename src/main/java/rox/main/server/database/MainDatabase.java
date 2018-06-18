package rox.main.server.database;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import rox.main.Main;
import rox.main.event.events.DatabaseConnectEvent;
import rox.main.event.events.DatabaseDisconnectEvent;
import rox.main.event.events.DatabaseQueryEvent;
import rox.main.event.events.DatabaseUpdateEvent;

import java.sql.*;

public class MainDatabase {

    private Connection conn;

    private boolean connected = false;

    /**
     * Create the database connection and save them to the conn var
     *
     * @param hostname The hostname of the database            (localhost)
     * @param port     The port of the database                (3306)
     * @param username The username to connect                 (root)
     * @param password The password to connect                 ('')
     * @param database The database name with all tables etc   (rox)
     */

    public MainDatabase(String hostname, int port, String username, String password, String database) {
        long startTime = System.currentTimeMillis();
        try {
            DatabaseConnectEvent event = new DatabaseConnectEvent();
            Main.getEventManager().callEvent(event);
            if (event.isCancelled()) return;
            conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
            connected = true;
        } catch (Exception e) {
            if (e instanceof CommunicationsException) {
                Main.getLogger().err("Database", "Database is not active!");
            } else {
                e.printStackTrace();
            }
        }
        Main.getLogger().time("DatabaseConnect", startTime);
    }

    public boolean isConnected() {
        return connected;
    }


    /**
     * @param qry The update string to add a table, a column or whatever
     */
    public void Update(String qry) {
        long startTime = System.currentTimeMillis();
        try {
            DatabaseUpdateEvent event = new DatabaseUpdateEvent(qry);
            Main.getEventManager().callEvent(event);
            if (event.isCancelled()) return;

            conn.prepareStatement(qry).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main.getLogger().time("DatabaseUpdate", startTime);
    }

    /**
     * Returns a whole table or a column or some data about shit
     *
     * @param sql The query string
     * @return The ResultSet with the asked shit
     */

    public ResultSet Query(String sql) {
        long startTime = System.currentTimeMillis();
        DatabaseQueryEvent event = new DatabaseQueryEvent(sql);
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return null;

        ResultSet rs = null;
        try {
            rs = conn.prepareStatement(sql).executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main.getLogger().time("DatabaseQuery", startTime);
        return rs;
    }

    public void disconnect() {
        try {
            DatabaseDisconnectEvent event = new DatabaseDisconnectEvent();
            Main.getEventManager().callEvent(event);
            if (event.isCancelled()) return;

            conn.close();
            connected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
