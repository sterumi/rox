package rox.main.server.command;

import rox.main.Main;
import rox.main.server.Client;
import rox.main.server.ServerCommandExecutor;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class DisconnectCommand implements ServerCommandExecutor {
    @Override
    public boolean command(Client user, String command, String[] args) {

        Socket socket = user.getSocket();
        try {
            user.getInputThread().interrupt();
            Main.getMainServer().saveUser(user.getUUID());
            Main.getMainServer().getClients().remove(user.getUUID());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
