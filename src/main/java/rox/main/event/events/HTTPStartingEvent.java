package rox.main.event.events;

import com.sun.net.httpserver.HttpServer;
import rox.main.event.Event;
import rox.main.event.IHandler;

public class HTTPStartingEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private HttpServer httpServer;

    public HTTPStartingEvent(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public HttpServer getHttpServer() {
        return httpServer;
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
