package com.secure.files;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHex;
import static org.apache.commons.io.FileUtils.readFileToByteArray;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.File;
import java.io.IOException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Secret key repository.
 *
 * @author neha_pandey
 *
 */
@Component
public class SecretKeyRepository {

	private static final Logger logger = LoggerFactory.getLogger(SecretKeyRepository.class);

	private final String secretKeyDrive;

	@Autowired
	public SecretKeyRepository(@Value("${secure_keys_directory}") final String secretKeyDrive) {
		this.secretKeyDrive = secretKeyDrive;
	}

	public void saveKey(final String userName, final String fileName, final SecretKey key) {
		try {
			createDir(userName);
			final File file = new File(getFilePath(userName, fileName));
			logger.info("saving secret key to {}", file.getPath());
			saveKey(key, file);
		} catch (final IOException ex) {
			logger.error("error saving secret-key error", ex);
			throw new CipherException("error in saving secret", ex);
		}
	}

	public SecretKey getKey(final String userName, final String fileName) {
		try {
			final File file = new File(this.secretKeyDrive + File.separator + userName + File.separator + fileName);
			logger.info("retriving secret key to {}", file.getPath());
			return loadKey(file);
		} catch (final IOException ex) {
			logger.error("error saving secret-key error", ex);
			throw new CipherException("error in saving secret", ex);
		}
	}

	private String getFilePath(final String userId, final String fileId) {
		return this.secretKeyDrive + File.separator + userId + File.separator + fileId;
	}

	private void createDir(final String userId) {
		new File(this.secretKeyDrive + File.separator + userId).mkdirs();
	}

	private static void saveKey(final SecretKey key, final File file) throws IOException {
		final char[] hex = encodeHex(key.getEncoded());
		writeStringToFile(file, String.valueOf(hex));
	}

	private static SecretKey loadKey(final File file) throws IOException {
		final String data = new String(readFileToByteArray(file));
		byte[] encoded;
		try {
			encoded = decodeHex(data.toCharArray());
		} catch (final DecoderException e) {
			e.printStackTrace();
			return null;
		}
		return new SecretKeySpec(encoded, "AES");
	}
}
