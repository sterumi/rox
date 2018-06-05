package rox.main.command;

import rox.main.MainCommandExecutor;

public class ExitCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        System.exit(0);
    }
}
