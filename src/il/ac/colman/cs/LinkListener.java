package il.ac.colman.cs;

import java.sql.SQLException;
import java.util.List;

import com.amazonaws.services.sqs.*;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import il.ac.colman.cs.util.DataStorage;
import il.ac.colman.cs.util.LinkExtractor;

public class LinkListener {
  public static void main(String[] args) throws SQLException {
    // Connect to the database
    DataStorage dataStorage = new DataStorage();

    // Initiate our link extractor
    LinkExtractor linkExtractor = new LinkExtractor();

    // Listen to SQS for arriving links
    AmazonSQS client = AmazonSQSClientBuilder.defaultClient();
    
    while (true) {
    	ReceiveMessageResult result = client.receiveMessage(System.getProperty("config.sqs.url"));
    	List<Message> messages = result.getMessages();
    	if (messages.size() == 0) {
    		try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
    	}
    	else {
    		for (Message message : messages) {
    			// Do something with message
    			String body = message.getBody();
    			String url = null;
    			String[] words = body.split(" ");
    			for(String word: words) {
    				if(word.startsWith("http")) {
    					url = word;
    					break;
    				}
    			}
    			ExtractedLink link = linkExtractor.extractContent(url);
    			
    		}
    	}
    }

    // Save everything in the database
  }
}
