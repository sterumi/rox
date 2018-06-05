package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class SayCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        Main.setInformations(1, "TOKEN");
        Main.getDiscordBot().setToken("TOKEN");
        Main.getDiscordBot().connect();
    }
}
