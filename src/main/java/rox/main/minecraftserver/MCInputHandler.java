package rox.main.minecraftserver;

import rox.main.Main;
import rox.main.event.events.MinecraftServerInputEvent;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.UUID;

public class MCInputHandler extends Thread {

    private Object[] objects;

    public MCInputHandler(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public void run() {
        try {
            String input;
            while ((input = ((BufferedReader) objects[2]).readLine()) != null) {
                //String[] args = input.split("[§ ]+");
                String[] args = input.split("§");
                if (input.startsWith("§")) {

                    MinecraftServerInputEvent event = new MinecraftServerInputEvent(objects, input);
                    Main.getEventManager().callEvent(event);
                    if (event.isCancelled()) return;

                    Main.getMinecraftServer().getMCI().setInformation((UUID)objects[4], args[0], args[1]);

                } else {
                    ((PrintWriter) objects[3]).println("§INVALID_COMMAND_STRUCTURE");
                }
            }
        } catch (Exception e) {
            if (e instanceof SocketException) {
                Main.getLogger().log("MinecraftServer", objects[5] + " disconnected.");
                Main.getMinecraftServer().removeServer((UUID) objects[4]);
            } else {
                e.printStackTrace();
            }
        }
    }
}

                    /*switch (args[0]) {
                        case "info":
                            switch (args[1]) {
                                case "playerSize":
                                    Main.getMinecraftServer().getMCI().setInformation((UUID) objects[4], "playerSize", Integer.valueOf(args[2]));
                                    break;
                                case "maxPlayers":
                                    Main.getMinecraftServer().getMCI().setInformation((UUID) objects[4], "maxPlayers", Integer.valueOf(args[2]));
                                    break;
                                case "whitelistSize":
                                    Main.getMinecraftServer().getMCI().setInformation((UUID) objects[4], "whitelistSize", Integer.valueOf(args[2]));
                                    break;
                            }
                            break;
                    }*/
