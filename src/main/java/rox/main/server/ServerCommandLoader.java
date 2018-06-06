package rox.main.server;

import java.util.concurrent.ConcurrentHashMap;

class ServerCommandLoader {

    private ConcurrentHashMap<String, ServerCommandExecutor> classes = new ConcurrentHashMap<>();

    void addCommand(String name, ServerCommandExecutor clazz) {
        if (classes.containsKey(name)) {
            System.out.println("This command is already registered!");
            return;
        }
        classes.put(name, clazz);
    }

    ServerCommandExecutor getCommand(String name) {
        return classes.get(name);
    }


}
