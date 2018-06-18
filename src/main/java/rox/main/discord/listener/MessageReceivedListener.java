package rox.main.discord.listener;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import rox.main.Main;
import rox.main.event.events.DiscordCommandExecuteEvent;

public class MessageReceivedListener implements EventListener {

    @Override
    public void onEvent(Event event) {
        if(event instanceof MessageReceivedEvent){

            String[] args = ((MessageReceivedEvent) event).getMessage().getContentRaw().split(" ");

            if (((MessageReceivedEvent) event).getMessage().getContentRaw().startsWith("!")) {

                DiscordCommandExecuteEvent discordCommandExecuteEvent = new DiscordCommandExecuteEvent(event.getJDA(), ((MessageReceivedEvent) event).getGuild(),
                        ((MessageReceivedEvent) event).getMember(),
                        ((MessageReceivedEvent) event).getTextChannel(), args[0], args);
                Main.getEventManager().callEvent(discordCommandExecuteEvent);
                if (discordCommandExecuteEvent.isCancelled()) return;

                Main.getDiscordBot().getCommandLoader().getCommand(args[0])
                        .command(event.getJDA(), ((MessageReceivedEvent) event).getGuild(),
                                ((MessageReceivedEvent) event).getMember(),
                                ((MessageReceivedEvent) event).getTextChannel(), args[0], args);
            }
        }
    }
}
