package rox.main.server.permission;

import rox.main.Main;

import java.sql.SQLException;
import java.util.UUID;

public class PermissionManager {

    public boolean setRank(UUID uuid, Rank rank) {
        if (Main.getMainServer().getClients().containsKey(uuid)) {
            Main.getMainServer().getClients().get(uuid)[5] = rank;
            save(uuid);
            return true;
        }
        return false;
    }

    public void setRank(String name, Rank rank) {

        Main.getMainServer().getClients().forEach((uuid, obj) -> {
            if (obj[1].equals(name)) {
                Main.getMainServer().getClients().get(uuid)[5] = rank;
                save(uuid);
            }
        });
    }

    private void save(UUID uuid) {
        Main.getMainServer().getDatabase().Update("UPDATE users SET rank='" + Main.getMainServer().getClients().get(uuid)[5].toString().toUpperCase() + "'");
    }

    public Rank getRank(UUID uuid) {
        if (Main.getMainServer().getClients().get(uuid)[5] == null)
            Main.getMainServer().getClients().get(uuid)[5] = Rank.USER;
        return (Rank) Main.getMainServer().getClients().get(uuid)[5];
    }

    public Rank getRankDatabase(UUID uuid) {
        try {
            return Rank.valueOf(Main.getMainServer().getDatabase().Query("SELECT rank FROM users WHERE uuid='" + uuid + "'").getString("rank"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Rank.USER;
    }

    public boolean hasRank(UUID uuid, Rank rank) {
        return ((Rank) Main.getMainServer().getClients().get(uuid)[5]).getValue() >= rank.getValue();
    }

}
