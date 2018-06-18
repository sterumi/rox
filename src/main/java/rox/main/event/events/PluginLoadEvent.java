package rox.main.event.events;

import rox.main.event.Event;
import rox.main.event.IHandler;
import rox.main.pluginsystem.ROXPlugin;

public class PluginLoadEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private ROXPlugin roxPlugin;

    private String name;

    public PluginLoadEvent(ROXPlugin roxPlugin, String name) {
        this.roxPlugin = roxPlugin;
        this.name = name;
    }

    public ROXPlugin getRoxPlugin() {
        return roxPlugin;
    }

    public String getName() {
        return name;
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
