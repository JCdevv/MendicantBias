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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

// handles all database connectivity

public class DB
{
	Connection c = null;
	Statement s = null;
	ResultSet r = null;

	public Statement getConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:config.sqlite");
			s = c.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
		return s;
	}

	public void closeConnection() {
		try {
			if (s != null) {
				s.close();
			}
			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean setReportID(String id) throws SQLException {
		boolean b = false;
		try {
			String query = "INSERT INTO config VALUES (" + id + ")";
			System.out.println(query);
			b = getConnection().execute(query);
			closeConnection();
			b = true;
		} catch (SQLException s) {
			throw new SQLException("Report Not Added");
		}
		return b;
	}
	
	public String getReportID() throws SQLException{

		String id = "";
		try {
			String sql = "select report_channel_id from config";
			ResultSet resultset = getConnection().executeQuery(sql);

			if (resultset != null) {
				while (resultset.next()) {
					try {
						id = resultset.getString("report_channel_id");


					} catch (SQLException s) {
						s.printStackTrace();
					}
				}
				resultset.close();
			}
		} catch (SQLException s) {
			System.out.println(s);
		}

		closeConnection();
		return id;
	} 
}
 