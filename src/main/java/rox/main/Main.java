package rox.main;

import rox.main.discord.DiscordBot;
import rox.main.httpserver.HTTPServer;
import rox.main.logger.Logger;
import rox.main.minecraftserver.MinecraftServer;
import rox.main.news.NewsSystem;
import rox.main.pluginsystem.JavaScriptEngine;
import rox.main.pluginsystem.PluginManager;
import rox.main.server.MainServer;
public class Main {

    private static MainServer mainServer;

    private static DiscordBot discordBot;

    private static Object[] informatics = new Object[16];

    private static Thread[] threads = new Thread[16];

    private static MainCommandLoader mainCommandLoader;

    private static FileConfiguration fileConfiguration;

    private static MinecraftServer minecraftServer;

    private static HTTPServer httpServer;

    private static JavaScriptEngine javaScriptEngine;

    private static PluginManager pluginManager;

    private static NewsSystem newsSystem;

    private static Logger logger;

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
     *
     * [0] - MAIN SERVER THREAD
     * [1] - DISCORD BOT THREAD
     * [2] - CONSOLE SCANNER THREAD
     * [3] - MINECRAFT SERVER THREAD
     * [4] - HTTP SERVER THREAD
     * [5] - NEWS SYSTEM THREAD
     * [6] - PLUGIN LOADER THREAD
     * [7] - SCRIPT ENGINE THREAD
     */

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        logger = new Logger();
        logger.log("ROX", "Starting ROX.");
        fileConfiguration = new FileConfiguration();
        (mainCommandLoader = new MainCommandLoader()).loadCommands();
        loadThreads();
        computeArgs(args);
        logger.time("MainLoad", startTime);
    }

    private static void loadThreads() {
        long startTime = System.currentTimeMillis();
        (threads[0] = new Thread(() -> mainServer = new MainServer(8981))).start();
        (threads[1] = new Thread(() -> discordBot = new DiscordBot((String) informatics[1]))).start();
        (threads[2] = new Thread(() -> mainCommandLoader.initCommandHandle())).start();
        (threads[3] = new Thread(() -> minecraftServer = new MinecraftServer(8982))).start();
        (threads[4] = new Thread(() -> httpServer = new HTTPServer(8081))).start();
        (threads[5] = new Thread(() -> newsSystem = new NewsSystem())).start();
        (threads[6] = new Thread(() -> pluginManager = new PluginManager())).start();
        (threads[7] = new Thread(() -> javaScriptEngine = new JavaScriptEngine())).start();
        Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown));
        logger.time("ThreadLoad", startTime);
    }

    private static void computeArgs(String[] args) {
        try {
            informatics[1] = args[0];
            discordBot.setToken(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.log("ROX", "No arguments given.");
        }
    }

    private static void shutdown() {
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) {
                threads[i].interrupt();
            }
            pluginManager.stop();
            discordBot.disconnect();
            minecraftServer.stop();
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

    public static MinecraftServer getMinecraftServer() {
        return minecraftServer;
    }

    public static HTTPServer getHttpServer() {
        return httpServer;
    }

    public static boolean isDebug() {

        //return (Boolean) fileConfiguration.getValue("debug");
        return true;
    }

    public JavaScriptEngine getJavaScriptEngine() {
        return javaScriptEngine;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public static NewsSystem getNewsSystem() {
        return newsSystem;
    }

    public static Logger getLogger() {
        return logger;
    }
}
