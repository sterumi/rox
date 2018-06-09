package rox.main.logger;

import rox.main.Main;

import java.time.LocalDateTime;

public class Logger {

    private String construct = "[TIME] [CLASS] [MESSAGE]";


    public void log(String name, String message) {
        System.out.println("[INFO]" + construct.replace("[TIME]", "[" + LocalDateTime.now() + "]")
                .replace("[CLASS]", "[" + name + "]").replace("[MESSAGE]", message));
    }

    public void warn(String name, String message) {
        System.out.println("[WARN]" + construct.replace("[TIME]", "[" + LocalDateTime.now() + "]")
                .replace("[CLASS]", "[" + name + "]").replace("[MESSAGE]", message));
    }

    public void err(String name, String message) {
        System.out.println("[ERROR]" + construct.replace("[TIME]", "[" + LocalDateTime.now() + "]")
                .replace("[CLASS]", "[" + name + "]").replace("[MESSAGE]", message));
    }

    public void debug(String name, String message) {
        if (Main.isDebug())
            System.out.println("[DEBUG]" + construct.replace("[TIME]", "[" + LocalDateTime.now() + "]")
                    .replace("[CLASS]", "[" + name + "]").replace("[MESSAGE]", message));
    }

}
