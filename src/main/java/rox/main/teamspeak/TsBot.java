package rox.main.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import org.json.simple.JSONObject;
import rox.main.Main;
import rox.main.event.events.TSCommandEvent;
import rox.main.event.events.TSStartedEvent;
import rox.main.event.events.TSStartingEvent;
import rox.main.util.BaseClient;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class TsBot implements BaseClient {

    private TS3Config config;
    private TS3Query query;
    private TS3Api api;

    private TSCommandLoader commandLoader;

    private int clientId;

    private String hostname, username, password;

    private NetworkUpdater networkUpdater;

    private Thread networkUpdaterThread;

    private ConcurrentHashMap<String, Object> information = new ConcurrentHashMap<>();

    private boolean active = false;

    public TsBot(String hostname, String username, String password){
        this.hostname = hostname;
        this.username = username;
        this.password = password;
    }

    public void connect(){

        TSStartingEvent event = new TSStartingEvent();
        Main.getEventManager().callEvent(event);
        if(event.isCancelled())return;

        commandLoader = new TSCommandLoader();

        config = new TS3Config();
        config.setHost(hostname);
        config.setEnableCommunicationsLogging(true);

        query = new TS3Query(config);
        query.connect();

        api = query.getApi();
        api.login(username, password);
        active = true;
        api.selectVirtualServerById(1);
        api.setNickname("ROXBot");
        clientId = api.whoAmI().getId();

        api.registerEvent(TS3EventType.TEXT_CHANNEL);

        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {

                TSCommandEvent event = new TSCommandEvent(e);
                Main.getEventManager().callEvent(event);
                if(event.isCancelled())return;

                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId)
                    if(e.getMessage().toLowerCase().startsWith("!")) commandLoader.getCommand(e.getMessage().toLowerCase().toLowerCase()).command(api,  e);
            }
        });
        Main.getLogger().log("TSBot", "Started TS Bot!");

        if((Boolean)Main.getFileConfiguration().getTsValues().get("autoRefreshNetwork")) (networkUpdaterThread = new Thread((networkUpdater = new NetworkUpdater(this)))).start();


        Main.getEventManager().callEvent(new TSStartedEvent(this));
    }

    public TSCommandLoader getCommandLoader() {
        return commandLoader;
    }

    public TS3Api getApi() {
        return api;
    }

    public int getClientId() {
        return clientId;
    }

    public TS3Config getConfig() {
        return config;
    }

    public TS3Query getQuery() {
        return query;
    }

    public boolean isActive(){
        return active;
    }

    public void disconnect() {
        query.exit();
        active = false;
    }

    public String getUsername() {
        return username;
    }

    public String getHostname() {
        return hostname;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }

    public ConcurrentHashMap<String, Object> getInformation() {
        return information;
    }

    public void setNetworkUpdater(NetworkUpdater networkUpdater) {
        this.networkUpdater = networkUpdater;
    }

    public void setNetworkUpdaterThread(Thread networkUpdaterThread) {
        this.networkUpdaterThread = networkUpdaterThread;
    }

    public void setInformation(ConcurrentHashMap<String, Object> information) {
        this.information = information;
    }

    public NetworkUpdater getNetworkUpdater() {
        return networkUpdater;
    }

    public Thread getNetworkUpdaterThread() {
        return networkUpdaterThread;
    }

    public String toJSONString(){
        JSONObject object = new JSONObject();
        information.forEach(object::put);
        return object.toJSONString();
    }

    public void setCommandLoader(TSCommandLoader commandLoader) {
        this.commandLoader = commandLoader;
    }
}
