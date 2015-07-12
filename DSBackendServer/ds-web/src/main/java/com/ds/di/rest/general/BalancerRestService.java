/**
 * 
 */
package com.ds.di.rest.general;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ds.di.utils.RestServiceUtils;

/**
 * @author Altin Cipi
 *
 */
@Component(value = BalancerRestService.SPRING_KEY)
@Transactional
@Path("/general/balancer")
public class BalancerRestService
{
	public static final String	SPRING_KEY	= "com.ds.di.rest.general.BalancerRestService";
	
	/**
	 * @return
	 */
	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getStatus()
	{
		double contention = 0;
		try
		{
			MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();

			OperatingSystemMXBean osMBean = ManagementFactory.newPlatformMXBeanProxy(
			mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
			

			long nanoBefore = System.nanoTime();
			//performWork(100000);
			//contention = (double)(System.nanoTime() - nanoBefore)* 0.000001;
			contention = getProcessCpuLoad();
			if(contention<0)
				contention = 100;
			

		}
		catch (Exception e)
		{
			e.printStackTrace();

			return RestServiceUtils.getErrorResponse(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.status(Status.OK).entity((new Double(contention))).build();
	}
	
	public static double getProcessCpuLoad() throws MalformedObjectNameException, ReflectionException, InstanceNotFoundException {

	    MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
	    ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
	    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

	    if (list.isEmpty())     return -1;

	    Attribute att = (Attribute)list.get(0);
	    Double value  = (Double)att.getValue();

	    if (value == -1.0)      return -1;  // usually takes a couple of seconds before we get real values

	    return value * 100;        // returns a percentage value with 1 decimal point precision
	}
	
	public static double getMemoryContention(){
		return 0.0;
	}
	
	void performWork(int iterations){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			for(int i=0; i<iterations; i++)
				md.digest("test".getBytes());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
