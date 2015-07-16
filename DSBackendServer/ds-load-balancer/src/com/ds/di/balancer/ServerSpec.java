package com.ds.di.balancer;

public class ServerSpec {
	public ServerSpec(String host, int port, Integer tlsPort){
		this.host = host;
		this.port = port;
		this.tlsPort = tlsPort;
	}
	public String host;
	public int port;
	public Integer tlsPort;
}
