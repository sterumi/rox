package rox.main.discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.simple.JSONObject;
import rox.main.Main;
import rox.main.discord.commands.*;
import rox.main.discord.listener.MessageReceivedListener;
import rox.main.discord.listener.UserJoinListener;
import rox.main.event.events.DiscordStartingEvent;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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

    private RankManager rankManager = new RankManager();

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
            loadThreads();

            (networkUpdaterThread = new Thread((networkUpdater = new NetworkUpdater(this)))).start();

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

    public JDA getJDA(){
        return jda;
    }

    public TextChannel getCommandChannel() {
        return jda.getTextChannelById(449287216871505922L);
    }

    private void loadThreads(){
        new Thread(() ->{
            while(true){
                switchGame();
                try {
                    Thread.sleep(180000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void switchGame(){
        AtomicInteger count = new AtomicInteger();
        ((JSONObject) Main.getFileConfiguration().getDiscordValues().get("games")).forEach((s, o) -> {
            if (count.getAndIncrement() == new Random().nextInt(((JSONObject) Main.getFileConfiguration().getDiscordValues().get("games")).size())) {
                setGame(Game.GameType.valueOf((String) o), (String) s);
            }
        });
    }

    private void loadEvents(){
        jda.addEventListener(new MessageReceivedListener());
        jda.addEventListener(new UserJoinListener());
    }

    private void loadCommands() {
        discordCommandLoader.addCommand("#getid", new GetIDCommand());
        discordCommandLoader.addCommand("#info", new InfoCommand());
        discordCommandLoader.addCommand("#mp", new MediaPlayerCommand());
        discordCommandLoader.addCommand("#say", new SayCommand());
        discordCommandLoader.addCommand("#dice", new DiceCommand());
        discordCommandLoader.addCommand("#", new EmoteCommand());
        discordCommandLoader.addCommand("#switchgame", new SwitchCommand());
    }

    public void setGame(Game.GameType game, String text){
        jda.getPresence().setGame(Game.of(game, text));
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

    public RankManager getRankManager() {
        return rankManager;
    }
}
