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

public class Info implements Command
{
    
	public void execute(String[] args, MessageReceivedEvent event) {
		Message msg = event.getMessage();
        MessageChannel chan = event.getChannel();
        Member op = event.getMember();
        Guild guild = event.getGuild();
        GuildController gc = guild.getController();
        
        if(op.hasPermission(Permission.BAN_MEMBERS)) {
	       if(args[0].equals("info")) {
	    	   try {
	    		   //JDA member
	    		   Member member = guild.getMemberById(args[1]);
	    		   //Local member (i really should've went for a different name)
	    		   Models.Member mbr = new DB().getMember(args[1]);
	    		   
	    		   EmbedBuilder embed = new EmbedBuilder();
	    		   
	    		   embed.setTitle("User Information");
	    		   embed.addField("User ID", mbr.getUserID(), true);
	    		   embed.addField("Username", mbr.getUsername(),true);
	    		   embed.addField("Join Date",mbr.getJoinDate(),true);
	    		   embed.addField("Warnings",mbr.getWarnings(),true);
	    		   embed.addField("Kicks",mbr.getKicks(),true);
	    		   embed.addField("Bans",mbr.getBans(),true);
	    		   
	    		   chan.sendMessage(embed.build()).queue();
	    			   
	    		   
	    	   }catch(Exception e){
	    		   chan.sendMessage("Please provide a valid User ID \n"
	    		   		+ "``!info <userid>``").queue();
	    	   }
	       }
        }
        else {
        	chan.sendMessage("Only moderators can .info a user").queue();
        }
		
	}
	public String getUsage() {
		
		return "Replies with a Pong to your Ping";
	}
	
	public String getDesc() {

		return "Replies with a Pong to your Ping";
	}



    
}