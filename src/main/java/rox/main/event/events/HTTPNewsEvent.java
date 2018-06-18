package rox.main.event.events;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import rox.main.event.Event;
import rox.main.event.IHandler;

public class HTTPNewsEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private HttpExchange exchange;

    private HttpServer httpServer;

    public HTTPNewsEvent(HttpServer httpServer, HttpExchange exchange, String response) {
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
