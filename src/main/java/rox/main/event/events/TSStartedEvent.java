package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;
import rox.main.teamspeak.TsBot;

public class TSStartedEvent extends Event {

    private static IHandler list = new IHandler();

    private TsBot tsBot;

    public TSStartedEvent(TsBot tsBot) {
        this.tsBot = tsBot;
    }

    @Override
    public IHandler getHandler() {
        return list;
    }

    public static IHandler getHandlerList() {
        return list;
    }

    public TsBot getTsBot() {
        return tsBot;
    }
}
