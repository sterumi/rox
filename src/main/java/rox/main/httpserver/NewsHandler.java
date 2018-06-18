package rox.main.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import rox.main.Main;
import rox.main.event.events.HTTPNewsEvent;

import java.io.IOException;
import java.io.OutputStream;

public class NewsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        StringBuilder response = new StringBuilder();
        response.append(Main.getNewsSystem().getAllNewsAsString());
        he.sendResponseHeaders(200, response.length());

        HTTPNewsEvent event = new HTTPNewsEvent(Main.getHttpServer().getServer(), he, response.toString());
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();

    }
}
