package rox.main.server;

public interface ServerCommandExecutor {

    /**
     * Interface for the command system
     *
     * @param user    All user objects (socket, inputThread, PrintWriter, UUID,..)
     * @param command The command where is given
     * @param args    All arguments
     * @return A bool if success to complete the function
     */

    boolean command(Client user, String command, String[] args);

}
