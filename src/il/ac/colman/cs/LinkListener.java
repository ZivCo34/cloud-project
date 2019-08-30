package il.ac.colman.cs;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.*;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
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
    AmazonSQS clientSQS = AmazonSQSClientBuilder.defaultClient();
	AmazonS3 clientS3 = AmazonS3ClientBuilder.defaultClient();
    
    while (true) {
    	ReceiveMessageResult result = clientSQS.receiveMessage(System.getProperty("config.sqs.url"));
    	List<Message> messages = result.getMessages();
    	if (messages.size() == 0) {
    		try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
    	}
    	else {
    		for (Message message : messages) {
    			String url = message.getBody();
    			ExtractedLink link = linkExtractor.extractContent(url);
    			Map<String, MessageAttributeValue> messageAttributes = message.getMessageAttributes();
    			String track = messageAttributes.get("track").getStringValue();
    			File screenshot = new File(link.getScreenshotURL());
    			clientS3.putObject("screenshots-from-tweets", link.getTitle(), screenshot);
    			dataStorage.addLink(link, track);
    		}
    	}
    }
  }
}
