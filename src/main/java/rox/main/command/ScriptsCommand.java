package rox.main.command;

import org.json.simple.JSONArray;
import rox.main.Main;
import rox.main.MainCommandExecutor;

public class ScriptsCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        switch (args.length) {
            case 1:
                Main.getLogger().log("ROX", "javascript, lua");
                break;
            case 2:

                switch (args[1]) {
                    case "reload":
                        ((JSONArray) Main.getFileConfiguration().getValue("scriptEngine")).parallelStream().forEach(o -> {
                            switch ((String) o) {
                                case "lua":
                                    Main.getLuaLoader().load();
                                    break;

                                case "javascript":
                                    Main.getJavaScriptEngine().init();
                                    break;
                            }
                        });
                        break;

                    default:
                        Main.getLogger().log("ROX", "scripts (reload, execute) <filename>");
                        break;

                }
                break;

            case 3:
                switch (args[1]){
                    case "execute":
                        Main.getLuaLoader().executeFile(args[3]);
                        Main.getJavaScriptEngine().executeFile(args[3]);
                        break;

                    default:
                        Main.getLogger().log("ROX", "scripts (reload, execute) <filename>");
                        break;
                }
                break;
            default:
                Main.getLogger().log("ROX", "scripts (reload, execute) <filename>");
                break;
        }
    }
}
