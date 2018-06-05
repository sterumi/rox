package rox.main.discord.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import rox.main.Main;
import rox.main.discord.DiscordCommandExecutor;

public class WarnCommand implements DiscordCommandExecutor {
    @Override
    public void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        if (args.length == 2) {
            if (sender.getRoles().get(0).getPosition() >= 7) {

                if (Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE discord_user_id='" + sender.getUser().getId() + "'") == null) {
                    sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Dieser User muss eine eingetragene ID haben in der Datenbank. Bitte melde dies dem Main-Manager.").complete());
                    return;
                }

                Main.getDiscordBot().addPoint(guild, sender.getUser().getId());
            } else {
                sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du hast keine Berechtigung dafür!").complete());
            }
        }
    }
}
