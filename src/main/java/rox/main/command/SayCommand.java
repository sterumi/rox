package rox.main.command;

import rox.main.MainCommandExecutor;

import java.util.Arrays;

public class SayCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length >= 1) {
            System.out.println(Arrays.toString(Arrays.copyOfRange(args, 1, args.length)));
        }
    }
}
