package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class TokenCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length == 3) {
            if (args[2] != null) {
                Main.setInformations(1, args[2]);
                Main.getDiscordBot().setToken(args[2]);
                System.out.println("Token is set.");
            } else {
                System.out.println("You must set a token!");
            }
        } else {
            System.out.println("discord (token,start,stop) <tokenCode>");
        }
    }
}
