package rox.main.server;

import rox.main.Main;
import rox.main.event.events.MainServerClientConnectingEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.UUID;

public class ClientAcceptHandler extends Thread {

    /**
     * This thread wait until a client is connecting to the server. It will then check if user exist and create all
     * information and save them to a object array
     */

    @Override
    public void run() {
        try {

            /*
             *   Client Object
             *   [0] username
             *   [1] socket
             *   [2] ClientInputHandlerThread
             *   [3] BufferedReader
             *   [4] PrintWriter
             *   [5] Rank
             *   [6] UUID
             *
             *
             *
             */
            long startTime = System.currentTimeMillis();
            Socket socket;
            while ((socket = Main.getMainServer().getServerSocket().accept()) != null) { // while loop: wait until a connection is incoming
                Main.getLogger().time("MSWaited", startTime);
                Main.getMainServer().setWaitingConnection(false); // Just a asked if the thread is currently compute a incoming connection

                // <name>§<password>

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")), true);

                String[] input = reader.readLine().substring(1).split("§"); // split incoming message to array with §. Connectionstring with informations

                if (Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE username='" + input[0] + "' AND password='" + input[1] + "'") != null) {
                    // if the username is registered in the database

                    if (Main.getMainServer().getStaticManager().isBanned(Main.getMainServer().getStaticManager().getUUID(input[0]))) {
                        // return if user is banned
                        writer.println("§BANNED");
                        return;
                    }

                    Object[] objects = new Object[8]; // new object array to save the information
                    Thread thread; // the input thread for the client
                    objects[0] = input[0]; // username
                    objects[1] = socket; // socket
                    objects[3] = reader; // reader to avoid to create every time a new (new (new (new ()))).. shit
                    objects[4] = writer; // same here for the writer
                    objects[6] = Main.getMainServer().getStaticManager().getUUID(input[0]); // uuid
                    objects[5] = Main.getMainServer().getPermissionManager().getRankDatabase((UUID) objects[6]); // the rank of the user
                    (thread = new ClientInputHandler(objects)).start(); // starting input thread
                    objects[2] = thread; //thread

                    MainServerClientConnectingEvent event = new MainServerClientConnectingEvent(objects);
                    Main.getEventManager().callEvent(event);
                    if (event.isCancelled()) {
                        writer.println("§CONNECTION_CANCELLED");
                        return;
                    }

                    Main.getMainServer().getClients().put(Main.getMainServer().getStaticManager().getUUID(input[0]), objects); // saving user to a async hashMap
                    writer.println("§SERVER_CONNECTED"); // writing user he is connected
                } else {
                    writer.println("§SERVER_WRONG_LOGIN"); // if user is not found in database
                    socket.close();
                }
                Main.getMainServer().setWaitingConnection(true); // server is again waiting for new connections
                startTime = System.currentTimeMillis();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
