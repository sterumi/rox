package rox.main;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import rox.main.discord.DiscordBot;
import rox.main.server.MainServer;

import java.util.Scanner;

public class Main {

    private static MainServer mainServer;

    private static DiscordBot discordBot;

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

    public static void main(String[] args) {
        System.out.println("Starting ROX.");
        Scanner scanner = new Scanner(System.in);

        for (String arg : args) {
            if (arg.contains("discordtoken=")) {
                String value = arg.split("=")[1];
                informations[1] = value;
            }
        }

        Thread mainServerThread = new Thread(() -> mainServer = new MainServer(8981));
        Thread discordBotThread = new Thread(() -> discordBot = new DiscordBot((String) informations[1]));
        threads[0] = mainServerThread;
        threads[1] = discordBotThread;

        mainServerThread.start();
        discordBotThread.start();


        /*
         * This handle all console inputs
         */
        String input;
        while ((input = scanner.nextLine()) != null) {
            String[] scargs = input.split(" ");

            StringBuilder strs = new StringBuilder();
            for (String str : scargs) {
                strs.append(str).append(" ");
            }
            System.out.println(strs);

            switch (scargs[0]) {
                case "exit":
                    System.exit(0);
                    break;

                case "say":
                    System.out.println(input.substring(scargs[0].length() + 1));
                    break;

                case "server":
                    switch (scargs[1]){
                        case "start":
                            if(scargs.length == 2){
                                if (mainServer.isActive()) {
                                    System.out.println("Server is already running.");
                                }else{
                                    System.out.println("Starting Main Server...");
                                    mainServer.start();
                                }
                            }else{
                                System.out.println("server (start,stop)");
                            }
                            break;

                        case "stop":
                            if(scargs.length == 2){
                                if (mainServer.isActive()) {
                                    System.out.println("Main Server stopping...");
                                    mainServer.stop();
                                }else{
                                    System.out.println("Main server isn't running.");
                                }
                            }else{
                                System.out.println("server (start,stop)");
                            }
                            break;

                            default: System.out.println("server (start,stop)"); break;
                    }

                    break;
                case "discord":


                    // discord (token,start,stop) <tokenCode>
                    switch (scargs[1]) {

                        case "token":

                            if (scargs.length == 3) {
                                if (scargs[2] != null) {
                                    informations[1] = scargs[2];
                                    discordBot.setToken(scargs[2]);
                                    System.out.println("Token is set.");
                                } else {
                                    System.out.println("You must set a token!");
                                }
                            } else {
                                System.out.println("discord (token,start,stop) <tokenCode>");
                            }
                            break;

                        case "start":
                            if (scargs.length == 2) {
                                if (discordBot.isConnected()) {
                                    System.out.println("Discord Bot is already running!");
                                } else {
                                    discordBot.connect();
                                    System.out.println("Started Discord Bot.");
                                }
                            } else {
                                System.out.println("discord start");
                            }

                            break;

                        case "stop":
                            if (scargs.length == 2) {
                                if (discordBot.isConnected()) {
                                    discordBot.disconnect();
                                    System.out.println("Stopped Discord Bot.");
                                } else {
                                    System.out.println("Discord Bot isn't running.");
                                }
                            } else {
                                System.out.println("discord stop");
                            }
                            break;

                        case "say":
                                if(scargs.length > 3){
                                    StringBuilder message = new StringBuilder();
                                    for (int i = 3; i < scargs.length; i++) message.append(scargs[i]).append(" ");

                                    TextChannel textChannel =discordBot.getJDA().getTextChannelsByName(scargs[2], true).get(0);

                                    MessageAction messageAction = textChannel.sendMessage(message.toString());
                                    messageAction.complete();
                                }else{
                                    System.out.println("discord say <channel> <message>");
                                }
                            break;

                        default:
                            System.out.println("discord (token,start,stop) <tokenCode>");
                            break;

                    }
                    break;


            }
        }

    }

    public static MainServer getMainServer() {
        return mainServer;
    }

    public static DiscordBot getDiscordBot() {
        return discordBot;
    }

}
