package rox.main.discord;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.bot.*;
import net.dv8tion.jda.client.*;
import net.dv8tion.jda.core.entities.Role;
import rox.main.Main;

import java.util.ArrayList;
import java.util.List;

public class RankManager {

    public Role getHighestRank(){
        return Main.getDiscordBot().getJDA().getRoles().get(Main.getDiscordBot().getJDA().getRoles().size());
    }

    public Role getLowestRank(){
        return Main.getDiscordBot().getJDA().getRoles().get(0);
    }

    public List<Role> getAdmininstratorRoles(){
        List<Role> list = new ArrayList<>();
        Main.getDiscordBot().getJDA().getRoles().forEach(r -> {
            if(r.hasPermission(Permission.ADMINISTRATOR)){
                list.add(r);
            }
        });
        return list;
    }
}
