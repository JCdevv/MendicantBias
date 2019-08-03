package MendicantBias.MD;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import MendicantBias.MD.Main.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class Help implements Command
{
    
    static Map<String,Command> commandMap = new HashMap<String,Command>();

    
	public void execute(String[] args, MessageReceivedEvent event) {
		Message msg = event.getMessage();
        MessageChannel chan = event.getChannel();
        Member member = event.getMember();
        Guild guild = event.getGuild();
        GuildController gc = guild.getController();
        
        EmbedBuilder eb = new EmbedBuilder();
        
        String userCommandList = "``!mal`` \n"
        		+ "``!music`` \n"
        		+ "``!ping`` \n"
        		+ "``!report`` - PM Only!";
        
        
        String modCommandList = "``!mal`` \n"
        		+ "``!music`` \n"
        		+ "``!ping``";
        
        if(member.hasPermission(Permission.BAN_MEMBERS)){
        	eb.setTitle("Commands");
        	eb.addField("Permissions Level", "Moderator", false);
        	eb.setDescription("For all commands, do !commandname usage/about for extra info");
        	eb.addField("Commands", modCommandList, true);	
        	
        	chan.sendMessage(eb.build()).queue();
        }
        else {
        	eb.setTitle("Commands");
        	eb.addField("Permissions Level", "User", false);
        	eb.setDescription("For all commands, do !commandname usage/about for extra info EXCEPT !report");
        	eb.addField("Commands", userCommandList, true);	
        	
        	chan.sendMessage(eb.build()).queue();
        }
        
		
	}
	public String getUsage() {
		
		return "``!help``";
	}
	
	public String getDesc() {

		return "Replies with a list of available commands.";
	}



    
}