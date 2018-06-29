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
            if(!Main.getDatabase().Query("SELECT * FROM ranks WHERE rankName='" + e.getRank() + "'").next()){
                e.getPrintWriter().println("§RANK_NOT_FOUND");
                Main.getMainServer().getClients().get(e.getUUID())[5] = Main.getMainServer().getPermissionManager().getDefault();
                e.getPrintWriter().println("§UPDATED_RANK§" + Main.getMainServer().getPermissionManager().getDefault());
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }
}
