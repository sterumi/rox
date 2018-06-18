package rox.main.command;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import rox.main.Main;
import rox.main.MainCommandExecutor;
import rox.main.event.events.DiscordCommandExecuteEvent;

public class DiscordCommand implements MainCommandExecutor {

    @Override
    public void command(String name, String[] args) {
        // discord (token,start,stop) <tokenCode>

        if (args.length == 1) {
            Main.getLogger().log("ROX", "discord (token,start,stop) <tokenCode>");
            return;
        }

        switch (args[1]) {

            case "token":

                if (args.length == 3) {
                    if (args[2] != null) {
                        Main.getDiscordBot().setToken(args[2]);
                        Main.setInformatics(1, args[2]);
                        Main.getLogger().log("ROX", "Token is set.");
                    } else {
                        Main.getLogger().log("ROX", "You must set a token!");
                    }
                } else {
                    Main.getLogger().log("ROX", "discord (token,start,stop) <tokenCode>");
                }
                break;

            case "start":
                if (args.length == 2) {
                    if (Main.getDiscordBot().isConnected()) {
                        Main.getLogger().log("ROX", "Discord Bot is already running!");
                    } else {
                        Main.getDiscordBot().connect();
                        Main.getLogger().log("ROX", "Started Discord Bot.");
                    }
                } else {
                    Main.getLogger().log("ROX", "discord start");
                }

                break;

            case "stop":
                if (args.length == 2) {
                    if (Main.getDiscordBot().isConnected()) {
                        Main.getDiscordBot().disconnect();
                        Main.getLogger().log("ROX", "Stopped Discord Bot.");
                    } else {
                        Main.getLogger().log("ROX", "Discord Bot isn't running.");
                    }
                } else {
                    Main.getLogger().log("ROX", "discord stop");
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
                    Main.getLogger().log("ROX", "discord say <channel> <message>");
                }
                break;

            default:
                Main.getLogger().log("ROX", "discord (token,start,stop) <tokenCode>");
                break;

        }
    }
}
