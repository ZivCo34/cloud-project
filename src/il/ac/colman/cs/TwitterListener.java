package il.ac.colman.cs;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.varia.NullAppender;

import com.amazonaws.services.sqs.*;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterListener {
  public static void main(String[] args) {
    // Create our twitter configuration
	BasicConfigurator.configure(new NullAppender());
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
        .setOAuthConsumerKey("zJdvg8A5MZEpK4Fy2Kkr5dkG8")
        .setOAuthConsumerSecret("HbsFd7i708vXuI2DGGzpMQXiwDZRv1opliszSS9ovmDHQSWIYD")
        .setOAuthAccessToken("1113426967641186309-ZGx8yn4s68QSRFym0Mft6UG3jr5xyO")
        .setOAuthAccessTokenSecret("sxS7pGC92m09SCEhWtdVmu2ghMTPFSBAaJs5A6YmhnyUT");

    // Create our Twitter stream
    TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
    TwitterStream twitterStream = tf.getInstance();
    
	AmazonSQS client = AmazonSQSClient.builder().withRegion("us-east-1").build();

    StatusListener listener = new StatusListener(){
    	
        public void onStatus(Status status) {
        	System.out.println(status.getText());
        	if(status.getText().contains("http"))
        		client.sendMessage("https://sqs.us-east-1.amazonaws.com/042445528747/TweetQueue",status.getText());
        }
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
        public void onException(Exception ex) {
            ex.printStackTrace();
        }
		@Override
		public void onScrubGeo(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onStallWarning(StallWarning arg0) {
			// TODO Auto-generated method stub
			
		}
    };
    
    twitterStream.addListener(listener);
    
    twitterStream.sample();
  }
}