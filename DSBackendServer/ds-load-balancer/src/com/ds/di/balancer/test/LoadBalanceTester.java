package com.ds.di.balancer.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;



class LoadBalanceTester {
	
	private static final String 	HTTP_PREFIX		= "http://";
	private static final String 	DOMAIN 			= "10.0.0.2:80/";
	private static final String 	HOME_URL 		= "ds-web";
	private static final String 	REST_TEST_URL 	= "ds-web/rest/general/test/calculate";
	
	private static final Integer	NO_OF_ITERATIONS_1K		= 1000;
	private static final Integer	NO_OF_ITERATIONS_10K	= 10000;
	private static final Integer	NO_OF_ITERATIONS_100K	= 100000;
	private static final Integer	NO_OF_ITERATIONS_1M		= 1000000;
	private static final Integer	NO_OF_ITERATIONS_10M	= 10000000;
	private static final Integer	NO_OF_ITERATIONS_100M	= 100000000;
	

	public static void main(String[] args) throws Exception {
		
		sendMassiveRequests(NO_OF_ITERATIONS_10K, DOMAIN, HOME_URL);
	}
	
	
	private static void sendMassiveRequests(Integer iterations, String domain, String requestURL){
		
		for(int i=0; i<iterations; i++)
			sendSingleRequest(domain, requestURL);
	}
	
	
	public static void sendSingleRequest(String domain, String requestURL){

		try {
	    	//String domain = address.getHostString()+(address.getPort()>0?(":"+address.getPort()):"");
	        //URL url = new URL("http://"+domain+"/ds-web/rest/general/balancer/status");
	        
	        URL url = new URL(HTTP_PREFIX+domain+requestURL);
			
			
	        URLConnection yc = url.openConnection();
	        BufferedReader in = new BufferedReader( new InputStreamReader(yc.getInputStream()));

	        in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}