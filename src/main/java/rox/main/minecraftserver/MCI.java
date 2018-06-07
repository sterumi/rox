package rox.main.minecraftserver;

import rox.main.Main;

import java.sql.SQLException;
import java.util.UUID;

public class MCI {

    public String getServerName(UUID uuid) {
        try {
            return Main.getMainServer().getDatabase().Query("SELECT * FROM mc_servers WHERE uuid='" + uuid.toString() + "'").getString("servername");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
