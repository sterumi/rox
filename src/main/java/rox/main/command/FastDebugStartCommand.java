package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class FastDebugStartCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {

        if (args.length == 1) {
            Main.setInformatics(1, Main.getFileConfiguration().getValue("discordToken"));
            Main.getDiscordBot().setToken((String) Main.getFileConfiguration().getValue("discordToken"));
            Main.getDiscordBot().connect();
        } else {
            System.out.println("Only 'fds'.");
        }

    }
}
