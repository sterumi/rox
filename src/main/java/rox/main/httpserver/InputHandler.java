package rox.main.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class InputHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange e) {
        e.getResponseHeaders().add("Content-type", "text/html");

        File file;

        if (e.getRequestURI().getPath().equals("/")) {
            file = new File("http/", "index.html");
        } else {
            file = new File("http/", e.getRequestURI().getPath());
        }


        InputStream is = getClass().getResourceAsStream("/html/notFound.html");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        final String[] response = {""};

        try {
            if (file.exists()) {
                response[0] = usingBufferedReader(file.getCanonicalPath());
            } else {
                reader.lines().forEach(s -> response[0] += s);
            }

            e.sendResponseHeaders(200, response[0].length());
            OutputStream os = e.getResponseBody();
            os.write(response[0].getBytes());
            os.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private String usingBufferedReader(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filePath));


        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {
            contentBuilder.append(sCurrentLine).append("\n");
        }
        return contentBuilder.toString();

    }
}
