package com.ds.di.balancer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

import org.xlightweb.server.HttpServer;


class LoadBalancer extends Thread {
	private HttpServer httpServer;
	private ServerCollection servers;
	private String logFile;
	private long updateInterval;
	
	public LoadBalancer(ServerCollection servers, long updateInterval, String logFile){
		this.servers = servers;
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

	@Override
	public void run(){
		synchronized (this) {
			
		}
		
    }	

	public static void main(String[] args) throws Exception {
		ServerCollection servers = new ServerCollection(new InetSocketAddress[] {
			new InetSocketAddress("10.0.0.2", 8080),
			new InetSocketAddress("10.0.0.2", 8081),
			new InetSocketAddress("10.0.0.11", 8888),
			new InetSocketAddress("10.0.0.11", 9888)
		});
		
		LoadBalancer balancer = new LoadBalancer(servers, 2000, "d:/balancerLog.txt");
		
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
        	   running = false;
        	   break;
    	   default:
    		   break;
           }
        }
        balancer.stopServer();
		
	}
}