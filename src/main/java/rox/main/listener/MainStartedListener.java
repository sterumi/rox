package rox.main.listener;

import rox.main.event.EventAnnotation;
import rox.main.event.Listener;
import rox.main.event.events.MainStartedEvent;

public class MainStartedListener implements Listener {

    @EventAnnotation
    public void onStart(MainStartedEvent e) {
    }
}
