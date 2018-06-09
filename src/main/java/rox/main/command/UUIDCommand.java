package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

import java.util.UUID;

public class UUIDCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length == 1) {
            Main.getLogger().log("ROX", UUID.randomUUID().toString());
        } else {
            Main.getLogger().log("ROX", "Only 'uuid'.");
        }
    }
}
