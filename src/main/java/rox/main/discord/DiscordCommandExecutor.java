package rox.main.discord;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.annotation.Nullable;

public interface DiscordCommandExecutor {

    void command(JDA jda, @Nullable Guild guild, Member sender, TextChannel currentChannel, String name, String[] args);

}
