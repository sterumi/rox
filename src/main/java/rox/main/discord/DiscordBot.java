package rox.main.discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;
import org.json.simple.JSONObject;
import rox.main.Main;
import rox.main.discord.commands.*;
import rox.main.discord.listener.MessageReceivedListener;
import rox.main.discord.listener.UserJoinListener;
import rox.main.event.events.DiscordStartingEvent;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DiscordBot {


    /*
    * This is the discord bot for the ROX discord.
    *
    * Token for connection added as start parameter: discordtoken=<token>
    *
     */

    private String token;

    private boolean connected = false;

    private JDA jda;

    private DiscordCommandLoader discordCommandLoader;

    private NetworkUpdater networkUpdater;

    private Thread networkUpdaterThread;

    private ConcurrentHashMap<String, Object> information = new ConcurrentHashMap<>();

    public DiscordBot(String token){ this.token = token; }

    public void connect(){

        DiscordStartingEvent event = new DiscordStartingEvent(jda);
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        try {

            if (!Main.getDatabase().isConnected()) {
                Main.getLogger().err("DiscordBot", "Could not start DiscordBot.");
                return;
            }

            jda = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
            discordCommandLoader = new DiscordCommandLoader();
            setConnected(true);
            loadEvents();
            loadCommands();

            (networkUpdaterThread = new Thread((networkUpdater = new NetworkUpdater(this)))).start();

            new Thread(() ->{
                while(true){
                    try {
                        int r = new Random().nextInt(20) + 1;
                        String game;
                        switch(r){
                            case 1:
                                game = "mit seinen Einstellungen herum.";
                                break;
                            case 2:
                                game = "mit einen Ball.";
                                break;
                            case 3:
                                game = "alleine.";
                                break;
                            case 4:
                                game = "mit Bleikind.";
                                break;
                            case 5:
                                game = "verstecken.";
                                break;
                            case 6:
                                game = "Schach.";
                                break;
                            case 7:
                                game = "nichts interessantes";
                                break;
                            default:
                                game = "nichts.";
                                break;
                        }
                        setGame(game);

                        Thread.sleep(1000 * 180);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            Main.getLogger().log("DiscordBot", "Started.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void disconnect(){
        if(jda != null){
            jda.shutdown();
            jda = null;
            setConnected(false);
        }

    }

    public boolean isConnected(){ return connected; }

    private void setConnected(boolean bool) {
        connected = bool;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setGame(String game){

        if(game.isEmpty()){
            jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, ""));
        }else{

            if(game.length() > 128){
                return;
            }

            jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, game));
        }
    }

    public JDA getJDA(){
        return jda;
    }

    public TextChannel getCommandChannel() {
        return jda.getTextChannelById(348773038889893891L);
    }

    private void loadEvents(){
        jda.addEventListener(new MessageReceivedListener());
        jda.addEventListener(new UserJoinListener());
    }

    private void loadCommands() {
        discordCommandLoader.addCommand("!getid", new GetIDCommand());
        discordCommandLoader.addCommand("!info", new InfoCommand());
        discordCommandLoader.addCommand("!mp", new MediaPlayerCommand());
        discordCommandLoader.addCommand("!say", new SayCommand());
    }

    public DiscordCommandLoader getCommandLoader() {
        return discordCommandLoader;
    }

    public String toJSONString(){
        JSONObject object = new JSONObject();
        information.forEach(object::put);
        return object.toJSONString();
    }

    public ConcurrentHashMap<String, Object> getInformation() {
        return information;
    }

    public void setInformation(ConcurrentHashMap<String, Object> information) {
        this.information = information;
    }

    public String getToken() {
        return token;
    }
}
