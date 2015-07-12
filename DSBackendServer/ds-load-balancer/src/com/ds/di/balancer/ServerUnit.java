package com.ds.di.balancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

import org.xlightweb.HttpRequest;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpResponse;
import org.xlightweb.IHttpResponseHandler;
import org.xlightweb.client.HttpClient;
import org.xsocket.Execution;

public class ServerUnit  implements Comparable<ServerUnit>{
	private static final double weight = 0.2;
	private static final double requestWeight = 1.0;
	public ServerUnit(InetSocketAddress address){
		this.address = address;
		rank = 100.0;
	}
	private InetSocketAddress address;
	public Double rank;
	
	public InetSocketAddress getAddress(){
		return address;
	}
	
	public void getStatus(){
		boolean couldConnect = false;
		double contention = 100;
		long startTime = System.nanoTime();
	    try {
	    	String domain = address.getHostString()+(address.getPort()>0?(":"+address.getPort()):"");
	        URL url = new URL("http://"+domain+"/ds-web/rest/general/balancer/status");
			
			
	        URLConnection yc = url.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                yc.getInputStream()));
	        String inputLine;
	        
	        while ((inputLine = in.readLine()) != null){ 
	            contention = Double.parseDouble(inputLine);
	            break;
	        }
	        in.close();
	        couldConnect = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	    long stopTime = System.nanoTime();
	    if(couldConnect){
	    	double newRank = ((double)(stopTime-startTime))*0.000001 + contention;
	    	synchronized (rank) {
	    		rank = weight*newRank + (1.0-weight)*rank;				
			}
	    }
	}
	
	public void recordRequest(){
    	synchronized (rank) {
        	double newRank = rank + requestWeight;
    		rank = weight*newRank + (1.0-weight)*rank;				
		}
	}

	@Override
	public int compareTo(ServerUnit arg0) {
		return rank==arg0.rank?0:(rank<arg0.rank?-1:1);
	}
	
	@Override
	public String toString(){
		return address.getHostString()+address.getPort()+" " + ((int)(rank*10))*0.1;
	}
}

