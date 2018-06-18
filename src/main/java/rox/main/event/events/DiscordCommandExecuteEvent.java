package rox.main.event.events;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import rox.main.event.Event;
import rox.main.event.IHandler;

public class DiscordCommandExecuteEvent extends Event {

    private static IHandler list = new IHandler();

    private boolean cancel;

    private JDA jda;

    private Guild guild;

    private Member sender;

    private TextChannel textChannel;

    private String command;

    private String[] args;

    public DiscordCommandExecuteEvent(JDA jda, Guild guild, Member sender, TextChannel currentChannel, String name, String[] args) {
        this.jda = jda;
        this.guild = guild;
        this.sender = sender;
        this.textChannel = currentChannel;
        this.command = name;
        this.args = args;
    }

    public JDA getJda() {
        return jda;
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getSender() {
        return sender;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    @Override
    public IHandler getHandler() {
        return list;
    }

    public static IHandler getHandlerList() {
        return list;
    }

}
