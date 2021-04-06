package com.secure.files;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File encrypter/decrypter class.
 *
 * @author neha_pandey
 *
 */
public class FileEncrypterDecrypter {

	private static final Logger logger = LoggerFactory.getLogger(FileEncrypterDecrypter.class);

	private final SecretKey secretKey;
	private final Cipher cipher;

	public FileEncrypterDecrypter(final SecretKey secretKey, final String transformation)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		this.secretKey = secretKey;
		this.cipher = Cipher.getInstance(transformation);
	}

	/**
	 * Encrypts content and saves it to the file.
	 *
	 * @param content
	 * @param fileName
	 */
	public void encrypt(final String content, final String fileName) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		} catch (final InvalidKeyException ex) {
			logger.error("cipher encryption initialization error", ex);
			throw new CipherException("cipher encryption initialization error", ex);
		}

		final byte[] iv = cipher.getIV();

		try (FileOutputStream fileOut = new FileOutputStream(fileName);
				CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
			fileOut.write(iv);
			cipherOut.write(content.getBytes());
		} catch (final IOException ex) {
			logger.error("file IO error ", ex);
			throw new CipherException("file IO error", ex);
		}
	}

	/**
	 * Decrypts file.
	 *
	 * @param fileName
	 * @return
	 */
	public String decrypt(final String fileName) {
		try (FileInputStream fileIn = new FileInputStream(fileName)) {
			final byte[] fileIv = new byte[16];
			fileIn.read(fileIv);

			try {
				cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));
			} catch (InvalidKeyException | InvalidAlgorithmParameterException ex) {
				logger.error("cipher decryption initialization error", ex);
				throw new CipherException("cipher decryption initialization error", ex);
			}

			try (CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
					InputStreamReader inputReader = new InputStreamReader(cipherIn);
					BufferedReader reader = new BufferedReader(inputReader)) {

				final StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			}
		} catch (final IOException e) {
			logger.error("file decryption error- io exception", e);
			throw new CipherException("file decryption error- io exception ", e);
		}
	}

}
