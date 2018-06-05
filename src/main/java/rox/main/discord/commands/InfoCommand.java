package rox.main.discord.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import rox.main.discord.DiscordCommandExecutor;

public class InfoCommand implements DiscordCommandExecutor {


    @Override
    public void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        if (args.length == 1) {
            currentChannel.sendMessage("Ich bin doch Online?").complete();
        }
    }
}
