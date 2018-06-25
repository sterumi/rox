package rox.main.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import rox.main.Main;
import rox.main.event.events.HTTPGetEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GetHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        HTTPGetEvent event = new HTTPGetEvent(Main.getHttpServer().getServer(), he);
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        Map<String, Object> parameters = new HashMap<>();
        HTTPServer.parseQuery(he.getRequestURI().getQuery(), parameters);

        StringBuilder response = new StringBuilder();

        parameters.keySet().forEach(key -> {
            switch (key){
                case "gsUUID":
                    UUID uuid = UUID.fromString((String)parameters.get(key));
                    if(Main.getGameSystem().getConnections().containsKey(uuid)){
                        response.append(Main.getGameSystem().getConnections().get(uuid).toJSONString());
                    }else{
                        response.append(new JSONObject().toJSONString());
                    }
                    break;
                case "ts":
                    response.append(Main.getTsBot().toJSONString());
                    break;
            }
        });

        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
