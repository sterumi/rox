package rox.main.command;

import rox.main.Main;
import rox.main.MainCommandExecutor;

public class NewsCommand implements MainCommandExecutor {

    // /news (add,remove,link) <author> <message>

    @Override
    public void command(String name, String[] args) {
        switch(args.length){
            case 2:
                if(args[1].equalsIgnoreCase("link")){
                    Main.getLogger().log("News", "bleibot.bleikind.de/news\nParameter: ?style=<bool>&customcss=<cssFile>");
                }
                break;

            default:
                if(args.length >= 4){
                    switch(args[1].toLowerCase()){
                        case "add":
                            StringBuilder text = new StringBuilder();
                            for (int i = 4; i < args.length; i++) text.append(args[i]).append(" ");
                            Main.getNewsSystem().addNews(text.toString(), args[2]);
                            Main.getLogger().log("News", "News added.");
                            break;
                        case "remove":
                            Main.getLogger().warn("News", "Not implemented.");
                            break;
                    }
                }else{
                    Main.getLogger().log("News", "/news (add, remove, link) <author> <message>");
                }
        }
    }
}
