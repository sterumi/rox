package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;

public class MainCommandExecuteEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private String command;

    private String[] args;

    public MainCommandExecuteEvent(String command, String[] args) {
        this.command = command;
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    @Override
    public IHandler getHandler() {
        return list;
    }

    public static IHandler getHandlerList() {
        return list;
    }


}
