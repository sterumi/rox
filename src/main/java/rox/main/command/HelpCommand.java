package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class HelpCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length == 1) {
            Main.getMainCommandLoader().getRegisteredCommands().forEach(System.out::println);
        } else {
            System.out.println("Only help Command.");
        }
    }
}
