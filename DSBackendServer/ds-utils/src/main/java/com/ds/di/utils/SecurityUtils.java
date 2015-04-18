package com.ds.di.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Created by Altin Cipi
 */
public class SecurityUtils
{
	private final static String			jasyptPassword	= "memaisthebest";
	private final static String			jasyptAlgorithm	= "PBEWithMD5AndTripleDES";
	// private static StandardPBEStringEncryptor encryptor;
	private static BasicTextEncryptor	encryptor;

	// public static StandardPBEStringEncryptor getJasyptEncryptor()
	// {
	// if(encryptor == null)
	// {
	// encryptor = new StandardPBEStringEncryptor();
	// encryptor.setPassword(jasyptPassword);
	// encryptor.setAlgorithm(jasyptAlgorithm);
	// }
	//
	// return encryptor;
	// }

	public static BasicTextEncryptor getJasyptEncryptor()
	{
		if (encryptor == null)
		{
			encryptor = new BasicTextEncryptor();
			encryptor.setPassword(jasyptPassword);
		}

		return encryptor;
	}

	public static String get_SHA_1_SecurePassword(String passwordToHash)
	{
		String generatedPassword = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(passwordToHash.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}

	public static String getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}

	public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt().getBytes();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}

	private static String toHex(byte[] array) throws NoSuchAlgorithmException
	{
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0)
		{
			return String.format("%0" + paddingLength + "d", 0) + hex;
		}
		else
		{
			return hex;
		}
	}

	public static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String[] parts = storedPassword.split(":");
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);

		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int diff = hash.length ^ testHash.length;
		for (int i = 0; i < hash.length && i < testHash.length; i++)
		{
			diff |= hash[i] ^ testHash[i];
		}
		return diff == 0;
	}

	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
	{
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}
}
