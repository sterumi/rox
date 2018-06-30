package rox.main.server.listener;
/*
Created by Bleikind
*/

import rox.main.Main;
import rox.main.event.EventAnnotation;
import rox.main.event.Listener;
import rox.main.event.events.MainServerClientConnectingEvent;

import java.sql.SQLException;

public class ClientJoinListener implements Listener {

    @EventAnnotation
    public void onJoin(MainServerClientConnectingEvent e){
        try {
            if(!Main.getDatabase().Query("SELECT * FROM ranks WHERE rankName='" + e.getClient().getRank() + "'").next()){
                e.getClient().getWriter().println("§RANK_NOT_FOUND");
                Main.getMainServer().getClients().get(e.getClient().getUUID()).setRank(Main.getMainServer().getPermissionManager().getDefault());
                e.getClient().getWriter().println("§UPDATED_RANK§" + Main.getMainServer().getPermissionManager().getDefault());
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }
}
