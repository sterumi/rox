package rox.main.httpserver;
/*
Created by Bleikind
*/

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import rox.main.Main;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class WebHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {

        Map<String, Object> parameters = new HashMap<>();
        HTTPServer.parseQuery(he.getRequestURI().getQuery(), parameters);
        StringBuilder response = new StringBuilder();

        parameters.forEach((key, value) -> {
            switch (key){
                case "overview-pc":
                    JSONObject object = new JSONObject();
                    object.put("cpuLoad", Main.getMathUtil().getCpuLoad());
                    object.put("ramTotal", Main.getMathUtil().getTotalRAMSize());
                    object.put("ramUsed", Main.getMathUtil().getUsedRAMSize());
                    response.append(object.toJSONString());
                    break;
            }
        });

        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
