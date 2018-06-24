package rox.main.discord;

import rox.main.Main;

import java.util.concurrent.ConcurrentHashMap;

public class DiscordCommandLoader {

    private ConcurrentHashMap<String, DiscordCommandExecutor> classes = new ConcurrentHashMap<>();

    public DiscordCommandLoader() {
    }


    public void addCommand(String name, DiscordCommandExecutor clazz) {
        if (classes.containsKey(name)) {
            Main.getLogger().log("Discord", "This command is already registered!");
            Main.getDiscordBot().getCommandChannel().sendMessage("Der Befehl " + name + " ist bereits registriert!").complete();
            Main.getLogger().debug("DiscordBot", "Added command: " + name);
            return;
        }

        classes.put(name, clazz);
    }

    public DiscordCommandExecutor getCommand(String name) {
        return classes.getOrDefault(name, (jda, guild, sender, currentChannel, name1, args)
                -> currentChannel.sendMessage("Dieser Befehl exsistiert nicht!").complete());
    }

    public ConcurrentHashMap<String, DiscordCommandExecutor> getClasses() {
        return classes;
    }

    public void setClasses(ConcurrentHashMap<String, DiscordCommandExecutor> classes) {
        this.classes = classes;
    }
}
