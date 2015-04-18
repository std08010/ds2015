/**
 * 
 */
package com.ds.di.utils;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

/**
 * @author Altin Cipi
 *
 */
public class AmazonS3Helper
{
	public static void uploadFile(String bucketname, String fileName, InputStream file, Long size, String accessKey, String secretKey) throws Exception
	{
		TransferManager tm = new TransferManager(new BasicAWSCredentials(accessKey, secretKey));

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(size);

		Upload upload = tm.upload(bucketname, fileName, file, metadata);
		upload.waitForCompletion();
		Logger.getRootLogger().info("Upload to S3 bucket completed successfuly.");
	}

	public static void uploadFile(String bucketname, String fileName, File file, String accessKey, String secretKey) throws Exception
	{
		TransferManager tm = new TransferManager(new BasicAWSCredentials(accessKey, secretKey));

		Upload upload = tm.upload(bucketname, fileName, file);
		upload.waitForCompletion();
		Logger.getRootLogger().info("Upload to S3 bucket completed successfuly.");
	}

	public static void deleteFile(String bucketname, String fileName, String accessKey, String secretKey) throws Exception
	{
		AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));

		s3Client.deleteObject(new DeleteObjectRequest(bucketname, fileName));
	}
}
