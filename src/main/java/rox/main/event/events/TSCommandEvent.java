package rox.main.event.events;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import rox.main.event.Event;
import rox.main.event.IHandler;

public class TSCommandEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private TextMessageEvent textMessageEvent;

    public TSCommandEvent(TextMessageEvent e) {
        this.textMessageEvent = e;
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

    public TextMessageEvent getTextMessageEvent() {
        return textMessageEvent;
    }
}
