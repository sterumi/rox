package rox.main.discord.listener;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class MessageReceivedListener implements EventListener {


    @Override
    public void onEvent(Event event) {
        if(event instanceof MessageReceivedEvent){

            MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;

            String message = messageReceivedEvent.getMessage().getContentRaw();

            String[] args = message.split(" ");

            Member user = messageReceivedEvent.getMember();

            if(message.startsWith("!")){
                switch(args[0]){
                    case "!info":

                        MessageAction messageAction = messageReceivedEvent.getChannel().sendMessage("Ich bin doch Online?");
                        messageAction.complete();
                        break;

                    case "!say":
                        if(args.length > 1){
                            //List<Role> roles = ((MessageReceivedEvent) event).getMember().getRoles();
                            //roles.forEach(role -> System.out.println(role.getName()));

                            if(user.getRoles().get(0).getPosition() >= 2){
                                messageReceivedEvent.getChannel().sendMessage(message.substring(4)).complete();
                            }else{
                                user.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Du hast keine Berechtigung dafür!").complete());
                            }
                        }
                }
            }
        }
    }
}
