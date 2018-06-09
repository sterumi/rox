package rox.main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileConfiguration {

    private File configFile;
    private File newsFile;

    private FileReader newsFileReader;

    private JSONParser parser;

    private ConcurrentHashMap<String, Object> values;

    FileConfiguration() {

        try {
            values = new ConcurrentHashMap<>();
            configFile = new File("config/", "config.json");
            newsFile = new File("config/", "news.json");
            newsFileReader = new FileReader(newsFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        parser = new JSONParser();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        configFile.getParentFile().mkdirs();
        if (!configFile.exists()) {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"));
            JSONObject object = new JSONObject();
            object.put("discordToken", "TOKEN");
            object.put("maintenance", false);
            object.put("debug", false);
            object.put("maxConnections", 20);
            writer.write(object.toJSONString());
            writer.flush();
            writer.close();
        } else {
            ((JSONObject) new JSONParser().parse(new FileReader(configFile.getPath()))).forEach((key, value) -> values.put((String) key, value));
        }

    }

    public Object getValue(String key) {
        return values.get(key);
    }

    public void saveKey(String key, Object value) {
        try {
            JSONObject object1 = (JSONObject) new JSONParser().parse(new FileReader(configFile.getPath()));
            object1.put(key, value);
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"));
            writer.write(object1.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getNewsIndex() {
        JSONObject jsonObject;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(newsFile.getPath())));
            String jsonString = reader.readLine();
            jsonObject = (JSONObject) parser.parse(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            Main.getLogger().log("ROX", "News File is empty.");

            return new JSONObject();
        }
        return jsonObject;
    }

    public File getNewsFile() {
        return newsFile;
    }

}
