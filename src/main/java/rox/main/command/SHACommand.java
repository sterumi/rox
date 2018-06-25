package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class SHACommand implements MainCommandExecutor {
    @Override
    public void command(String name, String[] args) {
        if(args.length == 2){
            Main.getLogger().log("ROX", Main.getMathUtil().computeSHA256(args[1]));
        }else{
            Main.getLogger().log("ROX", "sha <password>");
        }
    }
}
