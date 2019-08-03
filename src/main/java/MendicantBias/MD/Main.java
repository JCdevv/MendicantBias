package MendicantBias.MD;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.LoginException;
import org.json.simple.parser.ParseException;
import Models.Member;
import Models.Report;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;




public class Main extends ListenerAdapter
{
    
    static Map<String,Command> commandMap = new HashMap<String,Command>();

    String prefix = "!";

    Boolean added = false;
    Boolean filtered = false;

    public static void main( String[] args ) throws LoginException, InterruptedException, ParseException
    {
    	final JDA jda = new JDABuilder(AccountType.BOT).setToken("")
    			.setGame(Game.playing("PM me !report"))
    			.setStatus(OnlineStatus.OFFLINE)
    			.buildBlocking();
        jda.addEventListener(new Main());
        createCommands();
        try {
			loadConfig();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    public static void createCommands() {
        commandMap.put("ping", new Ping());
        commandMap.put("music", new Music());
        commandMap.put("mal", new MAL());
        commandMap.put("help", new Help());
        
    }

    public interface Command {
        void execute(String[] args, MessageReceivedEvent event) throws IOException, ParseException;

        String getUsage();

        String getDesc();

    }
    
    
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
    	
    	if(!event.getUser().getId().equals("441763965710893067")) {
    		new ReactionHandler().execute(null, event);
    	}
    	else{
    		return;
    	}
    }
    
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
    	
    	String[] command = event.getMessage().getContentRaw().split(" ");

    	try {
			new ReportHandler().execute(command, event);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    	
    	HTTP http = new HTTP();
    	User user = event.getUser();
    	String joinDate = event.getMember().getJoinDate().toString();
    	Member member = new Member(user.getId().toString(),user.getName(),joinDate,null,null,null);
    	
    	Thread thread = new Thread(new Runnable(){
			public void run(){
				http.addUser(member);
			}
		});
		
		// thread.start();    
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel chan = event.getChannel();
        User user = event.getAuthor();
        String content = msg.getContentRaw();

        if(!content.startsWith(prefix)) return;
        String[] args = msg.getContentRaw().substring(prefix.length()).split(" ");
        
        // This is pretty ugly... but IOOB Exceptions force me to do this crap
        
        if (commandMap.containsKey(args[0])) {
        	if(args.length == 1 && args[0].equals("help")) {
        		Command command = commandMap.get(args[0]);
        		try {
					command.execute(args, event);
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	else if(args[1].equals("usage")) {
        		
        		Command command = commandMap.get(args[0]);

    			chan.sendMessage(command.getUsage()).queue();

    	    }
        	else if(args[1].equals("about")) {
        		Command command = commandMap.get(args[0]);
        	
    			chan.sendMessage(command.getDesc()).queue();
        	}
        	else if (!args[1].equals("usage") && !args[1].equals("about")){
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
    
    public static void loadConfig() throws SQLException {
    	
    	DB db = new DB();
    	HTTP http = new HTTP();
    	
    	String serverID = http.getReportID();
    	String localID = db.getReportID();
    	
    	if(!serverID.equals(localID)) {
    		db.setReportID(serverID);
    		System.out.println("Local config updated");
    	}
    	else {
    		System.out.println("Local config up to date");
    	}
    	
    }
}
