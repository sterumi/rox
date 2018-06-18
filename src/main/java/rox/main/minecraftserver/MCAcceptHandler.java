package rox.main.minecraftserver;

import rox.main.Main;
import rox.main.event.events.MinecraftServerAcceptEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class MCAcceptHandler extends Thread {

    @Override
    public void run() {

        try {

            /*
             *   ServerMap:
             *   [0] socket
             *   [1] mcInputHandlerThread
             *   [2] BufferedReader
             *   [3] PrintWriter
             *   [4] UUID
             *   [5] serverName
             *
             */


            Socket socket;
            while ((socket = Main.getMinecraftServer().getServerSocket().accept()) != null) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                //<uuid>§<password>

                String[] input = reader.readLine().split("§");

                MinecraftServerAcceptEvent event = new MinecraftServerAcceptEvent(socket, reader, writer, input);
                Main.getEventManager().callEvent(event);
                if (event.isCancelled()) return;

                try {
                    Main.getMainServer().getDatabase().Query("SELECT * FROM mc_servers WHERE uuid='" + input[0] + "'");
                } catch (NullPointerException e) {
                    writer.println("§UUID_NOT_FOUND");
                    return;
                }

                if (Main.getMainServer().getDatabase().Query("SELECT * FROM users WHERE username='" + input[0] + "' AND password='" + input[1] + "'") != null) {

                    Object[] objects = new Object[8];

                    Thread thread;
                    objects[0] = socket;
                    objects[2] = reader;
                    objects[3] = writer;
                    objects[4] = UUID.fromString(input[0]);
                    objects[5] = Main.getMinecraftServer().getMCI().getServerName((UUID) objects[4]);
                    (thread = new MCInputHandler(objects)).start();
                    objects[1] = thread;
                    Main.getMinecraftServer().addServer((UUID) objects[4], objects);
                    writer.println("§MC_SERVER_CONNECTED");
                    Main.getLogger().log("MinecraftServer", objects[5] + " connected: " + socket.getInetAddress());
                } else {
                    writer.println("§ACCOUNT_NOT_FOUND");
                    socket.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
