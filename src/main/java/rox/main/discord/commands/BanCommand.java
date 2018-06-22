package rox.main.discord.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import rox.main.Main;
import rox.main.discord.DiscordCommandExecutor;

public class BanCommand implements DiscordCommandExecutor {
    @Override
    public void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        if (args.length == 2) {
            if (sender.getRoles().get(0).getPosition() >= 7) {

                if (Main.getDatabase().Query("SELECT * FROM users WHERE discord_user_id='" + sender.getUser().getId() + "'") == null) {
                    sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Dieser User muss eine eingetragene ID haben in der Datenbank. Bitte melde dies dem Main-Manager.").complete());
                    return;
                }
                try {
                    User userToBan = jda.getUserById(Long.parseLong(args[1]));
                    if (userToBan == null) {
                        sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Dieser Benutzer existiert nicht!").complete());
                        return;
                    }

                    Main.getDiscordBot().ban(guild, userToBan.getId());
                    jda.getTextChannelById(449315175783202816L).sendMessage(sender.getUser().getName() + " hat " + userToBan.getName() + " gebannt!").complete();


                } catch (NumberFormatException e) {
                    sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du musst die UserID angeben! Wie? Schalte Entwicklermodus an unter 'Benutzereinstellungen -> Erscheinungsbild -> Entwickermodus' dann musst du auf den jeweiligen Benutzer rechtsklick drücken und 'ID kopieren anklicken'.").complete());

                }
            } else {
                sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du hast keine Berechtigung dafür!").complete());
            }
        }
    }
}
