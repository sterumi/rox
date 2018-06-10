package rox.main.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class GetHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        HTTPServer.parseQuery(query, parameters);

        StringBuilder response = new StringBuilder();
        for (String key : parameters.keySet())
            response.append(key).append(" = ").append(parameters.get(key)).append("\n");
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
