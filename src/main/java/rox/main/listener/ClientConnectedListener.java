package rox.main.listener;

import rox.main.event.EventAnnotation;
import rox.main.event.Listener;
import rox.main.event.events.ClientConnectedEvent;

public class ClientConnectedListener implements Listener {

    @EventAnnotation
    public void onConnect(ClientConnectedEvent e) {
        System.out.println(e.getName() + " connected!");
    }

}
