package rox.main.discord.listener;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.concurrent.atomic.AtomicReference;

public class UserJoinListener implements EventListener {

    @Override
    public void onEvent(Event event) {

        if (event instanceof GuildMemberJoinEvent) {

            AtomicReference<GuildMemberJoinEvent> guildMemberJoinEvent = new AtomicReference<>((GuildMemberJoinEvent) event);
            guildMemberJoinEvent.get().getMember().getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Willkommen, " + guildMemberJoinEvent.get().getMember().getNickname() + "! Bitte schau in den #regeln Chat dir die Regeln an!").complete());
        }

    }
}
