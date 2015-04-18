/**
 * 
 */
package com.ds.di;

import java.io.File;

import com.ds.di.utils.AmazonS3Helper;

/**
 * @author Altin Cipi
 *
 */
public class AmazonS3ClientTest
{
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		AmazonS3Helper.uploadFile("dsdi", "avatars/test.jpg", new File("E://PHOTOS//rafina 2014//2014-10-25 16.30.51.jpg"), "AKIAJCEGXAKUV4LR5QFA", "+t2lyaRqUsJn5ENEte19+tNFXG7dIaAVftTkPlX/");
	}
}
