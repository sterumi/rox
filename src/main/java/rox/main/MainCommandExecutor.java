package rox.main;

public interface MainCommandExecutor {


    /**
     * @param name The main-name of the command
     * @param args All arguments given to the command from executor
     */

    void command(String name, String[] args);

}
