package rox.main.command;

import org.json.simple.JSONObject;
import rox.main.Main;
import rox.main.MainCommandExecutor;

public class HelpCommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if (args.length == 1) {
            Main.getMainCommandLoader().getRegisteredCommands().forEach(s ->
                    System.out.println(s + " - " + ((JSONObject)Main.getFileConfiguration().getCommandsValues().get("commands")).get(s)));
        } else {
            Main.getLogger().log("ROX", "Only 'help' Command.");
        }
    }
}
