package rox.main.util;

import rox.main.exception.IllegalVariableChangeException;
import rox.main.server.database.MainDatabase;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface BaseServer {
    
    ConcurrentHashMap<UUID, Object[]> clients = new ConcurrentHashMap<>();
    
    boolean start();
    
    boolean stop();

    void setClients(ConcurrentHashMap<UUID, Object[]> clients) throws IllegalVariableChangeException;

    ConcurrentHashMap<UUID, Object[]> getClients();

    boolean isConnected();

    MainDatabase getDatabase();

    void setDatabase(MainDatabase mainDatabase) throws IllegalVariableChangeException;


    
}
