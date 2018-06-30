package rox.main.server.dummy;
/*
Created by Bleikind
*/

import rox.main.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

public class TestClient {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public void connect() {
        try {
            socket = new Socket((String)Main.getFileConfiguration().getDummyValues().get("hostname"), Math.toIntExact((Long)Main.getFileConfiguration().getDummyValues().get("port")));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")), true);
            writer.println(Main.getFileConfiguration().getDummyValues().get("username") + "§" + Main.getFileConfiguration().getDummyValues().get("password"));
            String response = reader.readLine();
            switch (response) {
                case "§SERVER_CONNECTED":
                    TestInputHandler testInputHandler = new TestInputHandler(socket, reader, writer);
                    new Thread(testInputHandler).start();
                    System.out.println("Dummy connected.");
                    break;

                case "§BANNED":
                    System.out.println("You are banned.");
                    socket.close();
                    break;

                case "§CONNECTION_CANCELLED":
                    System.out.println("Connection was cancelled.");
                    socket.close();
                    break;

                case "§SERVER_WRONG_LOGIN":
                    System.out.println("Wrong username or password.");
                    socket.close();
                    break;
                default:
                    System.out.println("Could not identify this response: " + response + ". Do you need an update?");
                    socket.close();
                    break;
            }

            write((String)Main.getFileConfiguration().getDummyValues().get("command"), (String)Main.getFileConfiguration().getDummyValues().get("key"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String command, String args){
        writer.println("§" + command + "§" + args);
    }

}
