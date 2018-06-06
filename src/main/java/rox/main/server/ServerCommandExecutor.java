package rox.main.server;

public interface ServerCommandExecutor {

    boolean command(Object[] user, String command, String[] args);

}
