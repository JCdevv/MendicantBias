package MendicantBias.MD;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import MendicantBias.MD.Main.Command;
import Models.Report;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

//JDA Utils has a reaction handler.. i think. Which will undoubtedly perform better than this.
public class ReactionHandler 
{
  
	public void execute(String[] args, MessageReactionAddEvent event)  {
		
		String msg = event.getMessageId();
		User user = event.getUser();
		TextChannel chan = event.getTextChannel();
		
		chan.getMessageById(event.getMessageId()).queue(m -> {
			// Only performs the following actions if the user is a Moderator (Moderator assumed to have Ban permissions) reacting to a BOT message.
			if(m.getAuthor().getId().equals("441763965710893067") && event.getMember().getPermissions().contains(Permission.BAN_MEMBERS)) {
				String emote = event.getReactionEmote().getName();
				
				// Moderators should be informed about what emotes perform what action, so they dont use a delete reaction on a message that shouldn't be deleted.
				switch (emote) {
					case "1\u20E3":
						chan.deleteMessageById(m.getId()).queue();
					break;
					
					case "2\u20E3":
						chan.sendMessage(user.getName() + " has highlighted this report").queue();
					break;
				}	
			}
		});	
	}

}
