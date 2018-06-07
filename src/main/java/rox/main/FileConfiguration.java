package rox.main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileConfiguration {

    private File configfile;

    private ConcurrentHashMap<String, Object> values;

    public FileConfiguration() {
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
            writer.write(object.toJSONString());
            writer.flush();
        } else {
            ((JSONObject) new JSONParser().parse(new FileReader(configfile.getPath()))).forEach((key, value) -> values.put((String) key, value));
        }

    }

    public Object getValue(String key) {
        return values.get(key);
    }

}
