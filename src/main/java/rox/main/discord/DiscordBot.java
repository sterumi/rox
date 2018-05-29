package rox.main.discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import rox.main.discord.listener.MessageReceivedListener;
import rox.main.discord.listener.ReadyListener;

import java.util.List;
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

    public DiscordBot(String token){ this.token = token; }

    public void connect(){
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
            setConnected(true);
            loadEvents();
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

    private void loadEvents(){
        jda.addEventListener(new MessageReceivedListener());
    }
}
