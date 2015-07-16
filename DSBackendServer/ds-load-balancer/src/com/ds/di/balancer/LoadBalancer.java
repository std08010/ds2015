package com.ds.di.balancer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.xlightweb.server.HttpServer;


class LoadBalancer {
	private HttpServer httpServer;
	private HttpServer httpServerSSL;
	private ServerCollection servers;
	private String logFile;
	private long updateInterval;
	private SSLContext sslContext;
	
	public LoadBalancer(String configFilename, long updateInterval, String logFile){
		this.servers = getServers(configFilename);
		this.logFile = logFile;
		this.updateInterval = updateInterval;
		try {
			initSslContext();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initSslContext() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException, FileNotFoundException, IOException{
		KeyStore ks = KeyStore.getInstance("JKS"); // Load the keystore
		ks.load(new FileInputStream("server.keystore"), "evotepass".toCharArray()); // Load as required from the inputstream of your choice, for example.

		KeyStore ts = null;

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, "evotepass".toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ts);

		sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
	}
	
	public void startServer() throws IOException{

		try {
			if(httpServer!=null)
				httpServer.close();
			httpServer = new HttpServer(80, new LoadBalancerHandler(servers, logFile, null));
			if(sslContext!=null){
				if(httpServerSSL!=null)
					httpServerSSL.close();
				httpServerSSL = new HttpServer(443, new LoadBalancerHandler(servers, logFile, sslContext), sslContext, true);
			}
			servers.startStatusUpdates(updateInterval);
			httpServer.start();
			if(httpServerSSL!=null)
				httpServerSSL.start();
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
			if(httpServerSSL!=null)
				httpServerSSL.close();
			httpServerSSL = null;
		}
	}
	
	private ServerCollection getServers(String configFilename){
		List<ServerSpec> list = new LinkedList<ServerSpec>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(configFilename))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] s = line.split(":");
		    	if(s.length>0 && s.length<=3){
		    		list.add(new ServerSpec(s[0], (s.length>1?Integer.parseInt(s[1]):80), (s.length>2?Integer.parseInt(s[2]):null)));
		    	}
		    }
		    
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServerSpec[] servArray = new ServerSpec[list.size()];
		return new ServerCollection(list.toArray(servArray));
	}

	public static void main(String[] args) throws Exception {
		LoadBalancer balancer = new LoadBalancer("servers.ini", 2000, null);
		
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