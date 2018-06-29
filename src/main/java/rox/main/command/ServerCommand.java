package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class ServerCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {

        // server (start, stop)

        // server rank (create, delete, update) <name> <permission>

        switch (args.length) {
            case 2:
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
                        } else Main.getLogger().log("ROX", "Main server isn't running.");


                        break;
                    case "rank":
                        Main.getLogger().log("ROX", "server rank (create, delete, update) <rankName> <permission>");
                        break;

                    default:
                        Main.getLogger().log("ROX", "server (start, stop, rank)");
                        break;
                }
                break;

            case 4:
                switch (args[2]) {
                    case "create":
                        if (Main.getMainServer().getPermissionManager().createRank(args[3]))
                            Main.getLogger().log("ROX", "Created rank: " + args[3]);
                        else Main.getLogger().warn("ROX", "Could not create rank: " + args[3]);

                        break;

                    case "delete":
                        if (Main.getMainServer().getPermissionManager().deleteRank(args[3]))
                            Main.getLogger().log("ROX", "Deleted rank: " + args[3]);
                        else Main.getLogger().warn("ROX", "Could not delete rank: " + args[3]);

                        break;

                    case "update":
                        Main.getLogger().log("ROX", "server rank update <rankName> <permission>");
                        break;
                    default:
                        Main.getLogger().log("ROX", "server (start, stop, rank) <rankName> <permission>");
                        break;
                }
                break;

            case 5:

                switch (args[2]) {
                    case "update":

                        if (Main.getMainServer().getPermissionManager().addPermission(args[3], args[4]))
                            Main.getLogger().log("ROX", "Added permission " + args[4] + " to rank " + args[3]);
                        else
                            Main.getLogger().warn("ROX", "Could not update permissions " + args[4] + " at rank " + args[3]);


                        break;
                    default:
                        Main.getLogger().log("ROX", "server (start, stop, rank) <rankName> <permission>");
                        break;
                }

                break;

            default:
                Main.getLogger().log("ROX", "server (start, stop, rank)");
                break;
        }

    }
}
