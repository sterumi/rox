package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

public class NewsAddEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private String text, author;

    public NewsAddEvent(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
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
