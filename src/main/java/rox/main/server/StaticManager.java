package rox.main.server;

import rox.main.Main;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class StaticManager {

    public int getRegisteredUser() {
        try {
            return Main.getMainServer().getDatabase().Query("SELECT MAX(ID) FROM users").getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getOnlineUser() {
        try {
            return Main.getMainServer().getClients().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getOnlineAdmins() {
        try {
            return Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE rank >= '4'").getMetaData().getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public UUID getUUID(String name) {
        try {
            ResultSet rs = Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE username='" + name + "'");
            while (rs.next()) {
                return UUID.fromString(rs.getString("uuid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getBannedUsers() {
        try {
            ResultSet rs = Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE bantime IS NOT NULL");
            while (rs.next()) {
                return rs.getMetaData().getColumnCount();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Date getBanDate(UUID uuid) {
        try {

            ResultSet rs = Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE uuid='" + uuid.toString() + "'");
            while (rs.next()) {
                return Date.valueOf(rs.getString("bantime"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isUserOnline(UUID uuid) {
        return Main.getMainServer().getClients().containsKey(uuid);
    }
}
