package rox.main.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.embed.swt.SWTFXUtils;
import rox.main.Main;
import rox.main.event.events.HTTPNewsEvent;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NewsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {

        // /news
        // /news?style=<boolean>
        // /news?style=<boolean>&customStyle=<fileName>

        // The customStyle path is "http/css"


        StringBuilder response = new StringBuilder();
        he.sendResponseHeaders(200, response.length());

        Map<String, Object> parameters = new HashMap<>();
        HTTPServer.parseQuery(he.getRequestURI().getQuery(), parameters);

        if(!parameters.containsKey("style")){
            response.append(Main.getNewsSystem().getAllNewsAsString());
        }else{
            if(((String)parameters.get("style")).equalsIgnoreCase("true")){
                File customCss;
                if(parameters.containsKey("customStyle")){
                    String fileName = (String)parameters.get("customStyle");
                    if(!fileName.endsWith(".css")) fileName += ".css";
                    customCss = new File("http/css", fileName);
                    if(!customCss.exists()){
                        response.append("<p>Could not find custom css file.</p>");
                    }else{
                        StringBuilder cssContent = new StringBuilder();
                        new BufferedReader(new InputStreamReader(new FileInputStream(customCss))).lines().forEach(cssContent::append);
                        response.append("<html><head><meta charset=\"UTF-8\"><style>" + cssContent.toString() + "</style></head>");
                    }
                }else{
                    response.append("<!DOCTYPE html>" +
                            "<html><head><meta charset=\"UTF-8\"><style>" +
                            "table {font-family: arial, sans-serif;    border-collapse: collapse;width: 100%;}" +
                            " td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;}" +
                            "tr:nth-child(even) {background-color: #dddddd;}" +
                            "</style></head>");
                }
                response.append("<body><table><tr><th>News</th><th>Author</th><th>Date</th></tr>");
                Main.getNewsSystem().getNews().forEach((integer, jsonArray) -> response.append("<tr><td>" + jsonArray.get(0) + "</td><td>" + jsonArray.get(1) + "</td><td>" + jsonArray.get(2) + "</td></tr>"));
                response.append("</table></body></html>");
            }
        }

        HTTPNewsEvent event = new HTTPNewsEvent(Main.getHttpServer().getServer(), he, response.toString());
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();

    }
}
