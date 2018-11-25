package rox.main.command;
/*
Created by Bleikind
*/

import rox.main.FileConfiguration;
import rox.main.Main;
import rox.main.MainCommandExecutor;

public class ReloadCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if(args.length == 1){
            Main.getLogger().log("Reload", "Reloading...");
            Main.setFileConfiguration(new FileConfiguration());

            if(Main.getMainServer().isActive()){
                Main.getMainServer().stop();
                Main.getMainServer().clear();
                Main.getMainServer().start();
            }

            if(Main.getGameSystem().isActive()){
                Main.getGameSystem().stop();
                Main.getGameSystem().clear();
                Main.getGameSystem().start();
            }

            if(Main.getTsBot().isActive()){
                Main.getTsBot().disconnect();
                Main.getTsBot().clear();
                Main.getTsBot().connect();
            }

            if(Main.getDiscordBot().isConnected()){
                Main.getDiscordBot().disconnect();
                Main.getDiscordBot().clear();
                Main.getDiscordBot().connect();
            }

            Main.getLogger().log("Reload", "Reloaded!");

        }
    }
}
