package il.ac.colman.cs;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.varia.NullAppender;

import com.amazonaws.services.sqs.*;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.waiters.MaxAttemptsRetryStrategy;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterListener {
  public static void main(String[] args) {
    // Create our twitter configuration
	BasicConfigurator.configure(new NullAppender());
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
        .setOAuthConsumerKey(System.getProperty("config.twitter.consumer.key"))
        .setOAuthConsumerSecret(System.getProperty("config.twitter.consumer.secret"))
        .setOAuthAccessToken(System.getProperty("config.twitter.access.token"))
        .setOAuthAccessTokenSecret(System.getProperty("config.twitter.access.secret"));

    // Create our Twitter stream
    TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
    TwitterStream twitterStream = tf.getInstance();
    
	AmazonSQS client = AmazonSQSClient.builder().build();
	
	Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
	messageAttributes.put("track", new MessageAttributeValue()
	        .withDataType("String")
	        .withStringValue(System.getProperty("config.twitter.track")));

    StatusListener listener = new StatusListener(){
    	
        public void onStatus(Status status) {
        	if(status.getURLEntities().length>0) {
        		SendMessageRequest msg = new SendMessageRequest(System.getProperty("config.sqs.url"), status.getURLEntities()[0].getURL());
        		msg.withMessageAttributes(messageAttributes);
        		client.sendMessage(msg);
        	}
        }
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
        public void onException(Exception ex) {}
		@Override
		public void onScrubGeo(long arg0, long arg1) {}
		@Override
		public void onStallWarning(StallWarning arg0) {}
    };
    
    twitterStream.addListener(listener);
    
    twitterStream.filter(System.getProperty("config.twitter.track"));
    
    twitterStream.sample();
  }
}