package rox.main.pluginsystem;

import rox.main.FileConfiguration;
import rox.main.Main;
import rox.main.MainCommandExecutor;
import rox.main.discord.DiscordBot;
import rox.main.event.EventManager;
import rox.main.httpserver.HTTPServer;
import rox.main.logger.Logger;
import rox.main.minecraftserver.MinecraftServer;
import rox.main.server.MainServer;

public class Plugin {

    public static MainServer getMainServer() {
        return Main.getMainServer();
    }

    public static HTTPServer getHTTPServer() {
        return Main.getHttpServer();
    }

    public static DiscordBot getDiscordBot() {
        return Main.getDiscordBot();
    }

    public static MinecraftServer getMinecraftServer() {
        return Main.getMinecraftServer();
    }

    public static Object[] getInformatics() {
        return Main.getInformatics();
    }

    public static Thread getThread(int i) {
        return Main.getThread(i);
    }

    public void setThread(int i, Thread thread) {
        Main.setThread(i, thread);
    }

    public FileConfiguration getFileConfiguration() {
        return Main.getFileConfiguration();
    }

    public void setInformatics(int i, Object object) {
        Main.setInformatics(i, object);
    }

    public void addMainCommand(String command, MainCommandExecutor mainCommandExecutor) {
        Main.getMainCommandLoader().addCommand(command, mainCommandExecutor);
    }

    public static EventManager getEventManager() {
        return Main.getEventManager();
    }

    public Logger getLogger() {
        return Main.getLogger();
    }
}
