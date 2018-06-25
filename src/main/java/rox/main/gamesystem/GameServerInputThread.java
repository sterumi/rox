package rox.main.gamesystem;

import rox.main.Main;

import java.net.SocketException;

public class GameServerInputThread extends Thread {

    private DataService dataService;

    public GameServerInputThread(DataService dataService){
        this.dataService = dataService;
    }

    @Override
    public void run(){

        String input;
        try{
            while((input = dataService.getReader().readLine()) != null){
                // §<information>§<value>
                if(input.startsWith("§")) {
                    String[] content = input.split("§");
                    dataService.getInformation().put(content[1], content[2]);
                }else if(input.startsWith("?")){
                    // ?<information>
                    switch(input.substring(1)){
                        case "ping":
                            dataService.getWriter().println(a(input) + "Pong");
                            break;
                        case "onlineServers":
                            dataService.getWriter().println(a(input) + "TODO");
                            break;
                    }
                }
            }
        }catch (Exception e){
            if(e instanceof SocketException){
                Main.getLogger().log("GameServer", dataService.getName() + " disconnected!");
                Main.getGameSystem().disconnect(dataService.getUUID());
                return;
            }
            e.printStackTrace();
        }

    }

    private String a(String str){
        return "?" + str.substring(1) + "§";
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
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    public DataService getDataService() {
        return dataService;
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
    }

    @Override
    public void setContextClassLoader(ClassLoader cl) {
        super.setContextClassLoader(cl);
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}
