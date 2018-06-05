package rox.main.discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;
import rox.main.Main;
import rox.main.discord.commands.*;
import rox.main.discord.listener.MessageReceivedListener;
import rox.main.discord.listener.UserJoinListener;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

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

    public DiscordBot(String token){ this.token = token; }

    public void connect(){
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
            discordCommandLoader = new DiscordCommandLoader();
            setConnected(true);
            loadEvents();
            loadCommands();
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

    void setConnected(boolean bool){ connected = bool; }

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
        discordCommandLoader.addCommand("!ban", new BanCommand());
        discordCommandLoader.addCommand("!getid", new GetIDCommand());
        discordCommandLoader.addCommand("!info", new InfoCommand());
        discordCommandLoader.addCommand("!mp", new MediaPlayerCommand());
        discordCommandLoader.addCommand("!say", new SayCommand());
        discordCommandLoader.addCommand("!warn", new WarnCommand());
    }

    public DiscordCommandLoader getCommandLoader() {
        return discordCommandLoader;
    }

    public void addPoint(Guild guild, String userid) {

        Main.getMainServer().getDatabase().Update("UPDATE users SET points = points+1 WHERE discord_user_id='" + userid + "'");

        try {
            if (Main.getMainServer().getDatabase().Query("SELECT points FROM users WHERE username='" + userid + "'").getInt("points") == 3) {
                ban(guild, userid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ban(Guild guild, String userid) {
        UUID uuid = UUID.randomUUID();
        Main.getMainServer().getDatabase().Update("UPDATE users SET bandate = " + LocalDateTime.now().toString() + " WHERE discord_user_id = '" + userid + "'");
        Main.getMainServer().getDatabase().Update("UPDATE users SET ban_uuid = " + uuid.toString() + " WHERE discord_user_id = '" + userid + "'");
        AuditableRestAction auditableRestAction = guild.getController().ban(userid, -1, "Du hast die maximale Anzahl an Verwarnungen bekommen. Du kannst auch auf der Webseite einen Entbannungsantrag stellen. Deine UUID: " + uuid.toString());
        auditableRestAction.complete();
    }
}
