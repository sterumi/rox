package rox.main.discord.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import rox.main.Main;
import rox.main.discord.DiscordCommandExecutor;

public class SwitchCommand implements DiscordCommandExecutor {
    @Override
    public void command(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        if(sender.hasPermission(Permission.ADMINISTRATOR)){
            Main.getDiscordBot().switchGame();
        }else{
            sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du hast keine Berechtigung dafür!").complete());
        }
    }
}
