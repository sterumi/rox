package rox.main.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import rox.main.Main;
import rox.main.event.events.HTTPGetEvent;
import rox.main.server.permission.Rank;

import java.io.*;
import java.util.Arrays;
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
            switch (key) {
                case "gsUUID":
                    if(parameters.get("gsUUID") != null){
                        UUID uuid = UUID.fromString((String) parameters.get(key));
                        if (Main.getGameSystem().getConnections().containsKey(uuid)) {
                            try {
                                computeGameServer(response, parameters, uuid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            response.append(new JSONObject().toJSONString());
                        }
                    }else{
                        Main.getGameSystem().getConnections().keySet().forEach(uuid -> {
                            try {
                                computeGameServer(response, parameters, uuid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    break;

                case "ts":
                    response.append(Main.getTsBot().toJSONString());
                    break;

                case "ds":
                    response.append(Main.getDiscordBot().toJSONString());
                    break;

                case "info":
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("gsConnections", Main.getGameSystem().getConnections().size());
                    jsonObject.put("gsMaxConnections", Main.getGameSystem().getMaxConnections());
                    jsonObject.put("gsVersions", Main.getGameSystem().getVersions());
                    jsonObject.put("gsVersion", Main.getGameSystem().getVersion());
                    jsonObject.put("gsActive", Main.getGameSystem().isActive());
                    jsonObject.put("gsPort", Main.getGameSystem().getPort());
                    jsonObject.put("loggerConstruct", Main.getLogger().getConstruct());
                    jsonObject.put("msConnections", Main.getMainServer().getClients().size());
                    jsonObject.put("msRanks", Arrays.toString(Rank.values()));
                    jsonObject.put("msPort", Main.getMainServer().getPort());
                    jsonObject.put("msActive", Main.getMainServer().isActive());
                    jsonObject.put("msWaitingConnection", Main.getMainServer().isWaitingConnection());
                    jsonObject.put("dsActive", Main.getDiscordBot().isConnected());
                    jsonObject.put("dsInformation", Main.getDiscordBot().getInformation());
                    jsonObject.put("tsHostname", Main.getTsBot().getHostname());
                    jsonObject.put("tsClientID", Main.getTsBot().getClientId());
                    jsonObject.put("tsUsername", Main.getTsBot().getUsername());
                    jsonObject.put("tsInformation", Main.getTsBot().getInformation());
                    jsonObject.put("tsActive", Main.getTsBot().isActive());
                    jsonObject.put("httpPort", Main.getHttpServer().getPort());
                    jsonObject.put("httpAddress", Main.getHttpServer().getServer().getAddress());
                    jsonObject.put("news", Main.getNewsSystem().getNews());
                    jsonObject.put("loadedPlugins", Main.getPluginManager().getPlugins().keySet());
                    jsonObject.put("version", Main.getVersion());
                    response.append(jsonObject.toJSONString());
                    break;
            }
        });

        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    private void computeGameServer(StringBuilder response, Map<String, Object> parameters, UUID uuid) throws Exception{
        if (parameters.containsKey("style")) {
            if (((String) parameters.get("style")).equalsIgnoreCase("true")) {
                File customCss;
                if(parameters.containsKey("customStyle")){
                    if(parameters.get("customStyle") != null){
                        String fileName = (String)parameters.get("customStyle");
                        if(!fileName.endsWith(".css")) fileName += ".css";
                        customCss = new File("http/css", fileName);
                        if(!customCss.exists()){
                            response.append("<!DOCTYPE html>" +
                                    "<html><head><meta charset=\"UTF-8\"><style>" +
                                    "table {font-family: arial, sans-serif;    border-collapse: collapse;width: 100%;}" +
                                    " td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;}" +
                                    "tr:nth-child(even) {background-color: #dddddd;}" +
                                    "</style></head><body><p>Could not find custom css file.</p><table><tr><th>ServerName</th><th>Type</th><th>UUID</th><th>Version</th>");
                        }else{
                            StringBuilder cssContent = new StringBuilder();
                            new BufferedReader(new InputStreamReader(new FileInputStream(customCss))).lines().forEach(cssContent::append);
                            response.append("<html><head><meta charset=\"UTF-8\"><style>").append(cssContent.toString()).append("</style></head><table><tr><th>ServerName</th><th>Type</th><th>UUID</th><th>Version</th>");
                        }
                    }else{
                        response.append("<!DOCTYPE html>" +
                                "<html><head><meta charset=\"UTF-8\"><style>" +
                                "table {font-family: arial, sans-serif;    border-collapse: collapse;width: 100%;}" +
                                " td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;}" +
                                "tr:nth-child(even) {background-color: #dddddd;}" +
                                "</style></head><body><p>Could not find custom css file.</p><table><tr><th>ServerName</th><th>Type</th><th>UUID</th><th>Version</th>");

                    }
                }else{
                    response.append("<!DOCTYPE html>" +
                            "<html><head><meta charset=\"UTF-8\"><style>" +
                            "table {font-family: arial, sans-serif;    border-collapse: collapse;width: 100%;}" +
                            " td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;}" +
                            "tr:nth-child(even) {background-color: #dddddd;}" +
                            "</style></head><body><table><tr><th>ServerName</th><th>Type</th><th>UUID</th><th>Version</th>");
                }

                if(parameters.containsKey("advanced")){
                    if(((String)parameters.get("advanced")).equalsIgnoreCase("true")){
                        if (!Main.getGameSystem().getConnections().get(uuid).getInformation().isEmpty())
                            Main.getGameSystem().getConnections().get(uuid).getInformation().keySet().forEach(s ->
                                    response.append("<th>").append(s).append("</th>"));
                        response.append("</tr>");

                        Main.getGameSystem().getConnections().forEach((uid, o) ->
                                response.append("<tr><td>")
                                        .append(o.getName())
                                        .append("</td><td>")
                                        .append(o.getGameType().toString())
                                        .append("</td><td>")
                                        .append(uid)
                                        .append("</td><td>")
                                        .append(o.getVersion())
                                        .append("</td>"));

                        if (!Main.getGameSystem().getConnections().get(uuid).getInformation().isEmpty())
                            Main.getGameSystem().getConnections().get(uuid).getInformation().forEach((s, o) ->
                                    response.append("<td>").append(o).append("</td>"));

                        response.append("</tr></table></body></html>");
                    }else{
                        response.append("</tr>");

                        Main.getGameSystem().getConnections().forEach((uid, o) ->
                                response.append("<tr><td>")
                                        .append(o.getName())
                                        .append("</td><td>")
                                        .append(o.getGameType().toString())
                                        .append("</td><td>")
                                        .append(uid)
                                        .append("</td><td>")
                                        .append(o.getVersion())
                                        .append("</td>"));
                    }
                }else{
                    response.append("</tr>");

                    Main.getGameSystem().getConnections().forEach((uid, o) ->
                            response.append("<tr><td>")
                                    .append(o.getName())
                                    .append("</td><td>")
                                    .append(o.getGameType().toString())
                                    .append("</td><td>")
                                    .append(uid)
                                    .append("</td><td>")
                                    .append(o.getVersion())
                                    .append("</td>"));
                }

                response.append("</tr></table></body></html>");
            } else {
                response.append(Main.getGameSystem().getConnections().get(uuid).toJSONString());
            }
        } else {
            response.append(Main.getGameSystem().getConnections().get(uuid).toJSONString());
        }
    }
}
