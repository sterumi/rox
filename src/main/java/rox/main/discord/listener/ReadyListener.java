package rox.main.discord.listener;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;

import java.util.EventListener;

public class ReadyListener implements EventListener {

    public void onEvent(Event event){
        if(event instanceof ReadyEvent){
            System.out.println("Bot connected!");
        }
    }

}
