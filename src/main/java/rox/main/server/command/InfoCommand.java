package rox.main.server.command;

import rox.main.Main;
import rox.main.server.ServerCommandExecutor;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class InfoCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Object[] user, String command, String[] args) {

        // INFO (ping, onlineClients, registeredClients, onlineAdmins, bannedUsers, banDate) <user>

        if (args.length == 2) {
            switch (args[1]) {
                case "ping":

                    try {
                        //test not finito baguettito
                        InetAddress host = InetAddress.getByName("localhost");
                        long tm = System.nanoTime();
                        Socket socket = new Socket(host, 8089);
                        socket.close();
                        tm = (System.nanoTime() - tm) - 1000000L;
                        ((PrintWriter) user[4]).println("§ping§" + tm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "onlineClients":
                    ((PrintWriter) user[4]).println("§info§onlineClients§" + Main.getMainServer().getStaticManager().getOnlineUser());
                    break;
                case "registeredClients":
                    ((PrintWriter) user[4]).println("§info§registeredClients§" + Main.getMainServer().getStaticManager().getRegisteredUser());
                    break;
                case "onlineAdmins":
                    ((PrintWriter) user[4]).println("§info§onlineAdmins§" + Main.getMainServer().getStaticManager().getOnlineAdmins());
                    break;
                case "bannedUsers":
                    ((PrintWriter) user[4]).println("§info§bannedUsers§" + Main.getMainServer().getStaticManager().getBannedUsers());
                    break;
                default:
                    ((PrintWriter) user[4]).println("§info§COMMAND_NOT_FOUND");
                    break;

            }
        } else if (args.length == 3) {
            switch (args[1]) {
                case "banDate":
                    ((PrintWriter) user[4]).println("§info§banDate§" + Main.getMainServer().getStaticManager().getBanDate(Main.getMainServer().getStaticManager().getUUID(args[2])));
                    break;
                default:
                    ((PrintWriter) user[4]).println("§info§COMMAND_NOT_FOUND");
                    break;
            }
        } else {
            ((PrintWriter) user[4]).println("§info§LENGTH_TO_L_OR_H");
        }


        return false;
    }
}
