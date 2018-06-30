package rox.main.server.command;

import rox.main.Main;
import rox.main.server.Client;
import rox.main.server.ServerCommandExecutor;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class InfoCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Client user, String command, String[] args) {

        // INFO (ping, onlineClients, registeredClients, onlineAdmins, bannedUsers, banDate) <user>

        if (args.length == 2) {
            switch (args[1]) {
                case "ping":

                    try {
                        //test not finito baguettito
                        InetAddress host = InetAddress.getByName("localhost");
                        long tm = System.nanoTime();
                        Socket socket = new Socket(host, 8981);
                        socket.close();
                        tm = (System.nanoTime() - tm) - 1000000L;
                        user.getWriter().println("§ping§" + tm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "onlineClients":
                    user.getWriter().println("§info§onlineClients§" + Main.getMainServer().getStaticManager().getOnlineUser());
                    break;
                case "registeredClients":
                    user.getWriter().println("§info§registeredClients§" + Main.getMainServer().getStaticManager().getRegisteredUser());
                    break;
                case "onlineAdmins":
                    user.getWriter().println("§info§onlineAdmins§" + Main.getMainServer().getStaticManager().getOnlineAdmins());
                    break;
                case "bannedUsers":
                    user.getWriter().println("§info§bannedUsers§" + Main.getMainServer().getStaticManager().getBannedUsers());
                    break;
                default:
                    user.getWriter().println("§info§COMMAND_NOT_FOUND");
                    break;

            }
        } else if (args.length == 3) {
            switch (args[1]) {
                case "banDate":
                    user.getWriter().println("§info§banDate§" + Main.getMainServer().getStaticManager().getBanDate(Main.getMainServer().getStaticManager().getUUID(args[2])));
                    break;
                default:
                    user.getWriter().println("§info§COMMAND_NOT_FOUND");
                    break;
            }
        } else {
            user.getWriter().println("§info§LENGTH_TO_L_OR_H");
        }


        return false;
    }
}
