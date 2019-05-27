package MendicantBias.MD;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import MendicantBias.MD.Main.Command;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class Ping implements Command
{
    
    static Map<String,Command> commandMap = new HashMap<String,Command>();

    
	public void execute(String[] args, MessageReceivedEvent event) {
		Message msg = event.getMessage();
        MessageChannel chan = event.getChannel();
        User user = event.getAuthor();
        Guild guild = event.getGuild();
        GuildController gc = guild.getController();
        
        if(msg.getContentRaw().equals("!ping")) {
        	chan.sendMessage("Pong").queue();
        }
		
	}
	public String getUsage() {
		
		return "Replies with a Pong to your Ping";
	}
	
	public String getDesc() {

		return "Replies with a Pong to your Ping";
	}



    
}