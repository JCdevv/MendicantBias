package MendicantBias.MD;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import com.github.Doomsdayrs.Jikan4java.*;
import com.github.Doomsdayrs.Jikan4java.connection.Anime.AnimeConnection;
import com.github.Doomsdayrs.Jikan4java.connection.Character.CharacterConnection;
import com.github.Doomsdayrs.Jikan4java.connection.Manga.MangaConnection;
import com.github.Doomsdayrs.Jikan4java.types.Main.Anime.Anime;
import com.github.Doomsdayrs.Jikan4java.types.Main.Character.Character;
import com.github.Doomsdayrs.Jikan4java.types.Main.Manga.Manga;
import com.github.Doomsdayrs.Jikan4java.types.Support.Genre;

import org.json.simple.parser.ParseException;

import MendicantBias.MD.Main.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class MAL implements Command
{
    
    static Map<String,Command> commandMap = new HashMap<String,Command>();

    
	public void execute(String[] args, MessageReceivedEvent event) throws IOException, ParseException {
		String[] command = event.getMessage().getContentRaw().split(" ");
		String animeName = event.getMessage().getContentRaw().substring(11);
		String charName = event.getMessage().getContentRaw().substring(10);
		String mangaName = event.getMessage().getContentRaw().substring(11);
		
		
		EmbedBuilder eb = new EmbedBuilder();
		
		
		
		if(command[1].equals("anime")) {
			Anime anime = new AnimeConnection().searchSimple(animeName);
			Boolean isHentai = false;
			
			for(Genre g : anime.getGenres()){
				if(g.getName().equals("Hentai")) {
					event.getChannel().sendMessage("Anime with a Hentai genre are not allowed to avoid NSFW images.").queue();
					isHentai = true;
				}
			}
			if(isHentai == false) {
				eb.setTitle(anime.getTitle());
				eb.setDescription(anime.getSynopsis());
				eb.setImage(anime.getImageURL());
				eb.setColor(new Color(255, 0, 54));
				eb.addField("Score", Double.toString(anime.getScore()), true);
				eb.addField("Status", anime.getStatus(), true);
				eb.addField("More Info", anime.getUrl().toString(), true);
				
				
				event.getChannel().sendMessage(eb.build()).queue();
			}
		}
		
		if(command[1].equals("char")) {
			
			try {
				Character character = new CharacterConnection().search(charName);
				
				eb.setTitle(character.getName());
				eb.setDescription(character.getAbout());
				eb.setImage(character.getImage_url());
				eb.setColor(new Color(54, 0, 255));
				
				event.getChannel().sendMessage(eb.build()).queue();
			}
			catch(Exception e) {
				event.getChannel().sendMessage("No character found under the name: " + charName).queue();
			}
		}
		
		if(command[1].equals("manga")) {
			Manga manga = new MangaConnection().search(mangaName);
			Boolean isHentai = false;
			
			eb.setTitle(manga.getTitle());
			eb.setDescription(manga.getSynopsis());
			eb.setImage(manga.getImage_url());
			eb.setColor(new Color(255, 0, 54));
			eb.addField("Score", Double.toString(manga.getScore()), true);
			eb.addField("Status", manga.getStatus(), true);
			eb.addField("More Info", manga.getUrl().toString(), true);
				
				
			event.getChannel().sendMessage(eb.build()).queue();
			
		}
		
	}
	public String getUsage() {
		
		return "``!mal anime/manga/char <name>`` e.g ``!mal anime Evangelion``";
	}
	
	public String getDesc() {

		return "Searches MAL for the given query and returns at-a-glance information.";
	}



    
}