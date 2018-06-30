package rox.main.server.command;

import rox.main.Main;
import rox.main.server.Client;
import rox.main.server.ServerCommandExecutor;

import java.io.PrintWriter;
import java.util.UUID;

public class RankCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Client user, String command, String[] args) {

        // RANK§<user>§<rank>

        if ((Main.getMainServer().getPermissionManager().hasPermission(user.getRank(), "rox.commands.rank"))) {
            if (args.length == 3) {
                try {
                    Main.getMainServer().getClients().forEach((uuid, objects) -> {
                        if ((objects.getName().equalsIgnoreCase(args[1])))
                            if (Main.getMainServer().getPermissionManager().setRank(uuid, args[2]))
                                user.getWriter().println("§rank§CHANGED_RANK_TARGET");
                            else user.getWriter().println("§rank§COULD_NOT_CHANGE");
                    });
                } catch (IllegalArgumentException e) {
                    user.getWriter().println("§rank§RANK_NOT_EXIST");
                }
            } else {
                user.getWriter().println("§rank§LENGTH_TO_L_OR_H");
            }
        } else {
            user.getWriter().println("§rank§NO_PERM");
        }


        return true;
    }
}
