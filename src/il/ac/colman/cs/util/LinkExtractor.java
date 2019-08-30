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
		  ExtractedLink link = new ExtractedLink(realURL, content, title, description, screenshotURL);
		  return link;
	  } catch (IOException e) {}
	 return null;
  }
}
