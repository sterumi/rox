package rox.main.discord.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import rox.main.discord.DiscordCommandExecutor;

public class UserCommand implements DiscordCommandExecutor {
    @Override
    public void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        // !user <name>
        // Response: First Name, Rank, Description, Social Media,...

        if (args.length == 2) {
            User target = (jda.getUsersByName(args[1], true).get(0));
            switch (target.getName().toLowerCase()) {
                case "bleikind":

                    sender.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("User: Bleikind\n Rank: Creator of ROX, Team-Manager, Projekt-Manager & Main-Developer\n Description: " +
                            "Hat mich programmiert!\nSocialMedia:\n YouTube: bleikind\n Twitch: bleikind\n Twitter: bleikind\n Sogut wie überall mit dem Namen 'Bleikind'" +
                            " findbar.").complete());

                    break;
                case "redstonefuture":
                    sender.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("User: RedstoneFuture\n Rank: Projekt-Manager & Designer\n Description: " +
                            "Verteilt Mitglieder bei Aufträgen\nSocialMedia:\n YouTube: RedstoneFuture TV\n Twitch: RedstoneFuture\n Twitter: RedstoneFuture\n Sogut wie überall mit dem Namen 'RedstoneFuture'" +
                            " findbar.").complete());
                    break;
                case "missflauschi":
                    sender.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("User: MissFlauschi\n Rank: Main-Designer\n Description: " +
                            "Verwaltet alle Designer Aufträge.\nSocialMedia:\n YouTube: MissFlauschi TV\n Twitch: MissFlauschi\n  Sogut wie überall mit dem Namen 'MissFlauschi'" +
                            " findbar.").complete());
                    break;
                case "centrix":
                    sender.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("User: Centrix\n Rank: Supporter & Social-Media\n Description: " +
                            "Ist für das Social-Media verantwortlich.\nSocialMedia:\n Twitter: CentrixD\n  Sogut wie überall mit dem Namen 'Centrix'" +
                            " findbar.").complete());
                    break;

                default:
                    sender.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Dieser Befehl ist gerade in Aufbau und nur zur Demonstration! Bitte sei geduldig :)").complete());
                    break;

            }
        }
    }
}
