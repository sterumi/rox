package rox.main.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PostHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        URI requestedUri = he.getRequestURI();
        String query = requestedUri.getRawQuery();
        HTTPServer.parseQuery(query, parameters);

        StringBuilder response = new StringBuilder();
        for (String key : parameters.keySet())
            response.append(key).append(" = ").append(parameters.get(key)).append("\n");
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());
    }
}
