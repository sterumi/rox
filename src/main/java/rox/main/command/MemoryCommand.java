package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class MemoryCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length == 1) {
            long usedMemory = Runtime.getRuntime().totalMemory() / 1000000;
            long maxMemory = Runtime.getRuntime().maxMemory() / 1000000;

            Main.getLogger().log("MemoryInfo", "Max Memory (VM): " + maxMemory + "MB");
            Main.getLogger().log("MemoryInfo", "Used Memory: " + usedMemory + "MB");
            Main.getLogger().log("MemoryInfo", "Free Memory: " + (maxMemory - usedMemory) + "MB");
        }else{
            Main.getLogger().log("MemoryInfo", "Only 'mem'.");
        }
    }
}
