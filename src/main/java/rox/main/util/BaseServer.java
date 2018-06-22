package rox.main.util;

import rox.main.exception.IllegalVariableChangeException;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface BaseServer {
    
    ConcurrentHashMap<UUID, Object[]> clients = new ConcurrentHashMap<>();
    
    boolean start();
    
    boolean stop();

    void setClients(ConcurrentHashMap<UUID, Object[]> clients) throws IllegalVariableChangeException;

    ConcurrentHashMap<UUID, Object[]> getClients();

    boolean isConnected();

}
