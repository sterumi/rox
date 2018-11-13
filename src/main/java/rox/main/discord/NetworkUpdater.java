package rox.main.discord;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkUpdater extends Thread{

    private final Timer timer = new Timer();

    private final DiscordBot bot;

    public NetworkUpdater(DiscordBot bot) {
        this.bot = bot;
    }

    @Override
    public void run(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                bot.getInformation().put("usersJoined", bot.getJDA().getUsers().size());
                bot.getInformation().put("roles", bot.getJDA().getRoles().size());
                bot.getInformation().put("categories", bot.getJDA().getCategories().size());
                bot.getInformation().put("textChannels", bot.getJDA().getTextChannels().size());
                bot.getInformation().put("voiceChannels", bot.getJDA().getVoiceChannels().size());
                bot.getInformation().put("cloudFlareRays", bot.getJDA().getCloudflareRays().size());
                bot.getInformation().put("guilds", bot.getJDA().getGuilds().size());
                bot.getInformation().put("ping", bot.getJDA().getPing());
                bot.getInformation().put("selfID", bot.getJDA().getSelfUser().getId());
                bot.getInformation().put("selfCreationTime", bot.getJDA().getSelfUser().getCreationTime());
                ConcurrentHashMap<String, String> users = new ConcurrentHashMap<>();
                bot.getJDA().getUsers().parallelStream().forEach(user -> users.put(user.getName(), user.getId()));
                bot.getInformation().put("users", users);
            }
        }, 20, 20);
    }

    @Override
    public ClassLoader getContextClassLoader() {
        return super.getContextClassLoader();
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public State getState() {
        return super.getState();
    }

    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return super.getUncaughtExceptionHandler();
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    public DiscordBot getBot() {
        return bot;
    }
}
