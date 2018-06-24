package rox.main;

import org.json.simple.JSONArray;
import rox.main.database.DBData;
import rox.main.database.Database;
import rox.main.database.Jedis;
import rox.main.discord.DiscordBot;
import rox.main.event.EventManager;
import rox.main.event.events.MainStartedEvent;
import rox.main.gamesystem.GameSystem;
import rox.main.httpserver.HTTPServer;
import rox.main.logger.Logger;
import rox.main.lua.LuaLoader;
import rox.main.news.NewsSystem;
import rox.main.javascript.JavaScriptEngine;
import rox.main.pluginsystem.PluginManager;
import rox.main.server.MainServer;
import rox.main.teamspeak.TsBot;
import rox.main.util.MathUtil;

public class Main {

    private static Database database;

    private static MainServer mainServer;

    private static DiscordBot discordBot;

    private static Object[] informatics = new Object[16];

    private static Thread[] threads = new Thread[16];

    private static MainCommandLoader mainCommandLoader;

    private static FileConfiguration fileConfiguration;

    private static HTTPServer httpServer;

    private static JavaScriptEngine javaScriptEngine;

    private static PluginManager pluginManager;

    private static NewsSystem newsSystem;

    private static Logger logger;

    private static EventManager eventManager;

    private static Jedis jedisClient;

    private static TsBot tsBot;

    private static LuaLoader luaLoader;

    private static GameSystem gameSytem;

    private static MathUtil mathUtil = new MathUtil();

    /*
     * This class is the main class.
     * It will setup all servers in a own thread.
     *
     * Informations Object Array ->
     *
     * [0] - EMPTY
     * [1] - DISCORD TOKEN
     * [2] - EMPTY
     * [3] - EMPTY
     *
     * Threads Array ->
     * [0] - MYSQL/JEDIS CLIENT THREAD
     * [1] - MAIN SERVER THREAD
     * [2] - DISCORD BOT THREAD
     * [3] - CONSOLE SCANNER THREAD
     * [4] - MINECRAFT SERVER THREAD
     * [5] - HTTP SERVER THREAD
     * [6] - TEAMSPEAK BOT THREAD
     * [7] - NEWS SYSTEM THREAD
     * [8] - PLUGIN LOADER THREAD
     * [9] - SCRIPT ENGINE THREAD
     */

    /**
     * The start up function to load everything.
     *
     * @param args All arguments given to the program
     */


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); //Boot Start Time
        (eventManager = new EventManager()).loadEvents();
        logger = new Logger(); // Init logger
        logger.log("ROX", "Starting ROX.");
        fileConfiguration = new FileConfiguration(); // Load files for information
        (mainCommandLoader = new MainCommandLoader()).loadCommands(); // Loading all system commands
        computeArgs(args); // calculate args
        loadThreads();
        logger.time("MainLoad", startTime); // writing to console how long it take to startup everything
        eventManager.callEvent(new MainStartedEvent());
    }

    private static void loadThreads() { // starts all servers and some system functions in a own thread

        long startTime = System.currentTimeMillis(); //Loading Time
        switch (((String) fileConfiguration.getValue("databaseType")).toLowerCase()) {
            default:
            case "mysql":
                (threads[0] = new Thread(() -> database = new Database(new DBData("localhost", 3306, "root", "", "rox")))).start(); // main server
                break;

            case "jedis":
                (threads[0] = new Thread(() -> jedisClient = new Jedis(new DBData("localhost", 3306, "root", "", "rox")))).start(); // Jed
                break;

        }
        (threads[1] = new Thread(() -> mainServer = new MainServer(8981))).start(); // main server
        (threads[2] = new Thread(() -> discordBot = new DiscordBot((String) informatics[1]))).start(); // discord bot
        (threads[3] = new Thread(() -> mainCommandLoader.initCommandHandle())).start(); // system command handler
        (threads[4] = new Thread(() -> gameSytem = new GameSystem(8982))).start(); // game system
        (threads[5] = new Thread(() -> httpServer = new HTTPServer(8081))).start(); // http server
        (threads[6] = new Thread(() -> tsBot = new TsBot((String) fileConfiguration.getTsValues().get("hostname"), (String) fileConfiguration.getTsValues().get("username"), (String) fileConfiguration.getTsValues().get("password")))).start(); // ts bot
        (threads[7] = new Thread(() -> newsSystem = new NewsSystem())).start(); // news system
        (threads[8] = new Thread(() -> pluginManager = new PluginManager())).start(); // plugin system

        ((JSONArray) Main.getFileConfiguration().getValue("scriptEngine")).parallelStream().forEach(o -> {
            switch ((String) o) {
                case "lua":
                    (threads[10] = new Thread(() -> luaLoader = new LuaLoader())).start(); // javascript engine
                    break;

                case "javascript":
                    (threads[9] = new Thread(() -> javaScriptEngine = new JavaScriptEngine())).start(); // javascript engine
                    break;
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown)); // Function if system exit
        logger.time("ThreadLoad", startTime); // writing to console how long it take to init threads
    }

    /**
     * Calculate arguments and save them
     *
     * @param args Arguments from main method.
     * @see Main#main
     */

    private static void computeArgs(String[] args) {
        try {
            informatics[1] = args[0];
            discordBot.setToken(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.log("ROX", "No arguments given.");
        }
    }

    /**
     * Event if system exit
     * <p>
     * stops everything and disconnect all systems from clients or servers
     */


    private static void shutdown() {

        for (Thread thread : threads) {
            if (thread != null) {
                thread.interrupt();
            }
            pluginManager.stop();
            discordBot.disconnect();
            gameSytem.stop();
            httpServer.getServer().stop(0);
            mainServer.stop();
        }
    }

    public static MainCommandLoader getMainCommandLoader() {
        return mainCommandLoader;
    }

    public static Thread getThread(int i) {
        return threads[i];
    }

    public static void setThread(int i, Thread thread) {
        if (threads[i] == null) {
            threads[threads.length + 1] = thread;
        } else {
            threads[i] = thread;
        }
    }

    public static Object[] getInformatics() {
        return informatics;
    }

    public static void setInformatics(int i, Object obj) {
        informatics[i] = obj;
    }

    public static MainServer getMainServer() {
        return mainServer;
    }

    public static DiscordBot getDiscordBot() {
        return discordBot;
    }

    public static FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public static HTTPServer getHttpServer() {
        return httpServer;
    }

    public static boolean isDebug() {

        //return (Boolean) fileConfiguration.getValue("debug");
        return true;
    }

    public static JavaScriptEngine getJavaScriptEngine() {
        return javaScriptEngine;
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static NewsSystem getNewsSystem() {
        return newsSystem;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static Database getDatabase() {
        return database;
    }

    public static MathUtil getMathUtil() { return mathUtil; }

    public static Jedis getJedisClient() {
        return jedisClient;
    }

    public static TsBot getTsBot() {
        return tsBot;
    }

    public static Thread[] getThreads() {
        return threads;
    }

    public static LuaLoader getLuaLoader() {
        return luaLoader;
    }

    public static GameSystem getGameSytem() {
        return gameSytem;
    }

    public static void setLogger(Logger logger) {
        Main.logger = logger;
    }

    public static void setThreads(Thread[] threads) {
        Main.threads = threads;
    }

    public static void setFileConfiguration(FileConfiguration fileConfiguration) {
        Main.fileConfiguration = fileConfiguration;
    }

    public static void setInformatics(Object[] informatics) {
        Main.informatics = informatics;
    }
}
