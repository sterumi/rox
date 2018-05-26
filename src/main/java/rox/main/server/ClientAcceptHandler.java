package rox.main.server;

import rox.main.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientAcceptHandler extends Thread {

    @Override
    public void run() {
        try {
            Socket socket;
            while ((socket = Main.getMainServer().getServerSocket().accept()) != null) {
                Main.getMainServer().setWaitingConnection(false);

                // <name>§<password>

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                String[] input = reader.readLine().split("§");

                if (Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE username='" + input[0] + "' AND password='" + input[1] + "'") != null) {
                    Object[] objects = new Object[8];
                    objects[0] = input[0];
                    objects[1] = socket;
                    Thread thread1 = new ClientInputHandler(objects);
                    objects[2] = thread1;
                    thread1.start();


                    Main.getMainServer().getClients().put(input[0], objects);
                    writer.println("§SERVER_CONNECTED");
                } else {
                    writer.println("§SERVER_WRONG_LOGIN");
                    socket.close();
                }
                Main.getMainServer().setWaitingConnection(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
