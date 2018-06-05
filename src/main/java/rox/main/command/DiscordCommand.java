package rox.main.command;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import rox.main.Main;
import rox.main.MainCommandExecutor;

public class DiscordCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        // discord (token,start,stop) <tokenCode>

        if (args.length == 1) {
            System.out.println("discord (token,start,stop) <tokenCode>");
            return;
        }

        switch (args[1]) {

            case "token":

                if (args.length == 3) {
                    if (args[2] != null) {
                        Main.getDiscordBot().setToken(args[2]);
                        Main.setInformations(1, args[2]);
                        System.out.println("Token is set.");
                    } else {
                        System.out.println("You must set a token!");
                    }
                } else {
                    System.out.println("discord (token,start,stop) <tokenCode>");
                }
                break;

            case "start":
                if (args.length == 2) {
                    if (Main.getDiscordBot().isConnected()) {
                        System.out.println("Discord Bot is already running!");
                    } else {
                        Main.getDiscordBot().connect();
                        System.out.println("Started Discord Bot.");
                    }
                } else {
                    System.out.println("discord start");
                }

                break;

            case "stop":
                if (args.length == 2) {
                    if (Main.getDiscordBot().isConnected()) {
                        Main.getDiscordBot().disconnect();
                        System.out.println("Stopped Discord Bot.");
                    } else {
                        System.out.println("Discord Bot isn't running.");
                    }
                } else {
                    System.out.println("discord stop");
                }
                break;

            case "say":
                if (args.length > 3) {
                    StringBuilder message = new StringBuilder();
                    for (int i = 3; i < args.length; i++) message.append(args[i]).append(" ");

                    TextChannel textChannel = Main.getDiscordBot().getJDA().getTextChannelsByName(args[2], true).get(0);

                    MessageAction messageAction = textChannel.sendMessage(message.toString());
                    messageAction.complete();
                } else {
                    System.out.println("discord say <channel> <message>");
                }
                break;

            default:
                System.out.println("discord (token,start,stop) <tokenCode>");
                break;

        }
    }
}
