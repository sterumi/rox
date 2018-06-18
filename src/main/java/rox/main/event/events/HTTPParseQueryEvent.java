package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

import java.util.Map;

public class HTTPParseQueryEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private String qry;

    private Map<String, Object> parameters;


    public HTTPParseQueryEvent(String qry, Map<String, Object> parameters) {
        this.qry = qry;
        this.parameters = parameters;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getQuery() {
        return qry;
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
