package com.ds.di.balancer.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;



class LoadBalanceTesterThread extends Thread {

	
	private static final String 	HTTP_PREFIX		= "http://";
	
	private	Integer myThreadID;
	private	String domain;
	private	String requestURL;
	
	private	Integer myIterations;
    
	
	public LoadBalanceTesterThread(Integer threadID, Integer iterations, String domain, String requestURL) {
		
		this.myThreadID = threadID;
		this.domain = domain;
		this.requestURL = requestURL;
		this.myIterations = iterations;
	}
	
	public void run() {
        
		System.out.println("Thread " + myThreadID + " just started ");
		
		sendMassiveRequests(myIterations, domain, requestURL);
    }

	
	
	private void sendMassiveRequests(Integer iterations, String domain, String requestURL){
		
		for(int i=0; i<iterations; i++) {
			sendSingleRequest(domain, requestURL);
			
			if((i%100)==0){
				System.out.println("Thread " + myThreadID + ": " + i + " requests have been sent ");
			}
		}
		
		System.out.println("Thread " + myThreadID + ": " + iterations + " requests have been sent ");
	}
	
	
	public void sendSingleRequest(String domain, String requestURL){

		try {
	    	//String domain = address.getHostString()+(address.getPort()>0?(":"+address.getPort()):"");
	        //URL url = new URL("http://"+domain+"/ds-web/rest/general/balancer/status");
	        
	        URL url = new URL(HTTP_PREFIX+domain+requestURL);
			
	        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	        HttpURLConnection.setFollowRedirects(false);
	        huc.setConnectTimeout(1000);
	        huc.setRequestMethod("GET");
	        huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
	        huc.connect();

	        BufferedReader in = new BufferedReader( new InputStreamReader(huc.getInputStream()));

	        in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}