package rox.main.event;

public class RegisteredListener {
    private final Listener listener;
    private final EExecutor executor;
    private final EventManager main;
    private final EPriority priority;

    public RegisteredListener(Listener listener, EExecutor executor, EventManager main, EPriority priority) {
        this.listener = listener;
        this.executor = executor;
        this.main = main;
        this.priority = priority;
    }

    public Listener getListener() {
        return listener;
    }

    public EPriority getPriority() {
        return priority;
    }

    public EventManager getMain() {
        return main;
    }

    public void call(Event event) throws Exception {
        this.executor.execute(this.listener, event);
    }

}
