package il.ac.colman.cs;

import il.ac.colman.cs.util.LinkExtractor;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterListener {
  public static void main(String[] args) {
    // Create our twitter configuration
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
        .setOAuthConsumerKey("*********************")
        .setOAuthConsumerSecret("******************************************")
        .setOAuthAccessToken("**************************************************")
        .setOAuthAccessTokenSecret("******************************************");

    // Create our Twitter stream
    TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
    TwitterStream twitterStream = tf.getInstance();


    /*
      This is where we should start fetching the tweets using the Streaming API
      See Example 9 on this page: http://twitter4j.org/en/code-examples.html#streaming
    */
    twitterStream.filter(System.getProperty("track"));
  }
}
