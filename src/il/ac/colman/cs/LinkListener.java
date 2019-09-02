package il.ac.colman.cs;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.sqs.*;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

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
    
    while (true) {
    	ReceiveMessageRequest request = new ReceiveMessageRequest(System.getProperty("config.sqs.url")).withMessageAttributeNames("All");
    	List<Message> messages = clientSQS.receiveMessage(request).getMessages();
    	if (messages.size() == 0) {
    		try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
    	}
    	else {
    		for (Message message : messages) {
    			String url = message.getBody();
    			Map<String, MessageAttributeValue> messageAttributes = message.getMessageAttributes();
    			String track = messageAttributes.get("track").getStringValue();
    			ExtractedLink link = linkExtractor.extractContent(url);
    			dataStorage.addLink(link, track);
    		}
    	}
    }
  }
}
