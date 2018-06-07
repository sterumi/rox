package rox.main;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MainCommandLoader {

    private ConcurrentHashMap<String, MainCommandExecutor> classes = new ConcurrentHashMap<>();

    void addCommand(String name, MainCommandExecutor clazz) {
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
}
