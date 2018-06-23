package rox.main.database;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import rox.main.Main;
import rox.main.event.events.DatabaseConnectEvent;
import rox.main.event.events.DatabaseDisconnectEvent;
import rox.main.event.events.DatabaseQueryEvent;
import rox.main.event.events.DatabaseUpdateEvent;
import rox.main.server.permission.Rank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Database implements DatabaseStructure{

    private DBData dbData;

    private Connection conn;

    private boolean connected = false;

    private BaseContent baseContent = new BaseContent();

    private List<String> newCreatedTables = new ArrayList<>();

    public Database(DBData dbData){
        this.dbData = dbData;
        connect();
    }

    /**
     * Create the database connection and save them to the conn var
     *
     */

    public void connect(){
        long startTime = System.currentTimeMillis();
        try{
            DatabaseConnectEvent event = new DatabaseConnectEvent();
            Main.getEventManager().callEvent(event);
            if (event.isCancelled()) return;

            conn = DriverManager.getConnection(dbData.toJDBCString(), dbData.getUsername(), dbData.getPassword());
            setup();
            connected = true;

        }catch (Exception e){
            if (e instanceof CommunicationsException) {
                Main.getLogger().err("Database", "Database is not active! SQLState: " + ((CommunicationsException) e).getSQLState());
            } else {
                e.printStackTrace();
            }
        }

        Main.getLogger().time("DatabaseConnect", startTime);
    }

    private void setup(){
        Main.getLogger().debug("Database", "Checking tables...");
        try {
            ResultSet rs = conn.getMetaData().getTables(null, null, null, new String[]{"TABLE"});
            List<String> registeredTables = new ArrayList<>(), unregisteredTables = new ArrayList<>();
            while(rs.next()){
                registeredTables.add(rs.getString("TABLE_NAME"));
            }

            for (int i = 0; i < baseContent.getStandardTables().length; i++) {
                if(!registeredTables.contains(baseContent.getStandardTables()[i])){
                    unregisteredTables.add(baseContent.getStandardTables()[i]);
                }
            }


            unregisteredTables.parallelStream().forEach(s -> {
                switch (s){
                    case "users":
                        Update("CREATE TABLE `users` (\n" +
                                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                                "  `username` text NOT NULL,\n" +
                                "  `uuid` text NOT NULL,\n" +
                                "  `password` text NOT NULL,\n" +
                                "  `discord_user_id` text NOT NULL,\n" +
                                "  `points` int(11) NOT NULL,\n" +
                                "  `bandate` text,\n" +
                                "  `rank` text NOT NULL,\n" +
                                "  PRIMARY KEY (`id`)\n" +
                                ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;");
                        break;

                    case "mc_server":
                        Update("CREATE TABLE `mc_servers` (\n" +
                                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                                "  `servername` text NOT NULL,\n" +
                                "  `uuid` text NOT NULL,\n" +
                                "  `password` text NOT NULL,\n" +
                                "  PRIMARY KEY (`id`)\n" +
                                ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;");
                        break;
                }

                newCreatedTables.add(s);
                Main.getLogger().debug("Database", "Created table: " + s);
                Main.getLogger().debug("Database", "Checking tables done!");
            });

            if(newCreatedTables.contains("users")){
                String password = Main.getMathUtil().getRandomString(8);
                Main.getLogger().log("Database", "Users table created first time. Creating temporary account for connection. Username: admin. Passwort: " + password);
                Update("INSERT INTO users(username, uuid, password, rank) VALUES ('admin','" + UUID.randomUUID() + "','" + password + "','" + Rank.MANAGER + "')");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public List<String> getNewCreatedTables() {
        return newCreatedTables;
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
