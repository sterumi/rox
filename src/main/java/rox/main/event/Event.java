package rox.main.event;

public abstract class Event {
    private String name;
    private final boolean async;

    public Event() {
        this(false);
    }

    public Event(boolean isAsync) {
        this.async = isAsync;
    }

    public String getEventName() {
        if (this.name == null) this.name = this.getClass().getSimpleName();
        return this.name;
    }

    public abstract IHandler getHandler();

    public final boolean isAsync() {
        return async;
    }

    public static enum Result {
        DENY, DEFAULT, ALLOW;

        Result() {
        }
    }

}
