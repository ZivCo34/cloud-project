package il.ac.colman.cs.util;

import java.io.IOException;
import java.sql.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import il.ac.colman.cs.ExtractedLink;
/**
 * Extract content from links
 */
public class LinkExtractor {
  public ExtractedLink extractContent(String url) {
	  //BasicConfigurator.configure();
	  String content, title, description, screenshotURL;
	  try {
		  Document doc = Jsoup.connect(url).get();
		  String realURL = doc.title();
		  doc = Jsoup.connect(realURL).get();
		  content = doc.body().text();
		  title = doc.title();
		  description = title;
		  screenshotURL = ScreenshotGenerator.takeScreenshot(realURL, title);
		  Date time = new Date(System.currentTimeMillis());
		  ExtractedLink link = new ExtractedLink(realURL, content, title, description, screenshotURL, time.toString());
		  return link;
	  } catch (IOException e) {}
	  return null;
  }
}
