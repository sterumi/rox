package rox.main.server.database;

import rox.main.Main;

import java.sql.*;

public class MainDatabase {

    private Connection conn;

    public MainDatabase(String hostname, int port, String username, String password, String database){
        long startTime = System.currentTimeMillis();
        try {

            conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Main.getLogger().time("DatabaseConnect", startTime);
    }

    public void Update(String qry) {
        long startTime = System.currentTimeMillis();
        try {
            conn.prepareStatement(qry).executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        Main.getLogger().time("DatabaseUpdate", startTime);
    }

    public ResultSet Query(String sql) {
        long startTime = System.currentTimeMillis();
        ResultSet rs = null;
        try {
            rs = conn.prepareStatement(sql).executeQuery();
        } catch (Exception e) { e.printStackTrace(); }
        Main.getLogger().time("DatabaseQuery", startTime);
        return rs;
    }

    public void disconnect() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
