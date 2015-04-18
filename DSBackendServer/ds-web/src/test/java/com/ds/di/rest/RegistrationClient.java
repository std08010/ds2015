/**
 * 
 */
package com.ds.di.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.ds.di.dto.rest.ErrorDTO;
import com.ds.di.dto.rest.user.RegistrationCreateInDTO;
import com.ds.di.dto.rest.user.RegistrationCreateOutDTO;
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
public class RegistrationClient
{
	static
	{
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

			WebResource webResource = client.resource("https://localhost:8443/ds-web/rest/secure/user/register/create");

			RegistrationCreateInDTO input = new RegistrationCreateInDTO();
			input.setUsername("testuser5");
			input.setPassword("abc123");
			input.setEmail("email2@mahs.com");
			input.setCountry("Greece");

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);

			if (response.getStatus() != Status.OK.getStatusCode())
			{
				ErrorDTO error = response.getEntity(ErrorDTO.class);
				System.out.println("Failed : HTTP error code : " + response.getStatus() + " " + Status.fromStatusCode(response.getStatus()) + ", error message : " + error.getMessage());
				return;
			}

			System.out.println("Output from Server .... \n");
			RegistrationCreateOutDTO output = response.getEntity(RegistrationCreateOutDTO.class);
			System.out.println(output.getSessionToken());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
