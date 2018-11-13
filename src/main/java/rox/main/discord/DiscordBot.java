package rox.main.discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
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
        discordCommandLoader.addCommand("#:", new EmoteCommand());
    }

    public DiscordCommandLoader getCommandLoader() {
        return discordCommandLoader;
    }

    public void addPoint(Guild guild, String userid) {

        Main.getDatabase().Update("UPDATE users SET points = points+1 WHERE discord_user_id='" + userid + "'");

        try {
            if (Main.getDatabase().Query("SELECT points FROM users WHERE username='" + userid + "'").getInt("points") == 3) {
                ban(guild, userid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String toJSONString(){
        JSONObject object = new JSONObject();
        information.forEach(object::put);
        return object.toJSONString();
    }

    public void ban(Guild guild, String userid) {
        UUID uuid = UUID.randomUUID();
        Main.getDatabase().Update("UPDATE users SET bandate = " + LocalDateTime.now().toString() + " WHERE discord_user_id = '" + userid + "'");
        Main.getDatabase().Update("UPDATE users SET ban_uuid = " + uuid.toString() + " WHERE discord_user_id = '" + userid + "'");
        AuditableRestAction auditableRestAction = guild.getController().ban(userid, -1, "Du hast die maximale Anzahl an Verwarnungen bekommen. Du kannst auch auf der Webseite einen Entbannungsantrag stellen. Deine UUID: " + uuid.toString());
        auditableRestAction.complete();
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
