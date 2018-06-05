package rox.main.discord.music;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import rox.main.Main;

public class MusicStreamThread extends Thread {

    private JDA jda;

    private Guild guild;

    private long musicChannelID = 451033355136401415L;


    public MusicStreamThread(Guild guild, long ChannelID) {
        this.jda = guild.getJDA();
        this.guild = guild;
        if (ChannelID != 0) this.musicChannelID = ChannelID;
        run();
    }

    @Override
    public void run() {
        try {

            Main.getDiscordBot().getCommandChannel().sendMessage("The music player is currently not implemented.").complete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
