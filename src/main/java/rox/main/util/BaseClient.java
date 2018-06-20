package rox.main.util;

import rox.main.exception.IllegalVariableChangeException;

import java.net.Socket;

public interface BaseClient {

    boolean connect();

    boolean disconnect();

    void setConnected(boolean bool);

    boolean isConnected();

    void setSocket(Socket socket) throws IllegalVariableChangeException;

    Socket getSocket();

}
