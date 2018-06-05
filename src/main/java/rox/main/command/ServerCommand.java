package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class ServerCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        switch (args[2]) {
            case "start":
                if (args.length == 3) {
                    if (Main.getMainServer().isActive()) {
                        System.out.println("Server is already running.");
                    } else {
                        System.out.println("Starting Main Server...");
                        Main.getMainServer().start();
                    }
                } else {
                    System.out.println("server (start,stop)");
                }
                break;

            case "stop":
                if (args.length == 3) {
                    if (Main.getMainServer().isActive()) {
                        System.out.println("Main Server stopping...");
                        Main.getMainServer().stop();
                    } else {
                        System.out.println("Main server isn't running.");
                    }
                } else {
                    System.out.println("server (start,stop)");
                }
                break;

            default:
                System.out.println("server (start,stop)");
                break;
        }
    }
}
