package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

import java.util.Arrays;

public class ExecuteCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length >= 2) {
            Main.getMainCommandLoader().getCommand(args[1]).command(args[1], Arrays.copyOfRange(args, 1, args.length));
        }
    }
}
