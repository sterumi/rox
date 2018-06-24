package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class FastDebugStartCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {

        if (args.length == 1) {
            Main.getMainServer().start();
            Main.getGameSystem().start();
            Main.setInformatics(1, Main.getFileConfiguration().getValue("discordToken"));
            Main.getDiscordBot().setToken((String) Main.getFileConfiguration().getValue("discordToken"));
            Main.getDiscordBot().connect();
        } else {
            Main.getLogger().log("ROX", "Only 'fds'.");
        }

    }
}
