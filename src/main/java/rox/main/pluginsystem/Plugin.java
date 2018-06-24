package rox.main.pluginsystem;

import rox.main.FileConfiguration;
import rox.main.Main;
import rox.main.MainCommandExecutor;
import rox.main.database.Database;
import rox.main.discord.DiscordBot;
import rox.main.event.EventManager;
import rox.main.gamesystem.GameSystem;
import rox.main.httpserver.HTTPServer;
import rox.main.logger.Logger;
import rox.main.news.NewsSystem;
import rox.main.server.MainServer;
import rox.main.teamspeak.TsBot;
import rox.main.util.MathUtil;

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

    public static Object[] getInformatics() {
        return Main.getInformatics();
    }

    public static Thread getThread(int i) {
        return Main.getThread(i);
    }

    public static void setThread(int i, Thread thread) {
        Main.setThread(i, thread);
    }

    public static FileConfiguration getFileConfiguration() {
        return Main.getFileConfiguration();
    }

    public static void setInformatics(int i, Object object) {
        Main.setInformatics(i, object);
    }

    public static void addMainCommand(String command, MainCommandExecutor mainCommandExecutor) {
        Main.getMainCommandLoader().addCommand(command, mainCommandExecutor);
    }

    public static EventManager getEventManager() {
        return Main.getEventManager();
    }

    public static Logger getLogger() {
        return Main.getLogger();
    }

    public static TsBot getTSBot(){ return Main.getTsBot(); }

    public static NewsSystem getNewsSystem(){
        return Main.getNewsSystem();
    }

    public static Database getDatabase(){
        return Main.getDatabase();
    }

    public static MathUtil getMathUtil(){
        return Main.getMathUtil();
    }

    public static GameSystem getGameSystem(){
        return Main.getGameSytem();
    }

}
