package rox.main.database;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import org.json.simple.JSONArray;
import rox.main.Main;
import rox.main.event.events.DatabaseConnectEvent;
import rox.main.event.events.DatabaseDisconnectEvent;
import rox.main.event.events.DatabaseQueryEvent;
import rox.main.event.events.DatabaseUpdateEvent;
import rox.main.util.RandomString;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.UUID;

public class Database implements DatabaseStructure {

    private final DBData dbData;

    private Connection conn;

    private boolean connected = false;

    public Database(DBData dbData) {
        this.dbData = dbData;
        connect();
    }

    /**
     * Create the database connection and save them to the conn var
     */

    public void connect() {
        long startTime = System.currentTimeMillis();
        try {
            DatabaseConnectEvent event = new DatabaseConnectEvent();
            Main.getEventManager().callEvent(event);
            if (event.isCancelled()) return;

            conn = DriverManager.getConnection(dbData.toJDBCString(), dbData.getUsername(), dbData.getPassword());
            setup();
            connected = true;

        } catch (Exception e) {
            if (e instanceof CommunicationsException) {
                Main.getLogger().err("Database", "Database is not active! SQLState: " + ((CommunicationsException) e).getSQLState());
            } else {
                e.printStackTrace();
            }
        }

        Main.getLogger().time("DatabaseConnect", startTime);
    }

    private void setup() {
        Main.getLogger().debug("Database", "Checking tables...");
        try {
            Update("CREATE TABLE IF NOT EXISTS `users` (" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                    "  `username` text NOT NULL," +
                    "  `uuid` text NOT NULL," +
                    "  `password` text NOT NULL," +
                    "  `discord_user_id` text ," +
                    "  `points` int(11) ," +
                    "  `bandate` text," +
                    "  `rank` text NOT NULL," +
                    "  PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;");

            Update("CREATE TABLE IF NOT EXISTS `gameserver` (" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                    "  `name` text NOT NULL," +
                    "  `uuid` text NOT NULL," +
                    "  `gametype` enum('ARK','MINECRAFT','ARMA3','CSS','CSGO','AVORTION','FACTORIO') NOT NULL," +
                    "  `lastlogin` mediumtext," +
                    "  `password` text NOT NULL," +
                    "  PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;");

            Update("CREATE TABLE IF NOT EXISTS `ranks` (" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                    "  `rankName` text NOT NULL," +
                    "  `permissions` text," +
                    "  PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;");
            Update("CREATE TABLE IF NOT EXISTS `ranks_default` (" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                    "  `normal` text NOT NULL," +
                    "  `admin` text NOT NULL," +
                    "  PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;");

            Main.getLogger().debug("Database", "Checking tables done!");

            if (!Query("SELECT * FROM users").next()){
                String password = new RandomString().nextString();
                Main.getLogger().log("Database", "Users table created first time. Creating temporary account for connection. Username: admin. Passwort: " + password);
                Update("INSERT INTO users(username, uuid, password, rank) VALUES ('admin','" + UUID.randomUUID() + "','" + Main.getMathUtil().computeSHA256(password) + "','Admin')");
            }
            if (!Query("SELECT * FROM ranks_default").next()) Update("INSERT INTO ranks_default(normal, admin) VALUES ('User','Admin')");

            if(!Query("SELECT * FROM ranks").next()){
                JSONArray admn = new JSONArray(), hlp = new JSONArray(), dev, mod = new JSONArray(), sup, usr = new JSONArray();
                admn.add("rox.admin");
                hlp.add("rox.commands.*");
                hlp.add("rox.bypass.join");
                hlp.add("rox.bypass.ban");
                dev = hlp;
                mod.add("rox.commands.ban");
                mod.add("rox.commands.msg");
                mod.add("rox.commands.info");
                mod.add("rox.bypass.join");
                sup = mod;
                usr.add("rox.commands.info");
                usr.add("rox.commands.msg");

                Update("INSERT INTO ranks(rankName, permissions) VALUES ('Admin','" + admn.toJSONString() + "')");
                Update("INSERT INTO ranks(rankName, permissions) VALUES ('Helper','" + hlp.toJSONString() + "')");
                Update("INSERT INTO ranks(rankName, permissions) VALUES ('Developer','" + dev.toJSONString() + "')");
                Update("INSERT INTO ranks(rankName, permissions) VALUES ('Moderator','" + mod.toJSONString() + "')");
                Update("INSERT INTO ranks(rankName, permissions) VALUES ('Supporter','" + sup.toJSONString() + "')");
                Update("INSERT INTO ranks(rankName, permissions) VALUES ('User','" + usr.toJSONString() + "')");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public DBData getDbData() {
        return dbData;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
