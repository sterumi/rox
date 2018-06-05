package rox.main.discord.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import rox.main.discord.DiscordCommandExecutor;
import rox.main.discord.music.MusicStreamThread;

public class MediaPlayerCommand implements DiscordCommandExecutor {

    private MusicStreamThread musicStreamClass;

    private Thread musicStreamThread;

    @Override
    public void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        if (sender.getRoles().get(0).getPosition() >= 7) {
            musicStreamThread = new Thread(() -> musicStreamClass = new MusicStreamThread(guild, 451033355136401415L));
            musicStreamThread.start();
        } else {
            sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du hast keine Berechtigung dafür!").complete());
        }
    }
}
