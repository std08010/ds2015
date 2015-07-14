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

public class ServerUnit {
	public ServerUnit(InetSocketAddress address){
		this.address = address;
	}
	private InetSocketAddress address;
	public double rank;
	
	public void getStatus(){
	    try {
	    	String domain = address.getHostString()+(address.getPort()>0?(":"+address.getPort()):"");
	    	String url = "http://"+domain+"/ds-web/rest/general/balancer/status";
			
	    	HttpClient httpClient = new HttpClient();
		    httpClient.setAutoHandleCookies(false);
		    System.out.println(url);
		    IHttpRequest request = new HttpRequest("GET", url);
		    request.setHost(domain);
		    
			IHttpResponseHandler respHdl = new IHttpResponseHandler() {
				@Execution(Execution.NONTHREADED)
				public void onResponse(IHttpResponse response) throws IOException {
					System.out.println("response");
					System.out.println(response.getBlockingBody().readString());
				}
				@Execution(Execution.NONTHREADED)
				public void onException(IOException ioe) throws IOException {
					System.out.println("no response");
					System.out.println("Error "+ioe.getMessage());
				}
			};
			httpClient.send(request, respHdl);
			httpClient.close();
			
			
			
	        URL yahoo = new URL(url);
	        URLConnection yc = yahoo.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                yc.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null) 
	            System.out.println(inputLine);
	        in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
