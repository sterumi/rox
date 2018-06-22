package rox.main.minecraftserver;

import rox.main.Main;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MCI {

    private ConcurrentHashMap<UUID, ConcurrentHashMap<String, Object>> informationMap = new ConcurrentHashMap<>();

    public String getServerName(UUID uuid) {
        try {
            ResultSet rs;

            while ((rs = Main.getDatabase().Query("SELECT * FROM mc_servers WHERE uuid='" + uuid.toString() + "'")).next()) {
                return rs.getString("servername");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "MinecraftServer";
    }

    public void setInformation(UUID serverName, String key, Object value) {
        informationMap.get(serverName).put(key, value);
    }

    public ConcurrentHashMap<String, Object> getInformation(UUID serverName) {
        return informationMap.get(serverName);
    }

    public Object getInformation(UUID serverName, String key) {
        return informationMap.get(serverName).get(key);
    }

    public ConcurrentHashMap<UUID, ConcurrentHashMap<String, Object>> getInformations() {
        return informationMap;
    }
}
