package com.ds.di.balancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

public class ServerUnit  implements Comparable<ServerUnit>{
	private static final double statusUpdateWeight = 0.75;
	private static final double requestUpdateWeight = 0.5;
	private static final double requestWeight = 1.0;
	
	private double cpuUsage;
	private double latency;
	private int index;
	
	public ServerUnit(ServerSpec spec){
		
		address = new InetSocketAddress(spec.host, spec.port);
		this.addressHTTP = address;
		this.addressHTTPS = spec.tlsPort==null?null:new InetSocketAddress(address.getAddress(), spec.tlsPort);
		rank = 100.0;
		cpuUsage = 100;
		latency = 10000;
	}
	
	@Deprecated
	public ServerUnit(InetSocketAddress address, Integer tlsPort){
		this.address = address;
		this.addressHTTP = address;
		this.addressHTTPS = tlsPort==null?null:new InetSocketAddress(address.getAddress(), tlsPort);
		rank = 100.0;
		cpuUsage = 100;
		latency = 10000;
	}
	private InetSocketAddress address;
	private InetSocketAddress addressHTTP;
	private InetSocketAddress addressHTTPS;
	public Double rank;
	
	public synchronized double getRank(){
		return rank;
	}
	public synchronized void setRank(double r){
		rank = r;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
	public double getCpuUsage(){
		return cpuUsage;
	}

	public double getLatency(){
		return latency;
	}
	
	public InetSocketAddress getAddress(){
		return address;
	}
	public InetSocketAddress getAddressHTTP(){
		return addressHTTP;
	}
	public InetSocketAddress getAddressHTTPS(){
		return addressHTTPS;
	}
	
	public void getStatus(){
		boolean couldConnect = false;
		double contention = 100;
		long startTime = System.nanoTime();
	    try {
	    	String domain = addressHTTP.getHostString()+(addressHTTP.getPort()>0?(":"+addressHTTP.getPort()):"");
	        URL url = new URL("http://"+domain+"/ds-web/rest/general/balancer/status");
			
	        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	        HttpURLConnection.setFollowRedirects(false);
	        huc.setConnectTimeout(2 * 1000);
	        huc.setRequestMethod("GET");
	        huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
	        try{
	        huc.connect();
	        }catch(Exception ex){}
			
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                huc.getInputStream()));
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
	    		rank = statusUpdateWeight*newRank + (1.0-statusUpdateWeight)*rank;
	    		cpuUsage = contention;
	    		latency = (double)(stopTime-startTime)*0.000001;
			}
	    } else {
	    	synchronized (rank) {
		    	rank = (double) 10000;
		    	cpuUsage = 100;
		    	latency = 10000;
			}
	    }
	}
	
	public synchronized void recordRequest(){
    	double newRank = rank + requestWeight;
		rank = requestUpdateWeight*newRank + (1.0-requestUpdateWeight)*rank;				
	}

	@Override
	public int compareTo(ServerUnit arg0) {
		synchronized (this) {
			return rank==arg0.rank?0:(rank<arg0.rank?-1:1);
		}
	}
	
	@Override
	public String toString(){
		return address.getHostString()+":"+address.getPort()+(addressHTTPS!=null?(":"+addressHTTPS.getPort()):"")+" " + ((int)(getRank()*10))*0.1;
	}
}

