package rox.main.event.events;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import rox.main.event.Event;
import rox.main.event.IHandler;

import java.io.File;

public class HTTPInputEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private HttpExchange exchange;

    private HttpServer httpServer;

    private File file;

    public HTTPInputEvent(HttpServer httpServer, HttpExchange exchange, File file) {
        this.exchange = exchange;
        this.httpServer = httpServer;
        this.file = file;
    }

    public File getFile() {
        return file;
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
