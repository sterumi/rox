package rox.main.server.command;

import rox.main.Main;
import rox.main.server.ServerCommandExecutor;
import rox.main.server.permission.Rank;

import java.io.PrintWriter;
import java.util.UUID;

public class BanCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Object[] user, String command, String[] args) {

        // §ban§<user>
        if (args.length == 1) {
            if ((Main.getMainServer().getPermissionManager().hasRank((UUID) user[6], Rank.MAIN_SUPPORTER))) {
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
