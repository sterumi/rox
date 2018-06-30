package rox.main.server;

import rox.main.Main;
import rox.main.event.events.MainServerClientConnectingEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
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

                String[] input = reader.readLine().split("§"); // split incoming message to array with §. Connectionstring with informations
                if (Main.getDatabase().Query("SELECT * FROM users WHERE username='" + input[0] + "' AND password='" + input[1] + "'") != null) {
                    // if the username is registered in the database

                    if (Main.getMainServer().getStaticManager().isBanned(Main.getMainServer().getStaticManager().getUUID(input[0]))) {
                        // return if user is banned
                        writer.println("§BANNED");
                        return;
                    }
                    UUID uuid = Main.getMainServer().getStaticManager().getUUID(input[0]);
                    Thread thread; // the input thread for the client
                    Client client = new Client(input[0], socket, reader, writer, uuid, Main.getMainServer().getPermissionManager().getRankDatabase(uuid));
                    client.setInputThread(new ClientInputHandler(client));
                    (thread = new ClientInputHandler(client)).start(); // starting input thread

                    MainServerClientConnectingEvent event = new MainServerClientConnectingEvent(client);
                    Main.getEventManager().callEvent(event);
                    if (event.isCancelled()) {
                        writer.println("§CONNECTION_CANCELLED");
                        return;
                    }

                    Main.getMainServer().getClients().put(Main.getMainServer().getStaticManager().getUUID(input[0]), client); // saving user to a async hashMap
                    writer.println("§SERVER_CONNECTED"); // writing user he is connected
                } else {
                    writer.println("§SERVER_WRONG_LOGIN"); // if user is not found in database
                    socket.close();
                }
                Main.getMainServer().setWaitingConnection(true); // server is again waiting for new connections
                startTime = System.currentTimeMillis();


            }
        } catch (Exception e) {
            if(e instanceof SocketException){
                Main.getLogger().warn("MainServer", "Interrupted the accepting for clients.");
                return;
            }
            e.printStackTrace();
        }

    }

    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return super.getUncaughtExceptionHandler();
    }

    @Override
    public State getState() {
        return super.getState();
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public ClassLoader getContextClassLoader() {
        return super.getContextClassLoader();
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
    }

    @Override
    public void setContextClassLoader(ClassLoader cl) {
        super.setContextClassLoader(cl);
    }
}
