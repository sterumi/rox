package rox.main.discord.music;

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import rox.main.Main;
import sun.audio.AudioPlayer;

import javax.security.auth.login.LoginException;

public class MusicStreamThread extends Thread {

    private JDA jda;

    private Guild guild;

    private long musicChannelID = 348114778641793024L;


    public MusicStreamThread(Guild guild, long ChannelID) {
        this.jda = guild.getJDA();
        this.guild = guild;
        if (ChannelID != 0) {
            this.musicChannelID = ChannelID;
        }

        run();
    }

    @Override
    public void run() {
        try {
            AudioManager audioManager = guild.getAudioManager();
            audioManager.openAudioConnection(guild.getVoiceChannelById(musicChannelID));
            audioManager.setSendingHandler(new AudioPlayerSendHandler(AudioPlayer.player));

            //Main.getDiscordBot().getCommandChannel().sendMessage("The music player is currently not implemented.").complete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public class AudioPlayerSendHandler implements AudioSendHandler {
        private final AudioPlayer audioPlayer;
        private AudioFrame lastFrame;

        public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
            this.audioPlayer = audioPlayer;
        }

        @Override
        public boolean canProvide() {
            return lastFrame != null;
        }

        @Override
        public byte[] provide20MsAudio() {
            return lastFrame.getData();
        }

        @Override
        public boolean isOpus() {
            return true;
        }
    }
}
