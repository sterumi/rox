package rox.main;

import rox.main.command.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class MainCommandLoader {

    private ConcurrentHashMap<String, MainCommandExecutor> classes = new ConcurrentHashMap<>();

    public void addCommand(String name, MainCommandExecutor clazz) {
        if (classes.containsKey(name)) {
            System.out.println("This command is already registered!");
            return;
        }
        classes.put(name, clazz);
    }

    public MainCommandExecutor getCommand(String name) {
        return classes.get(name);
    }

    public ArrayList<String> getRegisteredCommands() {
        ArrayList<String> str = new ArrayList<>();
        classes.forEach((name, serverCommandExecutor) -> str.add(name));
        return str;
    }

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

    void initCommandHandle() {
        String input;
        while ((input = new Scanner(System.in).nextLine()) != null) {
            try {
                this.getCommand(input.split(" ")[0]).command(input.split(" ")[0], input.split(" "));
            } catch (Exception e) {
                e.printStackTrace();
                Main.getLogger().log("ROX", "Command not found.");
            }
        }
    }
}
