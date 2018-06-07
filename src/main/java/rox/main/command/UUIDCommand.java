package rox.main.command;

import rox.main.MainCommandExecutor;

import java.util.UUID;

public class UUIDCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length == 1) {
            System.out.println(UUID.randomUUID());
        } else {
            System.out.println("Only 'uuid'.");
        }
    }
}
