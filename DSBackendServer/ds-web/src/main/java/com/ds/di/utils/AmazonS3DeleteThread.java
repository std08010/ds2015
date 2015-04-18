/**
 * 
 */
package com.ds.di.utils;

import org.apache.log4j.Logger;

/**
 * @author Altin Cipi
 *
 */
public class AmazonS3DeleteThread extends Thread
{
	private String	fileName;
	private String	bucketName;
	private String	accessKey;
	private String	secretKey;

	public AmazonS3DeleteThread(String fileName, String bucketName, String accessKey, String secretKey)
	{
		this.fileName = fileName;
		this.bucketName = bucketName;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	@Override
	public void run()
	{
		try
		{
			AmazonS3Helper.deleteFile(bucketName, fileName, accessKey, secretKey);
		}
		catch (Exception e)
		{
			Logger.getLogger(AmazonS3DeleteThread.class).error(e.getMessage(), e);
		}
	}
}
