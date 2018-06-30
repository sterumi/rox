package rox.main.server;

import rox.main.Main;
import rox.main.event.events.MainServerCommandExecuteEvent;

import java.io.*;

public class ClientInputHandler extends Thread {

    private final Client client;

    ClientInputHandler(Client client) {
        this.client = client;
    }

    /**
     * Waiting if the specific client is sending a message and execute the command if this command exist.
     */

    @Override
    public void run() {
        try {
            String input;
            while ((input = client.getReader().readLine()) != null) { // While loop: execute code below if something is incoming from the user

                long startTime = System.currentTimeMillis(); // to calculate the time
                String[] args = input.substring(1).split("[§ ]+"); // splitting message to string array

                if (input.startsWith("§")) { // if the message start with § then execute

                    MainServerCommandExecuteEvent event = new MainServerCommandExecuteEvent(client, args[0], args);
                    Main.getEventManager().callEvent(event);
                    if (event.isCancelled()) return;

                    Main.getMainServer().getServerCommandLoader().getCommand(args[0].toUpperCase()).command(client, args[0].toUpperCase(), args); // execute command if exist
                } else {
                    client.getWriter().println("§INVALID_COMMAND_STRUCTURE"); // sending if command doesn't start with §
                }
                Main.getLogger().time("MSInputCommand (" + args[0] + ")", startTime); // Print the time it took to execute
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public long getId() {
        return super.getId();
    }

    @Override
    public State getState() {
        return super.getState();
    }

    public Client getClient() {
        return client;
    }

    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return super.getUncaughtExceptionHandler();
    }

    @Override
    public void setContextClassLoader(ClassLoader cl) {
        super.setContextClassLoader(cl);
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
    }

}
