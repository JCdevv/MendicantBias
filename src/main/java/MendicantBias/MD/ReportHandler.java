package MendicantBias.MD;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import MendicantBias.MD.Main.Command;
import Models.Report;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class ReportHandler 
{
   
	public void execute(String[] args, PrivateMessageReceivedEvent event)  {

    	HTTP http = new HTTP();
    	
    	String[] command = event.getMessage().getContentRaw().split(" ");
    	User user = event.getMessage().getAuthor();
    	String finalReport = "";
    	User reportee = event.getJDA().getUserById(user.getId());
    	
    	if(command[0].equals("!report")) {
    		if(command[1].equals("help")){
    			event.getChannel().sendMessage("``!report <userid> <details>`` \n"
    					+ "e.g: ``!report 441763965710893067 Spoilers in #general`` \n"
    					+ "This will send a report to server moderators, ensure the included User ID is the ID of who you are reporting.").queue();
    		}
    		else if(command[1].length() == 18) {
    			String userID = command[1];
    			
    			for(int i = 2; i < command.length; i++) {
    				
    				finalReport += " " + command[i];
    			}
    			
    			TextChannel chan = event.getJDA().getTextChannelById("486900195624353803");
    			User reportedUser = event.getJDA().getUserById(command[1]);
    			
    			EmbedBuilder eb = new EmbedBuilder();
    			
    			eb.setTitle("New User Report");
    			eb.addField("Reported User: ", reportedUser.getAsMention(),true);
    			eb.addField("Reported By: ", user.getAsMention(),true);
    			eb.setDescription(finalReport);
    			eb.setColor(new Color(255, 0, 54));
    			eb.setFooter("React With 1\u20E3 To Remove This Report, Add A 2\u20E3 Reaction To Alert Other Moderators", null);
    			
    			chan.sendMessage(eb.build()).queue(m -> m.addReaction("1\u20E3").queue());
    			
    			Report report = new Report(0, reportedUser.getId(), reportee.getId(), finalReport);
    			
    			Thread thread = new Thread(new Runnable(){
    				public void run(){
    					http.addReport(report);
    				}
    			});
 
    			
    			//thread.start();
    		}
    	}	
	}    
}