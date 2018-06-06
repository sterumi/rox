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
        try {

            String input;
            while ((input = ((BufferedReader) objects[3]).readLine()) != null) {
                String[] args = input.split("[§ ]+");

                if (input.startsWith("§")) {
                    Main.getMainServer().getServerCommandLoader().getCommand(args[0]).command(objects, args[0], args);
                } else {
                    ((PrintWriter) objects[4]).println("§INVALID_COMMAND_STRUCTURE");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
