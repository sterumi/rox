package rox.main.server.command;

import rox.main.Main;
import rox.main.server.ServerCommandExecutor;
import rox.main.server.permission.Rank;

import java.io.PrintWriter;
import java.util.UUID;

public class RankCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Object[] user, String command, String[] args) {

        // RANK§<user>§<rank>

        if (Main.getMainServer().getPermissionManager().hasRank((UUID) user[6], Rank.TEAM_MANAGER)) {
            if (args.length == 3) {
                try {
                    Rank rank = Rank.valueOf(args[2]);
                    if (((Rank) user[5]).getValue() >= rank.getValue()) {
                        Main.getMainServer().getPermissionManager().setRank(args[1], rank);
                        ((PrintWriter) user[4]).println("§rank§CHANGED_RANK_TARGET");
                    } else {
                        ((PrintWriter) user[4]).println("§rank§RANK_HIGHER_THEN_SELF");
                    }
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
