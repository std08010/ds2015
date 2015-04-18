/**
 * 
 */
package com.ds.di.utils;

import java.io.ByteArrayInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * @author Altin Cipi
 *
 */
public class AmazonS3UploadThread extends Thread
{
	private String	fileStr;
	private String	fileName;
	private String	bucketName;
	private String	accessKey;
	private String	secretKey;

	public AmazonS3UploadThread(String fileStr, String fileName, String bucketName, String accessKey, String secretKey)
	{
		this.fileStr = fileStr;
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
			byte[] source = Base64.decodeBase64(fileStr);
			AmazonS3Helper.uploadFile(bucketName, fileName, new ByteArrayInputStream(source), Long.valueOf(source.length), accessKey, secretKey);
		}
		catch (Exception e)
		{
			Logger.getLogger(AmazonS3UploadThread.class).error(e.getMessage(), e);
		}
	}
}
