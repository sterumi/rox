package rox.main.teamspeak;

import rox.main.Main;
import rox.main.teamspeak.commands.PingCommand;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class TSCommandLoader {

    private ConcurrentHashMap<String, TSCommandExecutor> classes = new ConcurrentHashMap<>();

    public TSCommandLoader(){
        loadCommands();
    }

    public void loadCommands(){
        addCommand("!ping", new PingCommand());
    }

    /**
     * Add a new command to the teamSpeak system.
     *
     * @param name  The ts-command name.
     * @param clazz The class with {@link TSCommandExecutor} implementation
     */

    public void addCommand(String name, TSCommandExecutor clazz) {
        if (classes.containsKey(name)) {
            Main.getLogger().warn("TSCommandLoader", "The command '" + name + "' is already registered!");
            return;
        }
        classes.put(name, clazz);
    }

    /**
     * Get the command class.
     *
     * @param name The ts-command name.
     * @return The registered class with {@link TSCommandExecutor} implementation
     */

    public TSCommandExecutor getCommand(String name) {
        return classes.get(name);
    }

    /**
     * Get all registered commands.
     *
     * @return The entire registered commands
     */

    public ArrayList<String> getRegisteredCommands() {
        ArrayList<String> str = new ArrayList<>();
        classes.forEach((name, tsCommandExecutor) -> str.add(name));
        return str;
    }

}
