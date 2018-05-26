package rox.main.server;

import rox.main.Main;

import java.io.*;
import java.net.Socket;

public class ClientInputHandler extends Thread {

    private Object[] objects;

    public ClientInputHandler(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public void run() {
        Socket socket = (Socket) objects[1];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String input;
            while ((input = reader.readLine()) != null) {
                String[] args = input.split(" ");
                if (args[0].startsWith("§")) {
                    switch (args[0]) {
                        case "§DISCONNECT":
                            socket.close();
                            ((Thread) objects[2]).interrupt();
                            Main.getMainServer().getClients().remove(objects[0].toString());
                            break;
                        case "§INFO":
                            writer.println("MainServer§" + Main.getMainServer().getClients().size());
                            break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
