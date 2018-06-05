package rox.main.discord.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import rox.main.discord.DiscordCommandExecutor;

public class SayCommand implements DiscordCommandExecutor {
    @Override
    public void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        if (args.length > 1) {
            if (sender.getRoles().get(0).getPosition() >= 7) {
                StringBuilder str = new StringBuilder();
                for (int i = 1; i < args.length; i++) str.append(args[i]);
                currentChannel.sendMessage(str.toString()).complete();
            } else {
                sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du hast keine Berechtigung dafür!").complete());
            }
        }
    }
}
