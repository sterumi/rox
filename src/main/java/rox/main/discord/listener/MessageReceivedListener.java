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

            if(((MessageReceivedEvent) event).getMessage().getContentRaw().toLowerCase().contains("fuck")){
                ((MessageReceivedEvent) event).getMessage().editMessage(((MessageReceivedEvent) event).getMessage().getContentRaw().toLowerCase().replace("fuck", "firetruck")).complete();
            }

            String[] args = ((MessageReceivedEvent) event).getMessage().getContentRaw().split(" ");
            if (((MessageReceivedEvent) event).getMessage().getContentRaw().toLowerCase().startsWith("#")) {

                DiscordCommandExecuteEvent discordCommandExecuteEvent = new DiscordCommandExecuteEvent(event.getJDA(), ((MessageReceivedEvent) event).getGuild(),
                        ((MessageReceivedEvent) event).getMember(),
                        ((MessageReceivedEvent) event).getTextChannel(), args[0].toLowerCase(), args);
                Main.getEventManager().callEvent(discordCommandExecuteEvent);
                if (discordCommandExecuteEvent.isCancelled()) return;

                Main.getDiscordBot().getCommandLoader().getCommand(args[0])
                        .command(event.getJDA(), ((MessageReceivedEvent) event).getGuild(),
                                ((MessageReceivedEvent) event).getMember(),
                                ((MessageReceivedEvent) event).getTextChannel(), args[0].toLowerCase(), args);
            }
        }
    }
}
