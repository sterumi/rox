package rox.main.twitter;
/*
Created by Bleikind
*/

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TwitterBot {

    Twitter twitter;
    Status status;

    public void start(String key, String secret){
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(key, secret);
        try {
            RequestToken requestToken = twitter.getOAuthRequestToken();
            AccessToken accessToken = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Open the following URL and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());
                System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
                String pin = br.readLine();
                try{
                    if(pin.length() > 0){
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    }else{
                        accessToken = twitter.getOAuthAccessToken();
                    }
                } catch (TwitterException te) {
                    if(401 == te.getStatusCode()){
                        System.out.println("Unable to get the access token.");
                    }else{
                        te.printStackTrace();
                    }
                }
            }

            storeAccessToken(twitter.verifyCredentials().getId() , accessToken);;

        } catch (TwitterException e) {
            e.printStackTrace();
            if(401 == e.getStatusCode()){
                System.out.println("Unable to get the access token.");
            }else{
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String message){
        try {
            twitter.updateStatus(new StatusUpdate(message));
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private void storeAccessToken(long id, AccessToken token){

    }

}
