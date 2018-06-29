package rox.main.server.command;

import rox.main.Main;
import rox.main.server.ServerCommandExecutor;

import java.io.PrintWriter;
import java.util.UUID;

public class BanCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Object[] user, String command, String[] args) {

        // §ban§<user>
        if (args.length == 1) {
            if ((Main.getMainServer().getPermissionManager().hasPermission((String)user[5], "rox.commands.ban"))) {
                UUID targetUUID = Main.getMainServer().getStaticManager().getUUID(command.split("§")[1]);
                if (Main.getMainServer().getStaticManager().isUserOnline(targetUUID)) {
                    Main.getMainServer().getStaticManager().banUser(targetUUID);
                } else {
                    Main.getMainServer().getStaticManager().banOfflineUser(targetUUID);
                }
            } else {
                ((PrintWriter) user[4]).println("§ban§NO_PERM");
            }
        } else {
            ((PrintWriter) user[4]).println("§ban§ARGS_NOT_ALLOWED");
        }

        return false;
    }
}
