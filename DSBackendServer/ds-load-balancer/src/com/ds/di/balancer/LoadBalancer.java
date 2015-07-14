package com.ds.di.balancer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.xlightweb.server.HttpServer;


class LoadBalancer {
	private HttpServer httpServer;
	private ServerCollection servers;
	private String logFile;
	private long updateInterval;
	
	public LoadBalancer(ServerCollection servers, long updateInterval, String logFile){
		this.servers = servers;
		this.logFile = logFile;
		this.updateInterval = updateInterval;
	}
	public LoadBalancer(String configFilename, long updateInterval, String logFile){
		this.servers = getServers(configFilename);
		this.logFile = logFile;
		this.updateInterval = updateInterval;
	}
	
	public void startServer() throws IOException{

		try {
			if(httpServer!=null)
				httpServer.close();
			httpServer = new HttpServer(80, new LoadBalancerHandler(servers, logFile));
			servers.startStatusUpdates(updateInterval);
			httpServer.start();
		} catch (IOException e) {
			httpServer = null;
			e.printStackTrace();
		}
	}

	public void stopServer(){
		if(httpServer!=null){
			servers.stopStatusUpdates();
			httpServer.close();
			httpServer = null;
		}
	}
	
	private ServerCollection getServers(String configFilename){
		List<InetSocketAddress> list = new LinkedList<InetSocketAddress>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(configFilename))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	String[] s = line.split(":");
		    	if(s.length>0 && s.length<=2){
		    		list.add(new InetSocketAddress(s[0], s.length>1?Integer.parseInt(s[1]):80));
		    	}
		    }
		    
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InetSocketAddress[] servArray = new InetSocketAddress[list.size()];
		return new ServerCollection(list.toArray(servArray));
	}

	public static void main(String[] args) throws Exception {
		LoadBalancer balancer = new LoadBalancer("servers.ini", 2000, "balancerLog.txt");
		
		balancer.startServer();
		
        Scanner s = new Scanner(System.in);
        boolean running = true;
        while(running){
           String command = s.next();
           switch(command){
           case "start":
        	   balancer.startServer();
        	   break;
           case "stop":
        	   balancer.stopServer();
        	   break;
           case "exit":
        	   balancer.stopServer();
        	   running = false;
        	   break;
    	   default:
    		   break;
           }
        }
        balancer.stopServer();
	}
}