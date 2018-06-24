package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class GSCommand implements MainCommandExecutor {

    @Override
    public void command(String name, String[] args) {
        // gs (start,stop) <tokenCode>

        if (args.length == 1) {
            Main.getLogger().log("ROX", "gs (start,stop)");
            return;
        }

        switch (args[1]) {
            case "start":
                if (args.length == 2) {
                    if (Main.getGameSytem().isActive()) {
                        Main.getLogger().log("ROX", "Game System is already running!");
                    } else {
                        Main.getGameSytem().start();
                        Main.getLogger().log("ROX", "Started Game System.");
                    }
                } else {
                    Main.getLogger().log("ROX", "discord start");
                }

                break;

            case "stop":
                if (args.length == 2) {
                    if (Main.getGameSytem().isActive()) {
                        Main.getGameSytem().stop();
                        Main.getLogger().log("ROX", "Stopped Game System.");
                    } else {
                        Main.getLogger().log("ROX", "Game System isn't running.");
                    }
                } else {
                    Main.getLogger().log("ROX", "gs stop");
                }
                break;

        }
    }
}