package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class ServerCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {

        if (args.length == 2) {
            switch (args[1]) {
                case "start":
                    if (Main.getMainServer().isActive()) {
                        Main.getLogger().log("ROX", "Server is already running.");
                    } else {
                        Main.getLogger().log("ROX", "Starting Main Server...");
                        Main.getMainServer().start();
                    }
                    break;

                case "stop":
                    if (Main.getMainServer().isActive()) {
                        Main.getLogger().log("ROX", "Main Server stopping...");
                        Main.getMainServer().stop();
                    } else {
                        Main.getLogger().log("ROX", "Main server isn't running.");
                    }
                    break;

                default:
                    Main.getLogger().log("ROX", "server (start,stop)");
                    break;
            }
        } else {
            Main.getLogger().log("ROX", "server (start, stop)");
        }

    }
}
