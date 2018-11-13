package rox.main.discord.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import rox.main.discord.DiscordCommandExecutor;

import java.util.Random;

public class DiceCommand implements DiscordCommandExecutor {

    @Override
    public void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        if(sender.hasPermission(Permission.MESSAGE_WRITE)){
            currentChannel.sendMessage("```" + sender.getNickname() + " hat eine " + new Random().nextInt(100) + " gewürfelt!```").complete();
        }else{
            sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du hast keine Berechtigung dafür!").complete());
        }
    }
}
