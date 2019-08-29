package il.ac.colman.cs.util;

import il.ac.colman.cs.ExtractedLink;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.sql.Statement;
import java.sql.ResultSet;

public class DataStorage {
	String url;
	Connection conn;

	public DataStorage() throws SQLException {
		String dbName = System.getProperty("RDS_DB_NAME");
	    String userName = System.getProperty("RDS_USERNAME");
	    String password = System.getProperty("RDS_PASSWORD");
	    String hostname = System.getProperty("RDS_HOSTNAME");
	    String port = System.getProperty("RDS_PORT");
	    url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "?password=" + password;
		conn = DriverManager.getConnection(url);
	}

	public void addLink(ExtractedLink link, String track) {
		try {
			Statement insert = conn.createStatement();
			String linkurl = link.getUrl();
			Date date = new Date();
			String content = link.getContent();
			String title = link.getTitle();
			String description = link.getDescription();
			String statement = "INSERT INTO links (link, track, date, content, title, description) VALUES ('" 
					+ linkurl + "','" + track + "','" + date + "','" + content + "','" + title + "','" + description + "');";
			insert.execute(statement);
			insert.close();
		} catch (SQLException e) {}
	}

	public List<ExtractedLink> search(String query) {
		List<ExtractedLink> result = new LinkedList<>();
		try {
			Statement output = conn.createStatement();
			ResultSet res = output.executeQuery(query);
			res.first();
			ExtractedLink link = getExtractedLink(res);
			result.add(link);
			while (res.next()) {
				link = getExtractedLink(res);
				result.add(link);
			}
		} catch (SQLException e) {}
		return result;
	}
	
	public ExtractedLink getExtractedLink(ResultSet res) {
		ExtractedLink link = null;
		try {
		String linkurl = res.getString("link");
		String content = res.getString("content");
		String title = res.getString("title");
		String description = res.getString("description");
		File screenshot = new File("screenshot_" + res.getDate("date") + ".png").getAbsoluteFile();
		link = new ExtractedLink(linkurl, content, title, description, screenshot.getPath());
		}  catch (SQLException e) {}
		return link;
	}
}
