package rox.main.discord.music;

import net.dv8tion.jda.core.audio.AudioSendHandler;
import sun.audio.AudioPlayer;

public class AudioPlayerSendHandling implements AudioSendHandler {

    private final AudioPlayer audioPlayer;

    public AudioPlayerSendHandling(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public boolean canProvide() {
        return false;
    }

    @Override
    public byte[] provide20MsAudio() {
        return new byte[0];
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
