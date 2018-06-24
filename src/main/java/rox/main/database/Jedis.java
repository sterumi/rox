package rox.main.database;

import java.util.ArrayList;

public class Jedis implements DatabaseStructure{

    private redis.clients.jedis.Jedis jedis;

    private final DBData dbData;

    private ArrayList<String> updatedKeys = new ArrayList<>();

    public Jedis(DBData dbData){
        this.dbData = dbData;
        //connect();
    }

    private void connect(){
        this.jedis = new redis.clients.jedis.Jedis(dbData.getHostname(), dbData.getPort());
    }


    public String get(String key){
        if(!updatedKeys.contains(key))updatedKeys.add(key);
        return jedis.get(key);
    }

    public String set(String key, String value){
        if(!updatedKeys.contains(key))updatedKeys.add(key);
        return jedis.set(key, value);
    }

    public long del(String key){
        if(updatedKeys.contains(key))updatedKeys.add(key + "§DELETED");
        return jedis.del(key);
    }

    public DBData getDbData() {
        return dbData;
    }

    public ArrayList<String> getUpdatedKeys() {
        return updatedKeys;
    }

    public redis.clients.jedis.Jedis getJedis() {
        return jedis;
    }

    public void setJedis(redis.clients.jedis.Jedis jedis) {
        this.jedis = jedis;
    }

    public void setUpdatedKeys(ArrayList<String> updatedKeys) {
        this.updatedKeys = updatedKeys;
    }
}
