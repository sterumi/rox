package rox.main.discord;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

public interface DiscordCommandExecutor {

    void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args);

}
