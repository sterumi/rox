package rox.main.httpserver;

import com.sun.net.httpserver.*;
import rox.main.Main;
import rox.main.event.events.HTTPInputEvent;

import java.io.*;

public class InputHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange e) {
        e.getResponseHeaders().add("Content-type", "text/html");
        File file;
        StringBuilder response = new StringBuilder();

        if (e.getRequestURI().getPath().equals("/")) {
            file = new File("http/", "index.html");
        } else {
            file = new File("http/", e.getRequestURI().getPath());
        }

        try {

            if(e.getRequestURI().getPath().endsWith("index.html")){
                if((Boolean) Main.getFileConfiguration().getValue("useDefaultIndex")){
                    new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/html/notFound.html"))).lines().forEach(response::append);
                    e.sendResponseHeaders(200, response.toString().length());
                    e.getResponseBody().write(response.toString().getBytes());
                    e.getResponseBody().close();
                    return;
                }
            }

            if (file.exists()) {
                response.append(usingBufferedReader(file.getCanonicalPath()));
            } else {
                new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/html/notFound.html"))).lines().forEach(response::append);
            }
            HTTPInputEvent event = new HTTPInputEvent(Main.getHttpServer().getServer(), e, file);
            Main.getEventManager().callEvent(event);
            if (event.isCancelled()) return;

            e.sendResponseHeaders(200, response.toString().length());
            e.getResponseBody().write(response.toString().getBytes());
            e.getResponseBody().close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private String usingBufferedReader(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        new BufferedReader(new InputStreamReader(new FileInputStream(filePath))).lines().forEach(s -> contentBuilder.append(s).append("\n"));
        return contentBuilder.toString();

    }
}
