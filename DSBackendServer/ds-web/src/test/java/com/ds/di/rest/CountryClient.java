/**
 * 
 */
package com.ds.di.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.ds.di.dto.rest.ErrorDTO;
import com.ds.di.dto.rest.general.CountryAllOutDTO;
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
public class CountryClient
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

	public static void main(String[] args)
	{
		try
		{
			ClientConfig clientConfig = new DefaultClientConfig();

			clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

			Client client = Client.create(clientConfig);

			WebResource webResource = client.resource("http://localhost:8080/ds-web/rest/general/country/all");

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != Status.OK.getStatusCode())
			{
				ErrorDTO error = response.getEntity(ErrorDTO.class);
				System.out.println("Failed : HTTP error code : " + response.getStatus() + " " + Status.fromStatusCode(response.getStatus()) + ", error message : " + error.getMessage());
				return;
			}

			System.out.println("Output from Server .... \n");
			CountryAllOutDTO output = response.getEntity(CountryAllOutDTO.class);
			for (String country : output.getCountries())
			{
				System.out.println(country);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
