package rox.main.teamspeak.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import rox.main.teamspeak.TSCommandExecutor;

public class PingCommand implements TSCommandExecutor {
    @Override
    public boolean command(TS3Api api, TextMessageEvent e) {
        api.sendChannelMessage("Pong!");
        return true;
    }
}
