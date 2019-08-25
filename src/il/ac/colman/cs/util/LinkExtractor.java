package il.ac.colman.cs.util;

import il.ac.colman.cs.ExtractedLink;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
/**
 * Extract content from links
 */
public class LinkExtractor {
  public ExtractedLink extractContent(String url) {
	  String content, title, description, screenshotURL;
	  
	  Document doc = null;
	  try {
		  doc = Jsoup.connect(url).get();
	  } catch (IOException e) { }
	  
	  content = doc.body().text();
	  title = doc.title();
	  description = doc.getElementsByTag("description").first().val();
	  
	  
	  
	  ExtractedLink link = new ExtractedLink(url, content, title, description, screenshotURL);
	
	  return link;
  }
}
