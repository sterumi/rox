package rox.main.teamspeak;

import rox.main.Main;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkUpdater extends Thread {

    private final Timer timer = new Timer();

    private final TsBot tsBot;

    public NetworkUpdater(TsBot tsBot) {
        this.tsBot = tsBot;
    }

    @Override
    public void run() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                tsBot.getInformation().put("onlineClients", tsBot.getApi().getClients().size());
                tsBot.getInformation().put("clientID", tsBot.getClientId());
                tsBot.getInformation().put("botName", tsBot.getUsername());
                tsBot.getInformation().put("hostname", tsBot.getHostname());
                tsBot.getInformation().put("version", tsBot.getApi().getVersion());
                tsBot.getInformation().put("bans", tsBot.getApi().getBans().size());
                tsBot.getInformation().put("channels", tsBot.getApi().getChannels().size());
                tsBot.getInformation().put("complaints", tsBot.getApi().getComplaints().size());

                ConcurrentHashMap<String, Integer> channelIndex = new ConcurrentHashMap<>();
                tsBot.getApi().getChannels().parallelStream().forEach(channel -> channelIndex.put(channel.getName(), channel.getTotalClients()));
                tsBot.getInformation().put("channelsIndex", channelIndex);

                ConcurrentHashMap<Integer, String> channelMap = new ConcurrentHashMap<>();
                ConcurrentHashMap<Integer, String> clientMap = new ConcurrentHashMap<>();
                tsBot.getApi().getChannels().parallelStream().forEach(channel -> channelMap.put(channel.getId(), channel.getName()));
                tsBot.getApi().getClients().parallelStream().forEach(client -> clientMap.put(client.getChannelId(), client.getNickname()));
                tsBot.getInformation().put("channelIDs", channelMap);
                tsBot.getInformation().put("clientChannel", clientMap);
            }
        }, (Long) Main.getFileConfiguration().getTsValues().get("refreshDelay") * 1000, (Long) Main.getFileConfiguration().getTsValues().get("refreshDelay") * 1000);
    }

    public Timer getTimer() {
        return timer;
    }

    public TsBot getTsBot() {
        return tsBot;
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
    }

    @Override
    public void setContextClassLoader(ClassLoader cl) {
        super.setContextClassLoader(cl);
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return super.getUncaughtExceptionHandler();
    }

    @Override
    public State getState() {
        return super.getState();
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public ClassLoader getContextClassLoader() {
        return super.getContextClassLoader();
    }

    @Override
    public boolean isInterrupted() {
        return super.isInterrupted();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
