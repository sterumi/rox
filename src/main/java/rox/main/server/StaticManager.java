package rox.main.server;

import rox.main.Main;

import java.sql.Date;
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
            return UUID.fromString(Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE username='" + name + "'").getString("uuid"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getBannedUsers() {
        try {
            return Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE bantime IS NOT NULL").getMetaData().getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Date getBanDate(UUID uuid) {
        try {
            return Date.valueOf(Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE uuid='" + uuid.toString() + "'").getString("bantime"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isUserOnline(UUID uuid) {
        return Main.getMainServer().getClients().containsKey(uuid);
    }
}
