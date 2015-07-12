package com.ds.di.balancer;

import java.net.InetSocketAddress;

import org.xlightweb.server.HttpServer;


class LoadBalancer {
	

	public static void main(String[] args) throws Exception {
		ServerCollection servers = new ServerCollection(new InetSocketAddress[] {
			new InetSocketAddress("10.0.0.2", 8080),
			new InetSocketAddress("10.0.0.2", 8081)
			//new InetSocketAddress("10.0.0.2", 8082),
			//new InetSocketAddress("10.0.0.2", 8083)
		});

		servers.startStatusUpdates(2000);
		
		HttpServer loadBalancer = new HttpServer(80, new LoadBalancerHandler(servers));
		loadBalancer.run();
		
	}
}