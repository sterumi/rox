package rox.main.server.database;

import java.sql.*;

public class MainDatabase {

    private Connection conn;

    public MainDatabase(String hostname, int port, String username, String password, String database){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
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
