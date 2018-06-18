package rox.main.event.events;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import rox.main.event.Event;
import rox.main.event.IHandler;

public class HTTPGetEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private HttpExchange exchange;

    private HttpServer httpServer;

    public HTTPGetEvent(HttpServer httpServer, HttpExchange exchange) {
        this.exchange = exchange;
        this.httpServer = httpServer;
    }

    public HttpExchange getHttpExchange() {
        return exchange;
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
