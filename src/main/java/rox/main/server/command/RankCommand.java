package rox.main.server.command;

import rox.main.Main;
import rox.main.server.ServerCommandExecutor;

import java.io.PrintWriter;
import java.util.UUID;

public class RankCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Object[] user, String command, String[] args) {

        // RANK§<user>§<rank>

        if ((Main.getMainServer().getPermissionManager().hasPermission((String) user[5], "rox.commands.rank"))) {
            if (args.length == 3) {
                try {
                    Main.getMainServer().getClients().forEach((uuid, objects) -> {
                        if (((String) objects[0]).equalsIgnoreCase(args[1]))
                            if (Main.getMainServer().getPermissionManager().setRank(uuid, args[2]))
                                ((PrintWriter) user[4]).println("§rank§CHANGED_RANK_TARGET");
                            else ((PrintWriter) user[4]).println("§rank§COULD_NOT_CHANGE");
                    });
                } catch (IllegalArgumentException e) {
                    ((PrintWriter) user[4]).println("§rank§RANK_NOT_EXIST");
                }
            } else {
                ((PrintWriter) user[4]).println("§rank§LENGTH_TO_L_OR_H");
            }
        } else {
            ((PrintWriter) user[4]).println("§rank§NO_PERM");
        }


        return true;
    }
}
