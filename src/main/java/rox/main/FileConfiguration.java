package rox.main;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FileConfiguration {

    private ConcurrentHashMap<String, File> files = new ConcurrentHashMap<>();

    private JSONParser parser;

    private ConcurrentHashMap<String, Object> values, ts_values, lua_values, commands_values;

    private boolean setting_up;

    /**
     * Load all files to a var.
     */

    FileConfiguration() {
        setting_up = true;
        try {
            values = new ConcurrentHashMap<>();
            ts_values = new ConcurrentHashMap<>();
            lua_values = new ConcurrentHashMap<>();
            commands_values = new ConcurrentHashMap<>();
            files.put("config", new File("config/", "config.json"));
            files.put("tsbot", new File("config/", "tsbot.json"));
            files.put("news", new File("config/", "news.json"));
            files.put("lua", new File("config/", "lua.json"));
            files.put("commands", new File("commands.json"));
            for (File file : Objects.requireNonNull(new File("config/").listFiles())) {
                if (!files.containsKey(file.getName().replace(".json", "").toLowerCase()))
                    files.put(file.getName().replace(".json", ""), file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        parser = new JSONParser(); // parser for json

        try {
            init(); // execute init method
        } catch (Exception e) {
            e.printStackTrace();
        }
        setting_up = false;
    }

    /**
     * Loads everything from news file or write a new news file.
     *
     * @throws Exception Mostly JsonExceptions or IOExceptions
     */

    public boolean isSettingUp() {
        return setting_up;
    }

    private void init() throws Exception {
        files.get("config").getParentFile().mkdirs(); // creates root dir if not exist
        if (!files.get("config").exists()) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(files.get("config")), "utf-8"), true); // create writer to file
            JSONObject object = new JSONObject();
            object.put("maintenance", false);
            object.put("debug", false);
            object.put("maxConnections", 20);
            object.put("gui", true);
            object.put("databaseType", "mysql");
            object.put("scriptEngine", new JSONArray().put("javascript").put("lua"));
            writer.write(object.toJSONString()); // write json string to file
            writer.close(); // close writer after finish
            object.forEach((o, o2) -> values.put((String) o, o2));
        }
        ((JSONObject) new JSONParser().parse(new FileReader(files.get("config").getPath()))).forEach((key, value) -> values.put((String) key, value)); // loads content of config file to a hashmap


        if (!files.get("tsbot").exists()) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(files.get("tsbot")), "utf-8"), true); // create writer to file
            JSONObject object = new JSONObject();
            object.put("hostname", "localhost");
            object.put("username", "serveradmin");
            object.put("password", "serveradminpassword");
            object.put("autoRefreshNetwork", true);
            object.put("refreshDelay", 60);
            writer.write(object.toJSONString()); // write json string to file
            writer.close(); // close writer after finish
            object.forEach((o, o2) -> values.put((String) o, o2));
        }
        ((JSONObject) new JSONParser().parse(new FileReader(files.get("tsbot").getPath()))).forEach((key, value) -> ts_values.put((String) key, value)); // loads content of tsbot file to a hashmap


        if (!files.get("lua").exists()) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(files.get("lua")), "utf-8"), true); // create writer to file
            JSONObject object = new JSONObject();
            object.put("mainFile", "boot.lua");
            object.put("loadExternal", false);
            writer.write(object.toJSONString()); // write json string to file
            writer.close(); // close writer after finish
            object.forEach((o, o2) -> values.put((String) o, o2));
        }
        ((JSONObject) new JSONParser().parse(new FileReader(files.get("lua").getPath()))).forEach((key, value) -> lua_values.put((String) key, value)); // loads content of lua file to a hashmap

        if (!files.get("commands").exists()) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(files.get("commands")), "utf-8"), true); // create writer to file
            JSONObject object = new JSONObject();
            JSONObject commands = new JSONObject();
            commands.put("discord", "To start the discord bot.");
            commands.put("exe", "To execute a internal command via a extra command.");
            commands.put("exit", "To safe-exit the program.");
            commands.put("fds", "Starting all server and bots directly.");
            commands.put("gs", "To start the game system service.");
            commands.put("help", "To display all commands.");
            commands.put("mem", "To display used memory and free memory.");
            commands.put("say", "Say something in this console.");
            commands.put("scripts", "to show all available script engines, to reload or execute a script file.");
            commands.put("server", "The main command of the main server for the client service.");
            commands.put("sha", "To create a SHA-256 password.");
            commands.put("token", "to set the discord token easily.");
            commands.put("ts", "To start the teamspeak bot.");
            commands.put("uuid", "To create a UUID randomly.");
            commands.put("test", "Just a test command.");
            object.put("commands", commands);
            writer.write(object.toJSONString()); // write json string to file
            writer.close(); // close writer after finish
            object.forEach((o, o2) -> commands_values.put((String) o, o2));
        }
        ((JSONObject) new JSONParser().parse(new FileReader(files.get("commands").getPath()))).forEach((key, value) -> commands_values.put((String) key, value)); // loads content of commands file to a hashmap


    }


    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
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
            JSONObject object1 = (JSONObject) new JSONParser().parse(new FileReader(files.get("config").getPath())); // reloading full config file
            object1.put(key, value); // adding new key and value
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(files.get("config")), "utf-8"), true); // set a new writer
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(files.get("news").getPath())));
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

    public ConcurrentHashMap<String, Object> getTsValues() {
        return ts_values;
    }

    public boolean existFile(String name) {
        return files.containsKey(name);
    }

    public File getFile(String name) {
        if (!files.containsKey(name)) return null;
        return files.get(name);
    }

    public File getNewsFile() {
        return files.get("news");
    }

    public ConcurrentHashMap<String, Object> getLuaValues() {
        return lua_values;
    }

    public ConcurrentHashMap<String, File> getFiles() {
        return files;
    }

    public ConcurrentHashMap<String, Object> getValues() {
        return values;
    }

    public JSONParser getParser() {
        return parser;
    }

    public void setFiles(ConcurrentHashMap<String, File> files) {
        this.files = files;
    }

    public void setParser(JSONParser parser) {
        this.parser = parser;
    }

    public void setValues(ConcurrentHashMap<String, Object> values) {
        this.values = values;
    }

    public ConcurrentHashMap<String, Object> getCommandsValues() {
        return commands_values;
    }
}
