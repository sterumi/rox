package rox.main.discord.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import rox.main.discord.DiscordCommandExecutor;

public class EmoteCommand implements DiscordCommandExecutor {
    @Override
    public void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        if(name.startsWith("!:")){
            if(args.length >= 1){
                StringBuilder builder = new StringBuilder();
                builder.append(args[0].substring(1));
                for(int i = 1; i < args.length; i++) builder.append(args[i]);
                currentChannel.sendMessage(builder.toString()).complete();
                currentChannel.deleteMessageById(currentChannel.getLatestMessageId()).complete();
            }
        }
    }
}
