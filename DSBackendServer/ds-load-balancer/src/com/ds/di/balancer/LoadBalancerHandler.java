package com.ds.di.balancer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;

import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.IHttpResponse;
import org.xlightweb.IHttpResponseHandler;
import org.xlightweb.client.HttpClient;
import org.xsocket.Execution;
import org.xsocket.ILifeCycle;

//import sun.net.www.http.HttpClient;

class LoadBalancerHandler implements IHttpRequestHandler, ILifeCycle {
	private ServerCollection servers;
	private HttpClient httpClient;
	private File logFile;
	private BufferedWriter logWriter;
	private long initTimestamp;

	/*
	 * this class does not implement server monitoring or healthiness checks
	 */

	public LoadBalancerHandler(ServerCollection servers, String logOutput) {
		this.servers = servers;
		initTimestamp = System.nanoTime();
		if(logOutput!=null && logOutput.length()>0){
			try {
				logFile = new File(logOutput);
				if (!logFile.exists()) {
					logFile.createNewFile();
				}

				FileWriter fw;
				fw = new FileWriter(logFile.getAbsoluteFile());
				logWriter = new BufferedWriter(fw);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
			logFile = null;
			logWriter = null;
		}
	}

	public void onInit() {
		httpClient = new HttpClient();
		httpClient.setAutoHandleCookies(false);
	}


	public void onDestroy() throws IOException {
		httpClient.close();
		if(logWriter!=null){
			logWriter.flush();
			logWriter.close();
		}
	}

	public void onRequest(final IHttpExchange exchange) throws IOException {
		final long requestStart = System.nanoTime();
		IHttpRequest request = exchange.getRequest();

		while(true){

			// retrieve the business server address and update the Request-URL of the request
			final ServerUnit serverUnit = servers.getForRequest();
			final long requestTimestamp = System.nanoTime() - initTimestamp;
			final double serverRank = serverUnit.getRank();
			final double serverCpuUsage = serverUnit.getCpuUsage();
			final double serverLatency = serverUnit.getLatency();
			//InetSocketAddress server = new InetSocketAddress(serverUnit.getAddress().getAddress().getHostAddress(), serverUnit.getAddress().getPort());
			InetSocketAddress server = serverUnit.getAddress();
			final URL url = request.getRequestUrl();
			final URL newUrl = new URL(url.getProtocol(), server.getHostName(), server.getPort(), url.getFile());
	
			final String oldDomain = "://"+url.getHost()+(url.getPort()>0?(":"+url.getPort()):"");
			final String newDomain = "://"+newUrl.getHost()+":"+newUrl.getPort();
	
			request.setHost(newUrl.getHost()+":"+newUrl.getPort());
	
	
			// create a response handler to forward the response to the caller
			IHttpResponseHandler respHdl = new IHttpResponseHandler() {
	
				@Execution(Execution.NONTHREADED)
				public void onResponse(IHttpResponse response) throws IOException {
					long requestTime = System.nanoTime() - requestStart;
					if(logWriter!=null)
						logRequest(requestTimestamp, serverUnit, serverRank, serverCpuUsage, serverLatency, requestTime);
					String location = response.getHeader("Location");
					if(location != null && location.length()>0){
						if(location.contains(newDomain)){
							location = location.replace(newDomain, oldDomain);
							response.setHeader("Location", location);
						}
					}
					try {
						exchange.send(response);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw e;
					}
				}
	
				@Execution(Execution.NONTHREADED)
				public void onException(IOException ioe) throws IOException {
					exchange.sendError(ioe);
				}
			};

		// forward the request in a asynchronous way by passing over the response handler
			try {
				httpClient.send(request, respHdl);
				break;
			} catch (IOException e) {
				servers.registerServerFailure(serverUnit);
				logServerFailure(requestTimestamp, serverUnit);
			}
		}
		
	}
	
	private void logServerFailure(long requestTimestamp, ServerUnit server){
		try {
			String logEntry = "FAILURE "+ server.getAddress().toString()+ "\n";
			logEntry = (int)(requestTimestamp/100000000)+","+server.getIndex()+",-1,-1,-1,-1,0\n";
			//System.out.print(logEntry);
			logWriter.append(logEntry);
			//logWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void logRequest(long requestTimestamp, ServerUnit server, double serverRank, double serverCpuUsage, double serverLatency, long requestTime){
		try {
			String logEntry = (int)(requestTimestamp/100000000)+","+server.getIndex()+","+serverRank+","+serverCpuUsage+","+serverLatency+","+((double)requestTime)*0.000001+",1\n";
			//String logEntry = server.getAddress().toString() + " "+serverRank+" : "+requestTime* 0.0000001 + "\n";

			//System.out.print(logEntry);
			logWriter.append(logEntry);
			//logWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}