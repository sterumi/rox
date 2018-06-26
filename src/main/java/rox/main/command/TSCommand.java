package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class TSCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length == 2) {
            switch (args[1]) {
                case "start":
                    if (Main.getTsBot().isActive()) {
                        Main.getLogger().log("ROX", "Bot is already running.");
                    } else {
                        Main.getLogger().log("ROX", "Starting TS Bot...");
                        Main.getTsBot().connect();
                    }
                    break;

                case "stop":
                    if (Main.getTsBot().isActive()) {
                        Main.getLogger().log("ROX", "TS Bot stopping...");
                        Main.getTsBot().disconnect();
                    } else {
                        Main.getLogger().log("ROX", "TS Bot isn't running.");
                    }
                    break;

                default:
                    Main.getLogger().log("ROX", "ts (start, stop)");
                    break;
            }
        } else {
            Main.getLogger().log("ROX", "ts (start, stop)");
        }
    }
}
