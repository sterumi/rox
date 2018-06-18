package rox.main.event;

public interface EExecutor {

    void execute(Listener listener, Event event) throws Exception;

}
