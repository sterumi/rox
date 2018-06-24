package rox.main.server.permission;

import rox.main.Main;
import rox.main.event.events.MainServerPermissionChangeEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PermissionManager {

    /**
     * if the user should get a better rank or worse rank if he is a retard
     *
     * @param uuid The uuid of the user
     * @param rank The rank to set
     * @return if success
     */

    public boolean setRank(UUID uuid, Rank rank) {
        if (Main.getMainServer().getClients().containsKey(uuid)) {

            MainServerPermissionChangeEvent event = new MainServerPermissionChangeEvent(uuid, (Rank) Main.getMainServer().getClients().get(uuid)[5], rank);
            Main.getEventManager().callEvent(event);
            if (event.isCancelled()) return false;

            Main.getMainServer().getClients().get(uuid)[5] = rank;
            save(uuid); // saving rank to database, yea i know. Not so good if you write it to the database every time. But how often do i refresh this?
            return true;
        }
        return false;
    }

    /**
     * Same function without return. Not very good, because if user change the name? Never mind, i prefer this function cause i don't care
     *
     * @param name The username of the user
     * @param rank The rank to set
     */

    public void setRank(String name, Rank rank) {

        //TODO

        MainServerPermissionChangeEvent event = new MainServerPermissionChangeEvent(name, null, rank);
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        Main.getMainServer().getClients().forEach((uuid, obj) -> {
            if (obj[1].equals(name)) {
                Main.getMainServer().getClients().get(uuid)[5] = rank;
                save(uuid);
            }
        });
    }

    /**
     * Saving the rank to the database
     *
     * @param uuid UUID of the user
     */
    private void save(UUID uuid) {
        Main.getDatabase().Update("UPDATE users SET rank='" + Main.getMainServer().getClients().get(uuid)[5].toString().toUpperCase() + "'");
    }

    /**
     * Returns the Rank of the user from uuid
     *
     * @param uuid UUID of the user
     * @return Return the rank
     */

    public Rank getRank(UUID uuid) {
        if (Main.getMainServer().getClients().get(uuid)[5] == null)
            Main.getMainServer().getClients().get(uuid)[5] = Rank.USER; //If the rank is null (which will not happen), set him the default rank
        return (Rank) Main.getMainServer().getClients().get(uuid)[5];
    }


    /**
     * Returns the rank is saved in database currently
     *
     * @param uuid UUID uf the user
     * @return Return the rank is saved in database
     */
    public Rank getRankDatabase(UUID uuid) {
        try {

            ResultSet rs = Main.getDatabase().Query("SELECT rank FROM users WHERE uuid='" + uuid + "'");
            while (rs.next()) {
                return Rank.valueOf(rs.getString("rank"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Rank.USER;
    }

    /**
     * @param uuid UUID of the user
     * @param rank The rank this will be better or lower then the rank of the user
     * @return If the given rank parameter is higher(true) or lower(false)
     */
    public boolean hasRank(UUID uuid, Rank rank) {
        return ((Rank) Main.getMainServer().getClients().get(uuid)[5]).getValue() >= rank.getValue();
    }

}
