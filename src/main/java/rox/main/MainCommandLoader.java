package rox.main;

import rox.main.command.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class MainCommandLoader {

    private ConcurrentHashMap<String, MainCommandExecutor> classes = new ConcurrentHashMap<>();

    /**
     * Add a new command to the system.
     *
     * @param name  The main-command name.
     * @param clazz The class with {@link MainCommandExecutor} implementation
     */

    public void addCommand(String name, MainCommandExecutor clazz) {
        if (classes.containsKey(name)) {
            Main.getLogger().warn("CommandLoader", "The command '" + name + "' is already registered!");
            return;
        }
        classes.put(name, clazz);
    }

    /**
     * Get the command class.
     *
     * @param name The main-command name.
     * @return The registered class with {@link MainCommandExecutor} implementation
     */

    public MainCommandExecutor getCommand(String name) {
        return classes.get(name);
    }

    /**
     * Get all registered commands.
     *
     * @return The entire registered commands
     */

    public ArrayList<String> getRegisteredCommands() {
        ArrayList<String> str = new ArrayList<>();
        classes.forEach((name, serverCommandExecutor) -> str.add(name));
        return str;
    }

    /**
     * Load all commands.
     */

    void loadCommands() {
        this.addCommand("discord", new DiscordCommand());
        this.addCommand("exit", new ExitCommand());
        this.addCommand("say", new SayCommand());
        this.addCommand("server", new ServerCommand());
        this.addCommand("token", new TokenCommand());
        this.addCommand("help", new HelpCommand());
        this.addCommand("uuid", new UUIDCommand());
        this.addCommand("fds", new FastDebugStartCommand());
        this.addCommand("exe", new ExecuteCommand());
        this.addCommand("mcs", new MCSCommand());
        this.addCommand("mem", new MemoryCommand());
        this.addCommand("test", (name, args) -> System.out.println("test"));
    }

    /**
     * Is called in a new thread {@link Main#loadThreads()}
     * Scanning the console for commands and execute the commands.
     */

    void initCommandHandle() {
        String input;
        while ((input = new Scanner(System.in).nextLine()) != null) {
            try {
                this.getCommand(input.split(" ")[0]).command(input.split(" ")[0], input.split(" "));
            } catch (Exception e) {
                if (e instanceof NullPointerException) {
                    Main.getLogger().log("ROX", "Command not found.");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }
}
