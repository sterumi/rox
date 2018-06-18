package rox.main.event.events;

import net.dv8tion.jda.core.JDA;
import rox.main.event.Event;
import rox.main.event.IHandler;

public class DiscordStartingEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private JDA jda;

    public DiscordStartingEvent(JDA jda) {
        this.jda = jda;
    }

    public JDA getJda() {
        return jda;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    @Override
    public IHandler getHandler() {
        return list;
    }

    public static IHandler getHandlerList() {
        return list;
    }

}
