package rox.main.discord.commands;
/*
Created by Bleikind
*/

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import rox.main.Main;
import rox.main.discord.DiscordCommandExecutor;
import rox.main.util.RandomString;

import javax.annotation.Nullable;
import java.sql.SQLException;

public class RegisterCommand implements DiscordCommandExecutor {
    @Override
    public void command(JDA jda, @Nullable Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        try {
            if(!Main.getDatabase().Query("SELECT username FROM users WHERE username='" + sender.getNickname() + "'").next()){
                if(!Main.getDatabase().Query("SELECT discord_user_id FROM users WHERE discord_user_id='" + sender.getUser().getId() + "'").next()){
                    String password = new RandomString().nextString();
                    Main.getDatabase().Update("INSERT INTO users(username, uuid, password, discord_user_id, rank) VALUES ('" + sender.getUser().getName() + "','0','" + Main.getMathUtil().computeSHA256(password) + "','" + sender.getUser().getId() + "','User')");
                    sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du wurdest registriert. Dein Passwort für alle Interaktionen mit dem Bleibot oder ROX-System: " + password).complete());
                }else{
                    sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du bist bereits registriert!").complete());
                }
            }else{
                sender.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du bist bereits registriert!").complete());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
