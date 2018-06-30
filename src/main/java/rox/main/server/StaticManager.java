package rox.main.server;

import rox.main.Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class StaticManager {

    public int getRegisteredUser() {
        try {
            ResultSet rs = Main.getDatabase().Query("SELECT MAX(id) FROM users");
            while(rs.next()) return rs.getInt("id");
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
            ResultSet rs = Main.getDatabase().Query("SELECT * FROM users WHERE rank = '" + Main.getMainServer().getPermissionManager().getDefaultAdmin() + "'");
            int i = 0;
            while(rs.next()) i++;
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public UUID getUUID(String name) {
        try {
            ResultSet rs = Main.getDatabase().Query("SELECT * FROM users WHERE username='" + name + "'");
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
            ResultSet rs = Main.getDatabase().Query("SELECT * FROM users WHERE bantime IS NOT NULL");
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

            ResultSet rs = Main.getDatabase().Query("SELECT * FROM users WHERE uuid='" + uuid.toString() + "'");
            while (rs.next()) {
                return Date.valueOf(rs.getString("bantime"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Client getUser(String name) {
        final Client[] objects = new Client[1];
        Main.getMainServer().getClients().forEach((uuid, obj) -> {
            if (obj.getName().equals(name)) {
                objects[0] = obj;
            }
        });
        return objects[0];
    }

    public void banUser(UUID uuid) {
        try {
            Main.getDatabase().Update("UPDATE users SET bandate='" + LocalDateTime.now() + "' WHERE uuid ='" + uuid + "'");
            Main.getMainServer().getClients().get(uuid).getWriter().println("§BANNED");
            Main.getMainServer().getClients().get(uuid).getInputThread().interrupt();
            Main.getMainServer().getClients().get(uuid).getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void banOfflineUser(UUID uuid) {
        try {
            Main.getDatabase().Update("UPDATE users SET bantime='" + LocalDateTime.now() + "' WHERE uuid ='" + uuid + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isBanned(UUID uuid) {
        try {

            ResultSet rs = Main.getDatabase().Query("SELECT * FROM users WHERE uuid='" + uuid + "'");
            while (rs.next()) {
                if(rs.getString("bandate") != null){
                    if(rs.getString("bandate").equalsIgnoreCase("")){
                        return false;
                    }else{
                        return true;
                    }
                }else{
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUserOnline(UUID uuid) {
        return Main.getMainServer().getClients().containsKey(uuid);
    }

}
