package rox.main.server;

import rox.main.Main;

import java.util.concurrent.ConcurrentHashMap;

class ServerCommandLoader {

    private ConcurrentHashMap<String, ServerCommandExecutor> classes = new ConcurrentHashMap<>();


    /**
     * Add new commands to the main server command system
     *
     * @param name  The command name
     * @param clazz The class to execute if this command is asked
     */
    public void addCommand(String name, ServerCommandExecutor clazz) {
        if (classes.containsKey(name)) {
            Main.getLogger().warn("MainServer", "The command '" + name + "' is already registered.");
            return;
        }
        classes.put(name, clazz);
    }

    public ServerCommandExecutor getCommand(String name) {
        return classes.get(name);
    }


}
