package rox.main.server.command;

import rox.main.Main;
import rox.main.server.ServerCommandExecutor;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class DisconnectCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Object[] user, String command, String[] args) {

        Socket socket = (Socket) user[1];
        try {
            ((Thread) user[2]).interrupt();
            Main.getMainServer().saveUser((UUID) user[6]);
            Main.getMainServer().getClients().remove((UUID) user[6]);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
