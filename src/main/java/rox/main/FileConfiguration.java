package rox.main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileConfiguration {

    private File configFile, newsFile;

    private JSONParser parser;

    private ConcurrentHashMap<String, Object> values;


    /**
     * Load all files to a var.
     */

    FileConfiguration() {
        long startTime = System.currentTimeMillis();
        try {
            values = new ConcurrentHashMap<>();
            configFile = new File("config/", "config.json"); // load config file
            newsFile = new File("config/", "news.json"); // load news file
        } catch (Exception e) {
            e.printStackTrace();
        }

        parser = new JSONParser(); // parser for json
        try {
            init(); // execute init method
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main.getLogger().time("FileLoad", startTime);
    }

    /**
     * Loads everything from news file or write a new news file.
     *
     * @throws Exception Mostly JsonExceptions or IOExceptions
     */

    private void init() throws Exception {
        configFile.getParentFile().mkdirs(); // creates root dir if not exist
        if (!configFile.exists()) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"), true); // create writer to file
            JSONObject object = new JSONObject();
            object.put("discordToken", "TOKEN");
            object.put("maintenance", false);
            object.put("debug", false);
            object.put("maxConnections", 20);
            writer.write(object.toJSONString()); // write json string to file
            writer.close(); // close writer after finish
        } else {
            ((JSONObject) new JSONParser().parse(new FileReader(configFile.getPath()))).forEach((key, value) -> values.put((String) key, value)); // loads content of news file to a hashmap
        }

    }

    public Object getValue(String key) {
        return values.get(key);
    }

    /**
     * Save a different key to the configFile for later.
     *
     * @param key   The key to find the saved object
     * @param value The object to save
     */

    public void saveKey(String key, Object value) {
        try {
            JSONObject object1 = (JSONObject) new JSONParser().parse(new FileReader(configFile.getPath())); // reloading full config file
            object1.put(key, value); // adding new key and value
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"), true); // set a new writer
            writer.write(object1.toJSONString()); // write the content to the config file
            writer.close(); // close writer
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loading the full news file again
     *
     * @return The whole content of the news file in a JSONObject
     */

    public JSONObject getNewsIndex() {
        JSONObject jsonObject;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(newsFile.getPath())));
            StringBuilder jsonString = new StringBuilder();
            reader.lines().forEach(jsonString::append);
            jsonObject = (JSONObject) parser.parse(jsonString.toString());
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
