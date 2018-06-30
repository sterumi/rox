package rox.main.server.permission;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import rox.main.Main;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PermissionManager {

    private ConcurrentHashMap<String, JSONArray> ranks = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, String> defaults = new ConcurrentHashMap<>();

    public PermissionManager(){
        try{
            ResultSet rs = Main.getDatabase().Query("SELECT * FROM ranks");
            while(rs.next()) ranks.put(rs.getString("rankName"), (JSONArray) new JSONParser().parse(rs.getString("permissions")));

            ResultSet rs1 = Main.getDatabase().Query("SELECT * FROM ranks_default");
            while(rs1.next()){
                defaults.put("normal", rs1.getString("normal"));
                defaults.put("admin", rs1.getString("admin"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public String getRankDatabase(UUID uuid){
        try{
            ResultSet rs = Main.getDatabase().Query("SELECT * FROM users WHERE uuid='" + uuid.toString() + "'");
            while(rs.next()){
                return rs.getString("rank");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return defaults.get("normal");
    }


    public boolean hasPermission(String rankName, String permission) {
        System.out.println(rankName);
        System.out.println(getDefaultAdmin());
        if(rankName.equalsIgnoreCase(getDefaultAdmin())) return true;
        return ranks.get(rankName).contains(permission);
    }

    public boolean addPermission(String rankName, String permission) {
        if (ranks.containsKey(rankName)) {
            if (ranks.get(rankName).contains(permission)) {
                return false;
            } else {
                ranks.get(rankName).add(permission);
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean removePermission(String rankName, String permission) {
        if (ranks.containsKey(rankName)) {
            if (ranks.get(rankName).contains(permission)) {
                ranks.get(rankName).remove(permission);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setDefault(String rankName) {
        defaults.put("normal", rankName);
    }

    public void setDefaultAdmin(String rankName) {
        defaults.put("admin", rankName);
    }

    public String getDefault() {
        return defaults.get("normal");
    }

    public String getDefaultAdmin() {
        return defaults.get("admin");
    }

    public boolean createRank(String rankName) {
        if (ranks.containsKey(rankName)) return false;
        else {
            ranks.put(rankName, new JSONArray());
            Main.getDatabase().Update("INSERT INTO ranks(rankName, permissions) VALUES ('" + rankName + "','" + new JSONArray().toJSONString() + "')");
            return true;
        }
    }

    private void save(String database, String key, String value){
        Main.getDatabase().Update("UPDATE " + database + " SET " + key + "='" + value + "'");
    }

    public boolean setRank(UUID uuid, String rankName){
        if(ranks.containsKey(rankName)){
            Main.getDatabase().Update("UPDATE users SET rank='" + rankName + "' WHERE uuid='" + uuid.toString() + "'");
            return true;
        }else return false;

    }

    public void updatePermission(){
        ranks.keySet().forEach(this::updatePermissions);
    }

    public void updatePermissions(String rankName){
        Main.getDatabase().Update("UPDATE ranks SET permissions='" + ranks.get(rankName).toJSONString() + "' WHERE rankName='" + rankName + "'");
    }

    public boolean deleteRank(String rankName) {
        if(ranks.containsKey(rankName)){
            ranks.remove(rankName);
            Main.getDatabase().Update("DELETE FROM ranks WHERE rankName='" + rankName + "'");
            Main.getMainServer().getClients().forEach((uuid, objects) -> { if(objects.getRank().equalsIgnoreCase(rankName)) objects.setRank(defaults.get("normal")); });
            return true;
        }else return false;
    }

    public ConcurrentHashMap<String, JSONArray> getRanks() {
        return ranks;
    }

    public ConcurrentHashMap<String, String> getDefaults() {
        return defaults;
    }
}
