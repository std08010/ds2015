/**
 * 
 */
package com.ds.di.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.ds.di.dto.rest.ErrorDTO;
import com.ds.di.dto.rest.SimpleDTO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * @author Altin Cipi
 *
 */
public class StressTestClient extends Thread
{
	static
	{
		// for localhost testing only
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier()
		{
			public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession)
			{
				if (hostname.equals("localhost") || hostname.equals("ds-di-180488603.eu-west-1.elb.amazonaws.com"))
				{
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void run()
	{
		try
		{
			ClientConfig clientConfig = new DefaultClientConfig();

			clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

			Client client = Client.create(clientConfig);

			WebResource webResource = client.resource("http://ec2-52-17-140-15.eu-west-1.compute.amazonaws.com/ds-web/rest/general/test/stress/sleep_no_transactions");

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != Status.OK.getStatusCode())
			{
				ErrorDTO error = response.getEntity(ErrorDTO.class);
				System.out.println("Failed : HTTP error code : " + response.getStatus() + " " + Status.fromStatusCode(response.getStatus()) + ", error message : " + error.getMessage());
				return;
			}

			System.out.println("Output from Server .... \n");
			SimpleDTO output = response.getEntity(SimpleDTO.class);
			System.out.println(output.getResult());
		}
		catch (Exception e)
		{
			Logger.getLogger(this.getClass()).error(e.getMessage(), e);
		}
	}

	public static void main(String[] args)
	{
		try
		{
			for (int i = 0; i < 603; i++)
			{
				StressTestClient thread = new StressTestClient();
				thread.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
