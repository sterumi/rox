package rox.main.server.command;

import rox.main.Main;
import rox.main.server.Client;
import rox.main.server.ServerCommandExecutor;

import java.io.PrintWriter;

public class MsgCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Client user, String command, String[] args) {

        // §msg <user> <message>
        StringBuilder message = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            message.append(args[i]);
        }
        if (args.length >= 3) {

            if (Main.getMainServer().getClients().containsKey(Main.getMainServer().getStaticManager().getUUID(args[1]))) {
                Client target = Main.getMainServer().getClients().get(Main.getMainServer().getStaticManager().getUUID(args[1]));
                target.getWriter().println("§msg§" + user.getName() + "§" + message);
            } else {
                user.getWriter().println("§msg§USER_NOT_FOUND_OR_ONLINE");
            }
        } else {
            user.getWriter().println("§msg§LENGTH_TO_LOW");
        }

        return false;
    }
}
