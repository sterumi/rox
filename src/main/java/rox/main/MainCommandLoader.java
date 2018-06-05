package rox.main;

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

}
