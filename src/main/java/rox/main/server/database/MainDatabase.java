package rox.main.server.database;

import java.sql.*;

public class MainDatabase {

    private Connection conn;

    private String connString = "jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public MainDatabase(String hostname, int port, String username, String password, String database){
        try {

            conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Update(String qry) {
        try {
            conn.prepareStatement(qry).executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public ResultSet Query(String sql) {
        try {
            return conn.prepareStatement(sql).executeQuery();
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

}
