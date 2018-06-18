package rox.main.event.events;

import org.json.simple.JSONArray;
import rox.main.event.Event;
import rox.main.event.IHandler;

public class NewsGetEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private int index;

    private JSONArray content, response = new JSONArray();

    public NewsGetEvent(int index, JSONArray content) {
        this.index = index;
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public JSONArray getContent() {
        return content;
    }

    public void setReturn(JSONArray array) {
        response = array;
    }

    public JSONArray getReturn() {
        return response;
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
