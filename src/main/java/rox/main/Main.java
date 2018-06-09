package rox.main;

import rox.main.command.*;
import rox.main.discord.DiscordBot;
import rox.main.httpserver.HTTPServer;
import rox.main.logger.Logger;
import rox.main.minecraftserver.MinecraftServer;
import rox.main.news.NewsSystem;
import rox.main.pluginsystem.JavaScriptEngine;
import rox.main.pluginsystem.PluginManager;
import rox.main.server.MainServer;

import java.util.Scanner;

public class Main {

    private static MainServer mainServer;

    private static DiscordBot discordBot;

    private static Object[] informatics = new Object[16];

    private static Thread[] threads = new Thread[32];

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
     *
     *
     */

    public static void main(String[] args) {
        logger = new Logger();
        logger.log("ROX", "Starting ROX.");
        fileConfiguration = new FileConfiguration();
        mainCommandLoader = new MainCommandLoader();

        (threads[0] = new Thread(() -> mainServer = new MainServer(8981))).start();
        (threads[1] = new Thread(() -> discordBot = new DiscordBot((String) informatics[1]))).start();
        (threads[3] = new Thread(() -> minecraftServer = new MinecraftServer(8982))).start();
        (threads[4] = new Thread(() -> httpServer = new HTTPServer(8081))).start();
        loadCommands();
        (threads[2] = new Thread(Main::initCommandHandle)).start();
        try {
            informatics[1] = args[0];
            discordBot.setToken(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.log("ROX", "No arguments given.");
        }

        (javaScriptEngine = new JavaScriptEngine()).init();
        try {
            (pluginManager = new PluginManager()).loadPlugins();
        } catch (Exception e) {
            e.printStackTrace();
        }

        newsSystem = new NewsSystem();

    }

    private static void loadCommands() {
        mainCommandLoader.addCommand("discord", new DiscordCommand());
        mainCommandLoader.addCommand("exit", new ExitCommand());
        mainCommandLoader.addCommand("say", new SayCommand());
        mainCommandLoader.addCommand("server", new ServerCommand());
        mainCommandLoader.addCommand("token", new TokenCommand());
        mainCommandLoader.addCommand("help", new HelpCommand());
        mainCommandLoader.addCommand("uuid", new UUIDCommand());
        mainCommandLoader.addCommand("fds", new FastDebugStartCommand());
        mainCommandLoader.addCommand("exe", new ExecuteCommand());
        mainCommandLoader.addCommand("mcs", new MCSCommand());
        mainCommandLoader.addCommand("test", (name, args) -> System.out.println("test"));
    }

    private static void initCommandHandle() {
        String input;
        while ((input = new Scanner(System.in).nextLine()) != null) {
            try {
                mainCommandLoader.getCommand(input.split(" ")[0]).command(input.split(" ")[0], input.split(" "));
            } catch (Exception e) {
                e.printStackTrace();
                logger.log("ROX", "Command not found.");
            }
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
        return (Boolean) fileConfiguration.getValue("debug");
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
