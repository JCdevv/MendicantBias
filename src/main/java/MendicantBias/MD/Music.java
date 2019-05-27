package MendicantBias.MD;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import MendicantBias.MD.Main.Command;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.GuildController;

public class Music implements Command
{
	private final AudioPlayerManager playerManager;
	private final Map<String, GuildMusicManager> musicManagers;
	
	public Music() {
		  this.musicManagers = new HashMap<>();

		    this.playerManager = new DefaultAudioPlayerManager();
		    AudioSourceManagers.registerRemoteSources(playerManager);
		    AudioSourceManagers.registerLocalSource(playerManager);
	}
       
	public void execute(String[] args, MessageReceivedEvent event) {
		Message msg = event.getMessage();
        MessageChannel chan = event.getChannel();
        User user = event.getAuthor();
        Guild guild = event.getGuild();
        GuildController gc = guild.getController();
        
        String[] command = event.getMessage().getContentRaw().split(" ");
        
        final GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
        
        if(musicManager.player.getPlayingTrack() == null) {
        	guild.getAudioManager().closeAudioConnection();
        }
        
        if("!music".equals(command[0])){
	        if (guild != null) {
	          if ("play".equals(command[1]) && command.length == 3) {
	            loadAndPlay(event.getTextChannel(), command[2]);
	          } 
	        	  
	          if ("skip".equals(command[1])) {
	            skipTrack(event.getTextChannel());
	          }
	          
	          if("stop".equals(command[1])) {
	        	  musicManager.scheduler.stop();
	        	   
	        	  event.getChannel().sendMessage("Playback stopped and queue cleared. Type ``~leave`` to have me leave the VC").queue();
	        	  
	          }
	          
	          if("leave".equals(command[1])) {
	        	  guild.getAudioManager().closeAudioConnection();
	        	  
	        	  event.getChannel().sendMessage("Leaving..").queue(); 
	          }
	          
	                
	          if("np".equals(command[1])) {
	        	  
	        	  AudioTrack currentTrack = musicManager.player.getPlayingTrack();
	        	 
	        	  if(currentTrack != null) {
	        		  event.getChannel().sendMessage("Currently Playing:" + musicManager.scheduler.nowPlaying()).queue();
	        	  }
	        	  else {
	        		  event.getChannel().sendMessage("Nothing is currently being played").queue();
	        	  }
	          }
	          
	          if("queue".equals(command[1])) {
	        	  BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
	        	  if(queue != null) {
	        		  StringBuilder sb = new StringBuilder();
	            	  
	            	  for(AudioTrack track : queue) {
	            		  sb.append("[" + getTimestamp(track.getDuration()) + "]");
	            		  sb.append(track.getInfo().title).append("\n");
	            	  }
	            	  
	            	  event.getChannel().sendMessage(sb.toString()).queue();
	        	  }
	        	  else {
	        		  event.getChannel().sendMessage("Queue is Empty").queue();
	        	  }
	        	  
	          }
	          
	          
	         /* if("~about".equals(command[0])) {
	        	  event.getChannel().sendMessage("Made by Voi.").queue();
	          }
	          
	          if("~help".equals(command[0])) {
	        	  event.getChannel().sendMessage("Use ``~play <video url>`` \n"
	        	  		+ "Supports YouTube, Soundcloud, Vimeo, Bandcamp.").queue();
	        }*/
	        }
        }
	}

         
      private void loadAndPlay(final TextChannel channel, final String trackUrl) {
        final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
          @Override
          public void trackLoaded(AudioTrack track) {
            channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

            play(channel.getGuild(), musicManager, track);
          }

          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            AudioTrack firstTrack = playlist.getSelectedTrack();

            if (firstTrack == null) {
              firstTrack = playlist.getTracks().get(0);
            }

            channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

            play(channel.getGuild(), musicManager, firstTrack);
          }

          @Override
          public void noMatches() {
            channel.sendMessage("Nothing found by " + trackUrl).queue();
          }

          @Override
          public void loadFailed(FriendlyException exception) {
            channel.sendMessage("Could not play: " + exception.getMessage()).queue();
          }
        });
      }
      
      private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());

        musicManager.scheduler.queue(track);
      }
      
      private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
      }
      
      
      private static String getTimestamp(long milliseconds)
      {
          int seconds = (int) (milliseconds / 1000) % 60 ;
          int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
          int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

          if (hours > 0)
              return String.format("%02d:%02d:%02d", hours, minutes, seconds);
          else
              return String.format("%02d:%02d", minutes, seconds);
      }
       
      private static void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
          for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
            audioManager.openAudioConnection(voiceChannel);
            break;
          }
        }
      }
      
      private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
          String guildId = guild.getId();
          GuildMusicManager musicManager = musicManagers.get(guildId);

          if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
          }

          guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

          return musicManager;
        }
    
		
	
	public String getUsage() {
		
		return "Use ``~play <video url>``";
	}
	
	public String getDesc() {

		return "Supports Youtube,Soundcloud and Vimeo and Bandcamp";
	}
}

