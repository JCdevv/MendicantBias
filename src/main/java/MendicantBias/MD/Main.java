package MendicantBias.MD;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.json.simple.parser.ParseException;

import Models.Report;
import net.dv8tion.jda.core.AccountType;
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

public class Main extends ListenerAdapter
{
    
    static Map<String,Command> commandMap = new HashMap<String,Command>();

    String prefix = "!";

    Boolean added = false;
    Boolean filtered = false;



    public static void main( String[] args ) throws LoginException, InterruptedException
    {
            JDA jda = new JDABuilder(AccountType.BOT).setToken("").buildBlocking();
            jda.addEventListener(new Main());
            createCommands();

    }
    
    public static void createCommands() {
        commandMap.put("ping", new Ping());
        commandMap.put("music", new Music());
        commandMap.put("mal", new MAL());
    }

    public interface Command {
        void execute(String[] args, MessageReceivedEvent event) throws IOException, ParseException;

        String getUsage();

        String getDesc();

    }
    
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
    	
    	HTTP http = new HTTP();
    	
    	String[] command = event.getMessage().getContentRaw().split(" ");
    	User user = event.getMessage().getAuthor();
    	String finalReport = "";
    	User reportedUser = event.getJDA().getUserById(command[1]);
    	User reportee = event.getJDA().getUserById(user.getId());
    	
    	System.out.println(event.getMessage().getContentRaw());
    	
    	if(command[0].equals("!report")) {
    		if(command[1].length() == 18) {
    			String userID = command[1];
    			
    			for(int i = 2; i < command.length; i++) {
    				
    				finalReport += " " + command[i];
    			}
    			
    			TextChannel chan = event.getJDA().getTextChannelById("486900195624353803");
    			
    			EmbedBuilder eb = new EmbedBuilder();
    			
    			eb.setTitle("New User Report");
    			eb.addField("Reported User: ", reportedUser.getAsMention(),true);
    			eb.addField("Reported By: ", user.getAsMention(),true);
    			eb.setDescription(finalReport);
    			eb.setColor(new Color(255, 0, 54));
    			
    			chan.sendMessage(eb.build()).queue();
    			
    			Report report = new Report(0, reportedUser.getId(), reportee.getId(), finalReport);
    			
    			http.addReport(report);
    		}
    	}
    	
    	
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel chan = event.getChannel();
        User user = event.getAuthor();


        String content = msg.getContentRaw();


        if(!content.startsWith(prefix)) return;
        String[] args = msg.getContentRaw().substring(prefix.length()).split(" ");
        if (commandMap.containsKey(args[0])) {
                Command command = commandMap.get(args[0]);
        try {
			command.execute(args, event);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    }
}