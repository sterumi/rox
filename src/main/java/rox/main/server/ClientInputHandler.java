package rox.main.server;

import rox.main.Main;
import rox.main.event.events.MainServerCommandExecuteEvent;

import java.io.*;

public class ClientInputHandler extends Thread {

    private Object[] objects;

    ClientInputHandler(Object[] objects) {
        this.objects = objects;
    }

    /**
     * Waiting if the specific client is sending a message and execute the command if this command exist.
     */

    @Override
    public void run() {
        try {
            String input;
            while ((input = ((BufferedReader) objects[3]).readLine()) != null) { // While loop: execute code below if something is incoming from the user
                long startTime = System.currentTimeMillis(); // to calculate the time
                String[] args = input.split("[§ ]+"); // splitting message to string array

                if (input.startsWith("§")) { // if the message start with § then execute

                    MainServerCommandExecuteEvent event = new MainServerCommandExecuteEvent(objects, args[0], args);
                    Main.getEventManager().callEvent(event);
                    if (event.isCancelled()) return;

                    Main.getMainServer().getServerCommandLoader().getCommand(args[0]).command(objects, args[0], args); // execute command if exist
                } else {
                    ((PrintWriter) objects[4]).println("§INVALID_COMMAND_STRUCTURE"); // sending if command doesn't start with §
                }
                Main.getLogger().time("MSInputCommand (" + args[0] + ")", startTime); // Print the time it took to execute
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
