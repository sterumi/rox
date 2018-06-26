package rox.main.util;

public class CommandData {

    private final String command, description, args;

    public CommandData(String command, String description, String args){
        this.command = command;
        this.description = description;
        this.args = args;
    }

    public String getArgs() {
        return args;
    }

    public String getDescription() {
        return description;
    }

    public String getCommand() {
        return command;
    }
}
