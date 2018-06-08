package rox.main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileConfiguration {

    private File configfile;

    private ConcurrentHashMap<String, Object> values;

    FileConfiguration() {
        values = new ConcurrentHashMap<>();
        configfile = new File("config/", "config.json");
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        configfile.getParentFile().mkdirs();
        if (!configfile.exists()) {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configfile), "utf-8"));
            JSONObject object = new JSONObject();
            object.put("discordToken", "TOKEN");
            object.put("maintenance", false);
            object.put("debug", false);
            object.put("maxConnections", 20);
            writer.write(object.toJSONString());
            writer.flush();
            writer.close();
        } else {
            ((JSONObject) new JSONParser().parse(new FileReader(configfile.getPath()))).forEach((key, value) -> values.put((String) key, value));
        }

    }

    public Object getValue(String key) {
        return values.get(key);
    }

    public void saveKey(String key, Object value) {
        try {
            JSONObject object1 = (JSONObject) new JSONParser().parse(new FileReader(configfile.getPath()));
            object1.put(key, value);
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configfile), "utf-8"));
            writer.write(object1.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
