package rox.main.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public interface TSCommandExecutor {

    boolean command(TS3Api api, TextMessageEvent e);
}
