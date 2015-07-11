package com.ds.di.balancer;

import java.net.InetSocketAddress;

import org.xlightweb.server.HttpServer;


class LoadBalancer {
	

	public static void main(String[] args) throws Exception {
		InetSocketAddress[] srvs = new InetSocketAddress[] {
			new InetSocketAddress("10.0.0.2", 8080),
			new InetSocketAddress("10.0.0.2", 8081),
			new InetSocketAddress("10.0.0.2", 8082),
			new InetSocketAddress("10.0.0.2", 8083)};
		//HttpServer loadBalancer = new HttpServer(80, new LoadBalancerHandler(srvs));
		//loadBalancer.run();
		
		ServerUnit unit = new ServerUnit(new InetSocketAddress("10.0.0.2", 8080));
		unit.getStatus();
	}
}