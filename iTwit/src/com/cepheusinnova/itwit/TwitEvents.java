/**
 * 
 */
package com.cepheusinnova.itwit;

import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author abhijeetr
 *
 */
public class TwitEvents {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		
		// Start Twitter Streaming Code
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		
		//Set Configuration for Twitter4j
		cb.setUser("captsparrow");
		cb.setPassword("abhi2158");
		//cb.setOAuthConsumerKey("uqRg4GugvZ425z8QAU6x5w");
		//cb.setOAuthConsumerSecret("5TTTBuhtp9AVjg2ePuqPwVrTXFDNbsYJovIa9obRquA");
		
		Configuration cfg = cb.build();
		
		callStreamAPI(cfg);  //call stream API with config object
		 	
		//updateStatusAPI(cfg, "This is a #test message 1"); 	
		 	
		 	
	}
	
	public static void callStreamAPI(Configuration cfg){
		
				
		//create stream instance
		TwitterStream twitterStream = new TwitterStreamFactory(cfg).getInstance();
		System.out.println("[iTwit]: Start Twitter Stream");
				
		//create twitter listener	
		StatusListener listener = new StatusListener() {
					
						public void onStatus(Status status) {
							 
						//print status stream
						     System.out.println("@" + status.getUser().getName() + "==" + status.getText());
							     
						}
							 
					   public void onDeletionNotice(
					     StatusDeletionNotice statusDeletionNotice) {
					    
					   }
					 
					   public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
					    
					   }
					 
					   public void onScrubGeo(long userId, long upToStatusId) {
					    
					   }
							 
					   public void onException(Exception ex) {
					    ex.printStackTrace();
					   }
				 	};
				 	
				 	twitterStream.addListener(listener);
				 	
				 	twitterStream.sample();
	
		}
	public static void updateStatusAPI(Configuration cfg, String setStatus){
		try {
            Twitter twitter = new TwitterFactory(cfg).getInstance();
            try {
                // get request token.
                // this will throw IllegalStateException if access token is already available
                RequestToken requestToken = twitter.getOAuthRequestToken();
                System.out.println("Got request token.");
                System.out.println("Request token: " + requestToken.getToken());
                System.out.println("Request token secret: " + requestToken.getTokenSecret());
                AccessToken accessToken = null;

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (null == accessToken) {
                    System.out.println("Open the following URL and grant access to your account:");
                    System.out.println(requestToken.getAuthorizationURL());
                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                    String pin = br.readLine();
                    try {
                        if (pin.length() > 0) {
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = twitter.getOAuthAccessToken(requestToken);
                        }
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            System.out.println("Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }
                }
                System.out.println("Got access token.");
                System.out.println("Access token: " + accessToken.getToken());
                System.out.println("Access token secret: " + accessToken.getTokenSecret());
            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    System.out.println("OAuth consumer key/secret is not set.");
                    System.exit(-1);
                }
            }
            Status status = twitter.updateStatus(setStatus);
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
		
	}
	
}


