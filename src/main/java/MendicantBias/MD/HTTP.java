package MendicantBias.MD;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;

import com.github.Doomsdayrs.Jikan4java.*;
import com.github.Doomsdayrs.Jikan4java.connection.Anime.AnimeConnection;
import com.github.Doomsdayrs.Jikan4java.connection.Character.CharacterConnection;
import com.github.Doomsdayrs.Jikan4java.connection.Manga.MangaConnection;
import com.github.Doomsdayrs.Jikan4java.types.Main.Anime.Anime;
import com.github.Doomsdayrs.Jikan4java.types.Main.Character.Character;
import com.github.Doomsdayrs.Jikan4java.types.Main.Manga.Manga;
import com.github.Doomsdayrs.Jikan4java.types.Support.Genre;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import MendicantBias.MD.Main.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import Models.Member;
import Models.Report;


public class HTTP
{
	
	 public ArrayList<Member> getAllUsers() {
		 
		 ArrayList<Member> users = new ArrayList<>();
		 HttpURLConnection urlConnection;
	     InputStream in = null;
	     try{
	    	 URL url = new URL("http://localhost/config/api");

	         urlConnection = (HttpURLConnection) url.openConnection();
	         
	         in = new BufferedInputStream(urlConnection.getInputStream());
	     } catch(IOException e){
	    	 e.printStackTrace();
	     }

	     String response = convertStreamToString(in);

	     System.out.println("Server response = " + response);

	     try {

	         JSONArray jsonArray = new JSONArray(response);

	         for (int i=0; i < jsonArray.length(); i++) {

	             int id = jsonArray.getJSONObject(i).getInt("id");
	             String username = jsonArray.getJSONObject(i).get("username").toString();
	             String userID = jsonArray.getJSONObject(i).get("userID").toString();
	             String warnings = jsonArray.getJSONObject(i).get("warnings").toString();
	             String bans = jsonArray.getJSONObject(i).get("bans").toString();
	             String kicks = jsonArray.getJSONObject(i).get("kicks").toString();
	             String join = jsonArray.getJSONObject(i).get("joinDate").toString();

	             Member member = new Member(username, userID, warnings,bans,join,kicks);
	             users.add(member);

	         }
	     } catch (JSONException e) {
	         e.printStackTrace();
	         }
	     return users;
	 }
	 
	 public void addUser(Member member) {
		 HashMap<String, String> params = new HashMap<String,String>();
			
         Gson gson = new Gson();
        
         String reportJson = gson.toJson(member);


         System.out.println(reportJson);

         params.put("user", reportJson);

         String url = "http://localhost:8005/config/api";
   
         performPostCall(url, params);

     }
	 public void addReport(Report report) {
		
	   	  HashMap<String, String> params = new HashMap<String,String>();
		
          Gson gson = new Gson();
         
          String reportJson = gson.toJson(report);


          System.out.println(reportJson);

          params.put("report", reportJson);

          String url = "http://localhost:8005/config/report";
    
          performPostCall(url, params);

      }
	 
	  public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {
	   	  URL url;
		  String response = "";
	
		  try {
			  url = new URL(requestURL);
			
			  //create the connection object
			  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			  conn.setReadTimeout(15000);
			  conn.setConnectTimeout(15000);
			  conn.setRequestMethod("POST");
			  conn.setDoInput(true);
			  conn.setDoOutput(true);
			
			  OutputStream os = conn.getOutputStream();
			  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
	
			  writer.write(getPostDataString(postDataParams));
			  writer.flush();
			  writer.close();
			
			  os.close();
	
			  int responseCode = conn.getResponseCode();
			  System.out.println("responseCode = " + responseCode);
	
			  if (responseCode == HttpsURLConnection.HTTP_OK) {
				  String line;
				  BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				  while ((line = br.readLine()) != null) {
					  response += line;
				  }
			  } else {
				  response = "";
			  }
	
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  System.out.println("response = " + response);
		  return response;
	  }


	  private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		
		  // Test api key
		  String apiKey = "abc123";
		  StringBuilder result = new StringBuilder();
		  boolean first = true;
		  for(Map.Entry<String, String> entry: params.entrySet()){
			  if (first){
				  first = false;
	          }
			  else{
				  result.append("&");
	          }
	       result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
	       result.append("=");
	       result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	   }
		
		   result.append("&");
	       result.append(URLEncoder.encode("apikey","UTF-8"));
	       result.append("=");
	       result.append(URLEncoder.encode(apiKey,"UTF-8"));
	        
		  return result.toString();
	   }
	
	   public String convertStreamToString(InputStream is) {
		   Scanner s = new Scanner(is).useDelimiter("\\A");
	       return s.hasNext() ? s.next() : "";
	  }
}
 