package rox.main.discord;

public class DiscordBot {


    /*
    * This is the discord bot for the ROX discord.
    *
    * Token for connection added as start parameter: discordtoken=<token>
    *
     */

    private String token;

    private boolean connected = false;

    public DiscordBot(String token){ this.token = token; }

    public void connect(){}

    public void disconnect(){}

    public boolean isConnected(){ return connected; }

    public void setConnected(boolean bool){ connected = bool; }

}
