package rox.main;

import rox.main.command.*;
import rox.main.discord.DiscordBot;
import rox.main.server.MainServer;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static MainServer mainServer;

    private static DiscordBot discordBot;

    private static Object[] informations = new Object[16];

    private static Thread[] threads = new Thread[32];

    private static MainCommandLoader mainCommandLoader;
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
     * ["] - CONSOLE SCANNER THREAD
     *
     *
     */

    public static void main(String[] args) {
        System.out.println("Starting ROX.");
        mainCommandLoader = new MainCommandLoader();

        for (String arg : args) {
            if (arg.contains("discordtoken=")) {
                String value = arg.split("=")[1];
                informations[1] = value;
            }
        }

        (threads[0] = new Thread(() -> mainServer = new MainServer(8981))).start();
        (threads[1] = new Thread(() -> discordBot = new DiscordBot((String) informations[1]))).start();
        loadCommands();
        (threads[2] = new Thread(Main::initCommandHandle)).start();

        System.out.println(UUID.randomUUID());


    }

    private static void loadCommands() {
        mainCommandLoader.addCommand("discord", new DiscordCommand());
        mainCommandLoader.addCommand("exit", new ExitCommand());
        mainCommandLoader.addCommand("say", new SayCommand());
        mainCommandLoader.addCommand("server", new ServerCommand());
        mainCommandLoader.addCommand("token", new TokenCommand());
        mainCommandLoader.addCommand("help", new HelpCommand());
    }

    private static void initCommandHandle() {
        String input;
        while ((input = new Scanner(System.in).nextLine()) != null) {
            try {
                mainCommandLoader.getCommand(input.split(" ")[0]).command(input.split(" ")[0], input.split(" "));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Command not found.");
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

    public static Object[] getInformations() {
        return informations;
    }

    public static void setInformations(int i, Object obj) {
        informations[i] = obj;
    }

    public static MainServer getMainServer() {
        return mainServer;
    }

    public static DiscordBot getDiscordBot() {
        return discordBot;
    }

}
