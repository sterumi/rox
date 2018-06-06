package rox.main.server.command;

import rox.main.Main;
import rox.main.server.ServerCommandExecutor;

import java.io.PrintWriter;

public class MsgCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Object[] user, String command, String[] args) {

        // §msg <user> <message>
        StringBuilder message = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            message.append(args[i]);
        }
        if (args.length >= 3) {

            if (Main.getMainServer().getClients().containsKey(Main.getMainServer().getStaticManager().getUUID(args[1]))) {
                Object[] target = Main.getMainServer().getClients().get(Main.getMainServer().getStaticManager().getUUID(args[1]));
                ((PrintWriter) target[4]).println("§msg§" + user[1] + "§" + message);
            } else {
                ((PrintWriter) user[4]).println("§msg§USER_NOT_FOUND_OR_ONLINE");
            }
        } else {
            ((PrintWriter) user[4]).println("§msg§LENGTH_TO_LOW");
        }

        return false;
    }
}
