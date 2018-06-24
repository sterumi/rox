package rox.main.logger;

import rox.main.Main;
import rox.main.event.events.LoggerEvent;

import java.time.LocalDateTime;

public class Logger {

    private String construct = "[TIME] [CLASS] [MESSAGE]";


    public void log(String name, String message) {

        LoggerEvent event = new LoggerEvent(construct, "[INFO]", name, message);
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        if(name.startsWith("[") || name.endsWith("]"))name = name.substring(1, name.length() - 1);

        System.out.println("[INFO] " + construct.replace("[TIME]", "[" + LocalDateTime.now() + "]")
                .replace("[CLASS]", "[" + name + "]").replace("[MESSAGE]", message));
    }

    public void warn(String name, String message) {

        LoggerEvent event = new LoggerEvent(construct, "[INFO]", name, message);
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        if(name.startsWith("[") || name.endsWith("]"))name = name.substring(1, name.length() - 1);

        System.out.println("[WARN] " + construct.replace("[WARN]", "[" + LocalDateTime.now() + "]")
                .replace("[CLASS]", "[" + name + "]").replace("[MESSAGE]", message));
    }

    public void err(String name, String message) {

        LoggerEvent event = new LoggerEvent(construct, "[ERROR]", name, message);
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        if(name.startsWith("[") || name.endsWith("]"))name = name.substring(1, name.length() - 1);

        System.out.println("[ERROR] " + construct.replace("[TIME]", "[" + LocalDateTime.now() + "]")
                .replace("[CLASS]", "[" + name + "]").replace("[MESSAGE]", message));
    }

    public void debug(String name, String message) {

        LoggerEvent event = new LoggerEvent(construct, "[DEBUG]", name, message);
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        if (Main.isDebug()){
            if(name.startsWith("[") || name.endsWith("]"))name = name.substring(1, name.length() - 1);
            System.out.println("[DEBUG] " + construct.replace("[TIME]", "[" + LocalDateTime.now() + "]")
                    .replace("[CLASS]", "[" + name + "]").replace("[MESSAGE]", message));
        }
    }

    public void time(String name, long startTime) {

        if (Main.isDebug()){
            if(name.startsWith("[") || name.endsWith("]"))name = name.substring(1, name.length() - 1);
            System.out.println("[DEBUG] " + construct.replace("[TIME]", "[" + LocalDateTime.now() + "]")
                    .replace("[CLASS]", "[" + name + "]").replace("[MESSAGE]", "LoadTime: " + (System.currentTimeMillis() - startTime) + "ms"));
        }
    }

    public String getConstruct() {
        return construct;
    }

    public void setConstruct(String construct) {
        this.construct = construct;
    }
}
