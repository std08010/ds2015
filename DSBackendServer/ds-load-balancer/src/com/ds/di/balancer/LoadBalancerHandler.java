package com.ds.di.balancer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	   /*
	    * this class does not implement server monitoring or healthiness checks
	    */

	   public LoadBalancerHandler(ServerCollection servers) {
	      this.servers = servers;
	   }

	  public void onInit() {
	      httpClient = new HttpClient();
	      httpClient.setAutoHandleCookies(false);
	}


	   public void onDestroy() throws IOException {
	      httpClient.close();
	   }

	   public void onRequest(final IHttpExchange exchange) throws IOException {
	      IHttpRequest request = exchange.getRequest();




	      // retrieve the business server address and update the Request-URL of the request
	      InetSocketAddress server = servers.getForRequest().getAddress();
	      final URL url = request.getRequestUrl();
	      final URL newUrl = new URL(url.getProtocol(), server.getHostName(), server.getPort(), url.getFile());
	      
	      final String oldDomain = "://"+url.getHost()+(url.getPort()>0?(":"+url.getPort()):"");
	      final String newDomain = "://"+newUrl.getHost()+":"+newUrl.getPort();
	      
	      request.setHost(newUrl.getHost()+":"+newUrl.getPort());

	      // proxy header handling (remove hop-by-hop headers, ...)
	      // ...


	      // create a response handler to forward the response to the caller
	      IHttpResponseHandler respHdl = new IHttpResponseHandler() {

	         @Execution(Execution.NONTHREADED)
	         public void onResponse(IHttpResponse response) throws IOException {
	        	 String location = response.getHeader("Location");
	        	 if(location != null && location.length()>0){
	        		 if(location.contains(newDomain)){
		        		 location = location.replace(newDomain, oldDomain);
		        		 response.setHeader("Location", location);
	        		 }
	        	 }
	            exchange.send(response);
	         }

	         @Execution(Execution.NONTHREADED)
	         public void onException(IOException ioe) throws IOException {
	            exchange.sendError(ioe);
	         }
	      };

	      // forward the request in a asynchronous way by passing over the response handler
	      httpClient.send(request, respHdl);
	   }
	}