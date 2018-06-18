package rox.main.listener;

import rox.main.event.EventAnnotation;
import rox.main.event.Listener;
import rox.main.event.events.MainStartEvent;

public class MainStartListener implements Listener {

    @EventAnnotation
    public void onStart(MainStartEvent e) {
        System.out.println("OIDA DER STARTET LOOOL");
    }
}
