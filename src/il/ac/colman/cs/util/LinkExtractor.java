package il.ac.colman.cs.util;

import il.ac.colman.cs.ExtractedLink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
/**
 * Extract content from links
 */
public class LinkExtractor {
  public ExtractedLink extractContent(String url) {
	  BasicConfigurator.configure();
	  String content, title, description="bla", screenshotURL;
	  System.out.println("old: "+url);
	  Document doc = null;
	  try {
		  doc = Jsoup.connect(url).get();
	  } catch (IOException e) { }
	  String realURL = doc.title();
	  System.out.println("real: "+realURL);
	  Document realDoc = null;
	  try {
		  realDoc = Jsoup.connect(realURL).get();
	  } catch (IOException e) { }
	  
	  content = realDoc.body().text();
	  title = realDoc.title();
	  description = title;
	  
	  screenshotURL = ScreenshotGenerator.takeScreenshot(realURL);
	  
	  ExtractedLink link = new ExtractedLink(realURL, content, title, description, screenshotURL);
	
	  return link;
  }
}
