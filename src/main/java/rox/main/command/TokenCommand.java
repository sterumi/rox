package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class TokenCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length == 3) {
            if (args[2] != null) {
                Main.setInformatics(1, args[2]);
                Main.getDiscordBot().setToken(args[2]);
                Main.getLogger().log("ROX", "Token is set.");
            } else {
                Main.getLogger().log("ROX", "You must set a token!");
            }
        } else {
            Main.getLogger().log("ROX", "discord (token,start,stop) <tokenCode>");
        }
    }
}
