package rox.main;

import rox.main.discord.DiscordBot;
import rox.main.server.MainServer;

import java.util.Scanner;

public class Main {

    private static MainServer mainServer;

    private static DiscordBot discordBot;

    private static Scanner scanner;

    private static Object[] informations = new Object[16];

    private static Thread[] threads = new Thread[32];
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
    *
    *
     */

    public static void main(String[] args){
        System.out.println("Starting ROX.");
        scanner = new Scanner(System.in);

        for (String arg : args) {
            if(arg.contains("discordtoken=")){
                String value = arg.split("=")[1];
                informations[1] = value;
            }
        }

        Thread mainServerThread = new Thread(() -> {  mainServer = new MainServer(8981); mainServer.start(); });
        Thread discordBotThread = new Thread(() -> {  discordBot = new DiscordBot((String)informations[1]); discordBot.connect(); });
        threads[0] = mainServerThread;
        threads[1] = discordBotThread;

        mainServerThread.start();
        discordBotThread.start();


        /*
        * This handle all console inputs
         */
        String input;
        while((input = scanner.nextLine()) != null){
            String[] scargs = input.split(" ");
            switch(scargs[0]){
                case "stop":
                    System.exit(0);
                    break;

                case "say":
                    System.out.println(input.substring(scargs[0].length() + 1));
                    break;
            }
        }

    }

}
