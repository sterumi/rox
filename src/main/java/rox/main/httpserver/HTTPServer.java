package rox.main.httpserver;

import com.sun.net.httpserver.HttpServer;
import rox.main.Main;
import rox.main.event.events.HTTPParseQueryEvent;
import rox.main.event.events.HTTPStartingEvent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPServer {

    private int port;

    private HttpServer server;

    /*
     * Added root and get/post handler.
     * Currently not used but implemented.
     * gets/posts currently not saved.
     * Only groundwork.
     *
     * Used for news system
     *
     */

    public HTTPServer(int port) {
        this.port = port;
        start();
    }

    private void start() {
        HTTPStartingEvent event = new HTTPStartingEvent(server);
        Main.getEventManager().callEvent(event);
        try {
            if (event.isCancelled()) return;
            server = HttpServer.create(new InetSocketAddress(port), 0);
            Main.getLogger().log("HTTPServer", "server started at " + port);
            server.createContext("/", new InputHandler());
            server.createContext("/header", new HeaderHandler());
            server.createContext("/get", new GetHandler());
            server.createContext("/post", new PostHandler());
            server.createContext("/news", new NewsHandler());
            server.setExecutor(null);
            server.start();
            Main.getLogger().log("HTTP", "Started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpServer getServer() {
        return server;
    }

    static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

        HTTPParseQueryEvent event = new HTTPParseQueryEvent(query, parameters);
        Main.getEventManager().callEvent(event);

        if (event.isCancelled()) return;
        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

    public int getPort() {
        return port;
    }
}
