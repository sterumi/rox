package rox.main.server.command;

import rox.main.Main;
import rox.main.server.Client;
import rox.main.server.ServerCommandExecutor;

import java.io.PrintWriter;
import java.util.UUID;

public class BanCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Client user, String command, String[] args) {

        // §ban§<user>
        if (args.length == 2) {
            if ((Main.getMainServer().getPermissionManager().hasPermission(user.getRank(), "rox.commands.ban"))) {
                UUID targetUUID = Main.getMainServer().getStaticManager().getUUID(args[1]);
                if (Main.getMainServer().getStaticManager().isUserOnline(targetUUID)) {
                    Main.getMainServer().getStaticManager().banUser(targetUUID);
                    return true;
                } else {
                    Main.getMainServer().getStaticManager().banOfflineUser(targetUUID);
                    return true;
                }
            } else {
                user.getWriter().println("§ban§NO_PERM");
            }
        } else {
            user.getWriter().println("§ban§ARGS_NOT_ALLOWED");
        }

        return false;
    }
}
