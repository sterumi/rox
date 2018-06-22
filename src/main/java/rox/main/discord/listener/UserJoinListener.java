package rox.main.discord.listener;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.apache.commons.lang.RandomStringUtils;
import rox.main.Main;

public class UserJoinListener implements EventListener {

    private char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?").toCharArray();

    @Override
    public void onEvent(Event event) {

        if (event instanceof GuildMemberJoinEvent) {

            GuildMemberJoinEvent guildMemberJoinEvent = (GuildMemberJoinEvent) event;
            guildMemberJoinEvent.getMember().getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Willkommen, " + guildMemberJoinEvent.getMember().getNickname() + "! Bitte schau in den #regeln Chat dir die Regeln an!").complete());
            Main.getDatabase().Update("INSERT INTO users(username, password, discord_user_id) VALUES ('" + guildMemberJoinEvent.getUser().getName() + "','" + RandomStringUtils.random(10, possibleCharacters) + "','" + guildMemberJoinEvent.getUser().getId() + "')");

        }

    }
}
