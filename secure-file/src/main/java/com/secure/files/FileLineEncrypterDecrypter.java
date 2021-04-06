package com.secure.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class FileLineEncrypterDecrypter {

	private static final String UTF_8 = "UTF-8";

	private static SecretKeySpec secretKey;
	private static byte[] key;

	public static void setKey(final String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void encryptLineByLine(final String secret, final File in, final File out) throws Exception {
		setKey(secret);
		final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		try (BufferedReader reader = new BufferedReader(new FileReader(in));
				CipherOutputStream cryptWriter = new CipherOutputStream(new FileOutputStream(out), cipher)) {
			String line;
			while ((line = reader.readLine()) != null) {
				line += String.format("%n");
				cryptWriter.write(line.getBytes(UTF_8));
			}
		}
	}

	public static void decryptLineByLine(final String secret, final File in, final File out) throws Exception {
		setKey(secret);
		final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		try (BufferedReader cryptReader = new BufferedReader(
				new InputStreamReader(new CipherInputStream(new FileInputStream(in), cipher), UTF_8));
				FileWriter writer = new FileWriter(out)) {

			String line;
			while ((line = cryptReader.readLine()) != null) {
				line += String.format("%n");
				writer.write(line);
			}
		}
	}

}
